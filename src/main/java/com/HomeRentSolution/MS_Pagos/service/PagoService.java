package com.HomeRentSolution.MS_Pagos.service;

import com.HomeRentSolution.MS_Pagos.config.AppConfig;
import com.HomeRentSolution.MS_Pagos.client.ReservaClient;
import com.HomeRentSolution.MS_Pagos.dto.PagoCancelacionEvento;
import com.HomeRentSolution.MS_Pagos.dto.PagoCreacionEvento;
import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import com.HomeRentSolution.MS_Pagos.exception.PagoNoEncontradoException;
import com.HomeRentSolution.MS_Pagos.model.EstadoPago;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import com.HomeRentSolution.MS_Pagos.repository.PagoRepository;
import lombok.extern.slf4j.Slf4j;
import feign.FeignException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class PagoService {

    private final PagoRepository pagoRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ReservaClient reservaClient;

    @Value("${ms.reservas.validacion-habilitada:true}")
    private boolean validacionReservaHabilitada;

    @Autowired
    public PagoService(PagoRepository pagoRepository, RabbitTemplate rabbitTemplate, ReservaClient reservaClient) {
        this.pagoRepository = pagoRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.reservaClient = reservaClient;
    }

    @Transactional
    public PagoResponseDTO crearPago(ReservaDTO request) {

        if (validacionReservaHabilitada) {
            validarReservaRemota(request);
        } else {
            log.warn("Validación remota de reserva DESHABILITADA (perfil sin ms-reservas disponible)");
        }

        validarReservaRemota(request);

        Pago nuevoPago = new Pago();
        nuevoPago.setIdReserva(request.getIdReserva());
        nuevoPago.setIdPropiedad(request.getIdPropiedad());
        nuevoPago.setIdInquilino(request.getIdInquilino());
        nuevoPago.setMontoTotal(request.getMontoTotal());
        nuevoPago.setMontoPagado(BigDecimal.ZERO);
        nuevoPago.setFechaVencimiento(LocalDateTime.now().plusDays(3));
        nuevoPago.setFechaPago(null);
        nuevoPago.setEstadoPago(EstadoPago.PENDIENTE);

        pagoRepository.save(nuevoPago);


        PagoCreacionEvento evento = new PagoCreacionEvento(
                request.getIdReserva(),
                request.getIdPropiedad(),
                request.getIdInquilino(),
                request.getMontoTotal()
        );
        rabbitTemplate.convertAndSend(AppConfig.PAGOS_EXCHANGE, AppConfig.ROUTING_CREADO, evento);
        log.info("[RabbitMQ] Mensaje enviado al exchange de pagos para la Reserva: {}", request.getIdReserva());

        // Armamos la respuesta corta de confirmación
        PagoResponseDTO response = new PagoResponseDTO();
        response.setIdPago(nuevoPago.getIdPago());
        response.setEstadoPago(nuevoPago.getEstadoPago());

        return response;
    }

    private void validarReservaRemota(ReservaDTO request) {
        try {
            ReservaDTO reserva = reservaClient.obtenerPorIdReserva(request.getIdReserva());
            if (reserva == null || !request.getIdReserva().equals(reserva.getIdReserva())) {
                throw new IllegalStateException("La respuesta de MS-Reservas no corresponde a la reserva solicitada");
            }
        } catch (FeignException.NotFound ex) {
            throw new IllegalArgumentException("No existe la reserva ID: " + request.getIdReserva(), ex);
        } catch (FeignException ex) {
            log.error("Error remoto al validar la reserva {}: HTTP {}", request.getIdReserva(), ex.status());
            throw new IllegalStateException("MS-Reservas no esta disponible para validar el pago", ex);
        }
    }

    @Transactional
    public void cancelarPago(ReservaDTO request) {
        Pago pagoExistente = pagoRepository.findByIdReserva(request.getIdReserva())
                .orElseThrow(() -> new PagoNoEncontradoException("No se encontró el pago para la reserva ID: " + request.getIdReserva()));

        pagoExistente.setEstadoPago(EstadoPago.CANCELADO);
        pagoRepository.save(pagoExistente);


        PagoCancelacionEvento evento = new PagoCancelacionEvento(
                request.getIdReserva(),
                pagoExistente.getIdPropiedad(),
                pagoExistente.getIdInquilino(),
                pagoExistente.getMontoReembolso()
        );
        rabbitTemplate.convertAndSend(AppConfig.PAGOS_EXCHANGE, AppConfig.ROUTING_ELIMINADO, evento);
        log.info("[RabbitMQ] Mensaje de cancelación enviado para la Reserva: {}", request.getIdReserva());
    }

    @Transactional
    public void eliminarPago(Long id) {
        Pago pago = pagoRepository.findByIdPago(id)
                .orElseThrow(() -> new PagoNoEncontradoException("No se pudo eliminar: Pago no encontrado con ID: " + id));
        pagoRepository.delete(pago);
        log.info("Pago eliminado físicamente de la base de datos: ID {}", id);
    }


    public Pago obtenerEntidadPorId(Long idPago) {
        return pagoRepository.findByIdPago(idPago)
                .orElseThrow(() -> new PagoNoEncontradoException(idPago));
    }
	public List<Pago> obtenerTodos() {
    return pagoRepository.findAll();
}

@Transactional
public void confirmarPago(Long idReserva) {
    Pago pagoExistente = pagoRepository.findByIdReserva(idReserva)
            .orElseThrow(() -> new PagoNoEncontradoException("No se encontró el pago para la reserva ID: " + idReserva));

    pagoExistente.setEstadoPago(EstadoPago.PAGADO);
    pagoExistente.setMontoPagado(pagoExistente.getMontoTotal());
    pagoExistente.setFechaPago(LocalDateTime.now());

    pagoRepository.save(pagoExistente);
    log.info("Pago confirmado para la Reserva: {}", idReserva);
}

	public List<Pago> obtenerPorInquilino(Long idInquilino) {
    return pagoRepository.findByIdInquilino(idInquilino);
	}


}

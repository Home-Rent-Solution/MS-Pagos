package com.HomeRentSolution.MS_Pagos.service;


import com.HomeRentSolution.MS_Pagos.assemblers.PagoAssembler;
import com.HomeRentSolution.MS_Pagos.dto.PagoCancelacionEvento;
import com.HomeRentSolution.MS_Pagos.dto.PagoCreacionEvento;
import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import com.HomeRentSolution.MS_Pagos.model.EstadoPago;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import com.HomeRentSolution.MS_Pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PagoAssembler pagoAssembler;


    // crearPago sigue recibiendo ReservaDTO porque lo llama ms-reservas
    public PagoResponseDTO crearPago(ReservaDTO request) {
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


        rabbitTemplate.convertAndSend("pagos.exchange", "", evento);
        System.out.println("Mensaje enviado al exchange de pagos para la Reserva: " + request.getIdReserva());

        PagoResponseDTO response = new PagoResponseDTO();
        response.setIdPago(nuevoPago.getIdPago()); // Asumiendo que tu entidad Pago genera un ID
        response.setEstadoPago(EstadoPago.valueOf(nuevoPago.getEstadoPago().toString()));

        return response;
    }

    public void cancelarPago (ReservaDTO request){
        Pago pagoExistente = pagoRepository.findByIdReserva(request.getIdReserva())
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró el pago para la reserva: " + request.getIdReserva()));
        pagoExistente.setEstadoPago(EstadoPago.CANCELADO);
        pagoRepository.save(pagoExistente);

        // Tu forma de "responderle" al sistema es este evento, no un return.
        PagoCancelacionEvento evento = new PagoCancelacionEvento(
                request.getIdReserva(),
                pagoExistente.getIdPropiedad(),
                pagoExistente.getIdInquilino(),
                pagoExistente.getMontoReembolso());

        rabbitTemplate.convertAndSend("pagos.cancelados.exchange", "", evento);
    }


    // Recibo de un pago específico
    public PagoResponseDTO obtenerRecibo(Long idPago) {
        Pago pago = pagoRepository.findByIdPago(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return pagoAssembler.toModel(pago);
    }

    // Todos los pagos de un inquilino
    public List<PagoResponseDTO> obtenerCuentaPorInquilino(Long idInquilino) {
        return pagoRepository.findByIdInquilino(idInquilino)
                .stream()
                .map(pago -> pagoAssembler.toModel(pago))
                .toList();
    }

    // Todos los pagos (vista admin)
    public List<PagoResponseDTO> obtenerTodos() {
        return pagoRepository.findAll()
                .stream()
                .map(pago -> pagoAssembler.toModel(pago))
                .toList();
    }

    // Detalle de un pago (vista admin)
    public PagoResponseDTO obtenerDetallePorAdmin(Long idPago) {
        Pago pago = pagoRepository.findByIdPago(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return pagoAssembler.toModel(pago);
    }

}

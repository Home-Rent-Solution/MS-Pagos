package com.HomeRentSolution.MS_Pagos.service;

import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import com.HomeRentSolution.MS_Pagos.model.EstadoPago;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import com.HomeRentSolution.MS_Pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoService {

        private final PagoRepository pagoRepository;

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

        return pagoRepository.save(nuevoPago);
    }


    // Recibo de un pago específico
    public PagoResponseDTO obtenerRecibo(Long idPago) {
        Pago pago = pagoRepository.findByIdPago(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return mapearAResponse(pago);
    }

    // Todos los pagos de un inquilino
    public List<PagoResponseDTO> obtenerCuentaPorInquilino(Long idInquilino) {
        return pagoRepository.findByIdInquilino(idInquilino)
                .stream()
                .map(this::mapearAResponse)
                .toList();
    }

    // Todos los pagos (vista admin)
    public List<PagoResponseDTO> obtenerTodos() {
        return pagoRepository.findAll()
                .stream()
                .map(this::mapearAResponse)
                .toList();
    }

    // Detalle de un pago (vista admin)
    public PagoResponseDTO obtenerDetallePorAdmin(Long idPago) {
        Pago pago = pagoRepository.findByIdPago(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return mapearAResponse(pago);
    }

}

package com.HomeRentSolution.MS_Pagos.assemblers;

import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.model.EstadoPago;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PagoAssemblerTest {

    private final PagoAssembler assembler = new PagoAssembler();

    @Test
    void convierteEntidadEnDtoConEnlaceSelf() {
        LocalDateTime fechaPago = LocalDateTime.now();
        LocalDateTime vencimiento = fechaPago.plusDays(3);
        Pago pago = new Pago(
                1L,
                2L,
                3L,
                4L,
                new BigDecimal("150000"),
                new BigDecimal("50000"),
                BigDecimal.ZERO,
                EstadoPago.PENDIENTE,
                fechaPago,
                vencimiento
        );

        PagoResponseDTO dto = assembler.toModel(pago);

        assertAll(
                () -> assertEquals(1L, dto.getIdPago()),
                () -> assertEquals(2L, dto.getIdReserva()),
                () -> assertEquals(3L, dto.getIdPropiedad()),
                () -> assertEquals(4L, dto.getIdInquilino()),
                () -> assertEquals(new BigDecimal("150000"), dto.getMontoTotal()),
                () -> assertEquals(new BigDecimal("50000"), dto.getMontoPagado()),
                () -> assertEquals(fechaPago, dto.getFechaPago()),
                () -> assertEquals(vencimiento, dto.getFechaVencimiento()),
                () -> assertEquals(EstadoPago.PENDIENTE, dto.getEstadoPago()),
                () -> assertTrue(dto.getLink("self").isPresent())
        );
    }
}

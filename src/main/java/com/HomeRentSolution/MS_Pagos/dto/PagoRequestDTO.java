package com.HomeRentSolution.MS_Pagos.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagoRequestDTO {

    private Long idReserva;
    private Long idPropiedad;
    private Long idInqulino;
    private BigDecimal montoTotal;
}

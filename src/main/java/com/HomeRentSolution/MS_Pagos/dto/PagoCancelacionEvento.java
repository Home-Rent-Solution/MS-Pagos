package com.HomeRentSolution.MS_Pagos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoCancelacionEvento implements Serializable {

    private Long idReserva;
    private Long idPropiedad;
    private Long idInquilino;
    private BigDecimal montoReembolso;
}

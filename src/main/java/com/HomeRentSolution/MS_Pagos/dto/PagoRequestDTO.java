package com.HomeRentSolution.MS_Pagos.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Data
public class PagoRequestDTO extends RepresentationModel<PagoRequestDTO>{

    private Long idReserva;
    private Long idPropiedad;
    private Long idInquilino;
    private BigDecimal montoTotal;
}

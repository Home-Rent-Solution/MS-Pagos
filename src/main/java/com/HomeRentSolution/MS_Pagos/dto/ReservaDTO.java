package com.HomeRentSolution.MS_Pagos.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReservaDTO extends RepresentationModel<ReservaDTO>{
    private Long idReserva;
    private Long idPropiedad;
    private Long idInquilino;
    private BigDecimal montoTotal;
    private LocalDateTime fechaVencimiento;
}

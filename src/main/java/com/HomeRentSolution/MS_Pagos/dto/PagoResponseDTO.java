package com.HomeRentSolution.MS_Pagos.dto;

import com.HomeRentSolution.MS_Pagos.model.EstadoPago;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagoResponseDTO extends RepresentationModel<PagoResponseDTO>{

    private Long idPago;
    private Long idReserva;
    private Long idPropiedad;
    private Long idInquilino;
    private BigDecimal montoTotal;
    private BigDecimal montoPagado;
    private LocalDateTime fechaPago;
    private LocalDateTime fechaVencimiento;
    private EstadoPago estadoPago;
}

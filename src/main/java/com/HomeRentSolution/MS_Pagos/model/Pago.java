package com.HomeRentSolution.MS_Pagos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    @Column(name = "id_reserva", nullable = false)
    private Long idReserva;

    @Column(name = "id_propiedad", nullable = false)
    private Long idPropiedad;

    @Column(name = "id_inquilino", nullable = false)
    private Long idInquilino;

    @Column(nullable = false)
    private BigDecimal montoTotal;

    @Column(nullable = false)
    private BigDecimal montoPagado;

    @Column(nullable = false)
    private BigDecimal montoReembolso;

    @Enumerated(EnumType.STRING)
    private EstadoPago estadoPago;

    @Column(nullable = true)
    private LocalDateTime fechaPago;

    @Column(nullable = false)
    private LocalDateTime fechaVencimiento;
}

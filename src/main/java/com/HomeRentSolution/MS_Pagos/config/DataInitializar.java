package com.HomeRentSolution.MS_Pagos.config;

import com.HomeRentSolution.MS_Pagos.model.EstadoPago;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import com.HomeRentSolution.MS_Pagos.repository.PagoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataInitializar implements CommandLineRunner {

    private final PagoRepository pagoRepository;


    @Autowired
    public DataInitializar(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Override
    public void run(String... args) {
        if (pagoRepository.count() == 0) {
            Pago pago1 = new Pago();
            pago1.setIdReserva(1L);
            pago1.setIdPropiedad(1L);
            pago1.setIdInquilino(1L);
            pago1.setMontoTotal(new BigDecimal("500000"));
            pago1.setMontoPagado(BigDecimal.ZERO);
            pago1.setEstadoPago(EstadoPago.PENDIENTE);
            pago1.setFechaPago(null);
            pago1.setFechaVencimiento(LocalDateTime.now().plusDays(3));

            pagoRepository.save(pago1);
        }
    }
}

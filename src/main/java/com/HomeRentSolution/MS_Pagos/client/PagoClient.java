package com.HomeRentSolution.MS_Pagos.client;

import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-reservas", url = "${ms-reservas.url}")
public interface PagoClient {

    @GetMapping("/api/reservas/{id}")
    ReservaDTO obtenerReservaPorId(@PathVariable Long id);
}

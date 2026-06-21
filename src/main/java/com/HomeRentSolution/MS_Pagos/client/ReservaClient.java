package com.HomeRentSolution.MS_Pagos.client;

import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-reservas", url = "${ms.reservas.url}")
public interface ReservaClient {

    @GetMapping("/api/v1/reservas/{id}")
    ReservaDTO obtenerPorIdReserva(@PathVariable Long idReserva);
}

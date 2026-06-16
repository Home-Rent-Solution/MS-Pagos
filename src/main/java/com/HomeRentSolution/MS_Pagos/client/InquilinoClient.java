package com.HomeRentSolution.MS_Pagos.client;

import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-inquilinos", url = "${ms.inquilinos.url}")
public interface InquilinoClient {

    @GetMapping("/api/inquilinos/{id}")
    ReservaDTO obtenerPorIdInquilino(@PathVariable Long idInquilino);
}

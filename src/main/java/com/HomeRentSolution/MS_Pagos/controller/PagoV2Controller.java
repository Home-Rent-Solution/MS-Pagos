package com.HomeRentSolution.MS_Pagos.controller;

import com.HomeRentSolution.MS_Pagos.assemblers.PagoAssembler;
import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import com.HomeRentSolution.MS_Pagos.service.PagoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos V2 (HATEOAS)", description = "Endpoints de pagos que incluyen navegación por enlaces")

public class PagoV2Controller {

    private final PagoService pagoServicios;
    private final PagoAssembler assembler;

    @GetMapping("/recibo/{idPago}")
    public ResponseEntity<PagoResponseDTO> obtenerPorId(@PathVariable Long idPago) {

        Pago pagoEntidad = pagoServicios.obtenerEntidadPorId(idPago);


        PagoResponseDTO response = assembler.toModel(pagoEntidad);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<PagoResponseDTO>> obtenerTodosConEnlaces() {

        List<Pago> pagosEntidades = pagoServicios.obtenerTodasLasEntidades();

        CollectionModel<PagoResponseDTO> responseCollection = assembler.toCollectionModel(pagosEntidades);

        return ResponseEntity.ok(responseCollection);
    }
}

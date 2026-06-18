package com.HomeRentSolution.MS_Pagos.controller;

import com.HomeRentSolution.MS_Pagos.assemblers.PagoAssembler;
import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import com.HomeRentSolution.MS_Pagos.service.PagoService;
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
public class PagoV2Controller {

    private final PagoService pagoService;
    private final PagoAssembler pagoAssembler;


    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> obtenerRecibo(@PathVariable Long id) {
        Pago pago = pagoService.obtenerPorId(id);
        return ResponseEntity.ok(pagoAssembler.toModel(pago));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<PagoResponseDTO>> obtenerTodosLosPagosConEnlaces() {
        List<Pago> pagos = pagoService.obtenerTodos();
        CollectionModel<PagoResponseDTO> collectionModel = pagoAssembler.toCollectionModel(pagos);
        return ResponseEntity.ok(collectionModel);
    }
}

package com.HomeRentSolution.MS_Pagos.controller;

import com.HomeRentSolution.MS_Pagos.assemblers.PagoAssembler;
import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import com.HomeRentSolution.MS_Pagos.service.PagoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos V1", description = "API de consulta y gestión de pagos")
public class PagoController {

    private final PagoService pagoServicios;
    private final PagoAssembler assembler;

    @PostMapping
    public ResponseEntity<Void> crearPago(@RequestBody ReservaDTO reservaDTO) {
        pagoServicios.crearPago(reservaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/recibo/{idPago}")
    public ResponseEntity<PagoResponseDTO> obtenerPorId(@PathVariable Long idPago) {
        Pago pago = pagoServicios.obtenerEntidadPorId(idPago);

        // Mapeo plano manual (Sin links HATEOAS)
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setEstadoPago(pago.getEstadoPago());
        // ... setear los demás campos ...

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/cuenta/inquilino/{idInquilino}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerCuentaPorInquilino(@PathVariable Long idInquilino) {
        List<Pago> pagos = pagoServicios.obtenerPorInquilino(idInquilino);
        List<PagoResponseDTO> dtos = pagos.stream()
                .map(pago -> {
                    PagoResponseDTO dto = new PagoResponseDTO();
                    dto.setIdPago(pago.getIdPago());
                    dto.setEstadoPago(pago.getEstadoPago());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> buscarTodosLosPagos() {
        List<Pago> pagos = pagoServicios.obtenerTodos();
        List<PagoResponseDTO> dtos = pagos.stream()
                .map(assembler::toModel)  //
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoServicios.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

}

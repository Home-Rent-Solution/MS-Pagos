package com.HomeRentSolution.MS_Pagos.controller;

import com.HomeRentSolution.MS_Pagos.assemblers.PagoAssembler;
import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import com.HomeRentSolution.MS_Pagos.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
@Tag(name = "")
public class PagoController {

    private final PagoService pagoServicios;

    @Autowired
    private PagoAssembler assembler;


    @PostMapping
    public ResponseEntity<Void> crearPago(@RequestBody ReservaDTO reservaDTO) {
        pagoServicios.crearPago(reservaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/recibo/{idPago}")
    public ResponseEntity<PagoResponseDTO> obtenerPorId(@PathVariable Long idPago) {
        PagoResponseDTO dto = pagoServicios.obtenerRecibo(idPago);

        PagoResponseDTO response = assembler.agregarLinks(dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cuenta/inquilino/{idInquilino}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerCuentaPorInquilino(
            @PathVariable Long idInquilino) {
        List<PagoResponseDTO> cuenta = pagoServicios.obtenerCuentaPorInquilino(idInquilino);
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<PagoResponseDTO>> obtenerTodos() {
        List<PagoResponseDTO> pagos = pagoServicios.obtenerTodos();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/admin/{idPago}")
    public ResponseEntity<PagoResponseDTO> obtenerDetallePorAdmin(
            @PathVariable Long idPago) {
        PagoResponseDTO detalle = pagoServicios.obtenerDetallePorAdmin(idPago);
        return ResponseEntity.ok(detalle);
    }

    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> buscarTodosLosPagos() {
        return ResponseEntity.ok(pagoServicios.obtenerTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoServicios.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

}

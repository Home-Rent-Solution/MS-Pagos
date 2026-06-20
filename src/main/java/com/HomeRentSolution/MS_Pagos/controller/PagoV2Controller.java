package com.HomeRentSolution.MS_Pagos.controller;

import com.HomeRentSolution.MS_Pagos.assemblers.PagoAssembler;
import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import com.HomeRentSolution.MS_Pagos.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Obtener recibo HATEOAS", description = "Retorna el pago con enlaces de navegación relacionados.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Pago HATEOAS", content = @Content(schema = @Schema(implementation = PagoResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Pago no encontrado")})
    public ResponseEntity<PagoResponseDTO> obtenerPorId(
            @Parameter(description = "Identificador del pago", example = "1", required = true) @PathVariable Long idPago) {

        Pago pagoEntidad = pagoServicios.obtenerEntidadPorId(idPago);


        PagoResponseDTO response = assembler.toModel(pagoEntidad);

        return ResponseEntity.ok(response);
    }
    @GetMapping
    @Operation(summary = "Listar pagos HATEOAS", description = "Retorna la colección completa con enlaces de navegación.")
    @ApiResponse(responseCode = "200", description = "Colección HATEOAS")
    public ResponseEntity<CollectionModel<PagoResponseDTO>> obtenerTodosConEnlaces() {
        List<Pago> pagos = pagoServicios.obtenerTodos();
        return ResponseEntity.ok(assembler.toCollectionModel(pagos));
    }
}

package com.HomeRentSolution.MS_Pagos.controller;

import com.HomeRentSolution.MS_Pagos.assemblers.PagoAssembler;
import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import com.HomeRentSolution.MS_Pagos.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pagos V1", description = "API de consulta y gestión de pagos")
public class PagoController {

    private final PagoService pagoServicios;
    private final PagoAssembler assembler;


    @Autowired
    public PagoController(PagoService pagoServicios, PagoAssembler assembler) {
        this.pagoServicios = pagoServicios;
        this.assembler = assembler;
    }

    @PostMapping
    @Operation(summary = "Crear pago", description = "Crea un pago pendiente a partir de los datos de una reserva y publica el evento correspondiente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pago creado"),
            @ApiResponse(responseCode = "400", description = "Datos de reserva inválidos")
    })
    public ResponseEntity<Void> crearPago(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    description = "Datos económicos de la reserva",
                    content = @Content(schema = @Schema(implementation = ReservaDTO.class),
                            examples = @ExampleObject(value = "{\"idReserva\":25,\"idPropiedad\":10,\"idInquilino\":7,\"montoTotal\":250000}")))
            @RequestBody ReservaDTO reservaDTO) {
        pagoServicios.crearPago(reservaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/recibo/{idPago}")
    @Operation(summary = "Obtener recibo por ID de pago")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado",
                    content = @Content(schema = @Schema(implementation = PagoResponseDTO.class),
                            examples = @ExampleObject(value = "{\"idPago\":1,\"idReserva\":25,\"montoTotal\":250000,\"montoPagado\":0,\"estadoPago\":\"PENDIENTE\"}"))),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<PagoResponseDTO> obtenerPorId(
            @Parameter(description = "Identificador del pago", example = "1", required = true)
            @PathVariable Long idPago) {
        Pago pago = pagoServicios.obtenerEntidadPorId(idPago);

        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setIdReserva(pago.getIdReserva());
        dto.setIdPropiedad(pago.getIdPropiedad());
        dto.setIdInquilino(pago.getIdInquilino());
        dto.setMontoTotal(pago.getMontoTotal());
        dto.setMontoPagado(pago.getMontoPagado());
        dto.setFechaPago(pago.getFechaPago());
        dto.setFechaVencimiento(pago.getFechaVencimiento());
        dto.setEstadoPago(pago.getEstadoPago());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/cuenta/inquilino/{idInquilino}")
    @Operation(summary = "Listar pagos de un inquilino")
    @ApiResponse(responseCode = "200", description = "Cuenta del inquilino consultada")
    public ResponseEntity<List<PagoResponseDTO>> obtenerCuentaPorInquilino(
            @Parameter(description = "Identificador del inquilino", example = "7", required = true)
            @PathVariable Long idInquilino) {
        List<Pago> pagos = pagoServicios.obtenerPorInquilino(idInquilino);
        List<PagoResponseDTO> dtos = pagos.stream()
                .map(pago -> {
                    PagoResponseDTO dto = new PagoResponseDTO();
                    dto.setIdPago(pago.getIdPago());
                    dto.setIdReserva(pago.getIdReserva());
                    dto.setIdPropiedad(pago.getIdPropiedad());
                    dto.setIdInquilino(pago.getIdInquilino());
                    dto.setMontoTotal(pago.getMontoTotal());
                    dto.setMontoPagado(pago.getMontoPagado());
                    dto.setFechaPago(pago.getFechaPago());
                    dto.setFechaVencimiento(pago.getFechaVencimiento());
                    dto.setEstadoPago(pago.getEstadoPago());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    @Operation(summary = "Listar todos los pagos")
    @ApiResponse(responseCode = "200", description = "Listado completo de pagos")
    public ResponseEntity<List<PagoResponseDTO>> buscarTodosLosPagos() {
        List<Pago> pagos = pagoServicios.obtenerTodos();
        List<PagoResponseDTO> dtos = pagos.stream()
                .map(assembler::toModel)  //
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pago")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pago eliminado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Void> eliminarPago(
            @Parameter(description = "Identificador del pago", example = "1", required = true)
            @PathVariable Long id) {
        pagoServicios.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

	@PutMapping("/{idReserva}/confirmar")
	@Operation(summary = "Confirmar pago por ID de reserva")
	 @ApiResponse(responseCode = "204", description = "Pago confirmado correctamente")
public ResponseEntity<Void> confirmarPago(
        @Parameter(description = "Identificador de la reserva", example = "25", required = true)
        @PathVariable Long idReserva) {
    pagoServicios.confirmarPago(idReserva);
    return ResponseEntity.noContent().build();
	}	
}

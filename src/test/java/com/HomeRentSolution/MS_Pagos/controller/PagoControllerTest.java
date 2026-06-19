package com.HomeRentSolution.MS_Pagos.controller;

import com.HomeRentSolution.MS_Pagos.PagoService;
import com.HomeRentSolution.MS_Pagos.assemblers.PagoAssembler;
import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import com.HomeRentSolution.MS_Pagos.model.EstadoPago;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagoControllerTest {

    @Mock
    private PagoService pagoServicios;

    @Mock
    private PagoAssembler assembler;

    @InjectMocks
    private PagoController pagoController;

    private Pago pago;
    private PagoResponseDTO pagoResponseDTO;
    private ReservaDTO reservaDTO;

    @BeforeEach
    void setUp() {
        pago = new Pago();
        pago.setIdPago(1L);
        pago.setIdReserva(1L);
        pago.setIdPropiedad(2L);
        pago.setIdInquilino(3L);
        pago.setMontoTotal(new BigDecimal("100000"));
        pago.setMontoPagado(BigDecimal.ZERO);
        pago.setMontoReembolso(BigDecimal.ZERO);
        pago.setEstadoPago(EstadoPago.PENDIENTE);
        pago.setFechaVencimiento(LocalDateTime.now().plusDays(3));

        pagoResponseDTO = new PagoResponseDTO();
        pagoResponseDTO.setIdPago(1L);
        pagoResponseDTO.setIdReserva(1L);
        pagoResponseDTO.setIdPropiedad(2L);
        pagoResponseDTO.setIdInquilino(3L);
        pagoResponseDTO.setMontoTotal(new BigDecimal("100000"));
        pagoResponseDTO.setMontoPagado(BigDecimal.ZERO);
        pagoResponseDTO.setEstadoPago(EstadoPago.PENDIENTE);
        pagoResponseDTO.setFechaVencimiento(LocalDateTime.now().plusDays(3));

        reservaDTO = new ReservaDTO();
        reservaDTO.setIdReserva(1L);
        reservaDTO.setIdPropiedad(2L);
        reservaDTO.setIdInquilino(3L);
        reservaDTO.setMontoTotal(new BigDecimal("100000"));
    }

    // PRUEBA 1: crear pago devuelve 201 CREATED
    @Test
    void crearPago_debeRetornar201() {
        when(pagoServicios.crearPago(any(ReservaDTO.class))).thenReturn(pagoResponseDTO);

        ResponseEntity<Void> respuesta = pagoController.crearPago(reservaDTO);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        verify(pagoServicios, times(1)).crearPago(any(ReservaDTO.class));
    }

    // PRUEBA 2: obtener pago por ID devuelve el DTO completo
    @Test
    void obtenerPorId_debeRetornarPagoCompleto() {
        when(pagoServicios.obtenerEntidadPorId(1L)).thenReturn(pago);

        ResponseEntity<PagoResponseDTO> respuesta = pagoController.obtenerPorId(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(1L, respuesta.getBody().getIdPago());
        assertEquals(EstadoPago.PENDIENTE, respuesta.getBody().getEstadoPago());
    }

    // PRUEBA 3: obtener pagos por inquilino devuelve lista
    @Test
    void obtenerCuentaPorInquilino_debeRetornarLista() {
        when(pagoServicios.obtenerPorInquilino(3L)).thenReturn(List.of(pago));

        ResponseEntity<List<PagoResponseDTO>> respuesta = pagoController.obtenerCuentaPorInquilino(3L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertFalse(respuesta.getBody().isEmpty());
        assertEquals(1, respuesta.getBody().size());
    }

    // PRUEBA 4: obtener todos los pagos devuelve lista con HATEOAS
    @Test
    void buscarTodosLosPagos_debeRetornarListaConEnlaces() {
        when(pagoServicios.obtenerTodos()).thenReturn(List.of(pago));
        when(assembler.toModel(pago)).thenReturn(pagoResponseDTO);

        ResponseEntity<List<PagoResponseDTO>> respuesta = pagoController.buscarTodosLosPagos();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertFalse(respuesta.getBody().isEmpty());
        verify(assembler, times(1)).toModel(pago);
    }

    // PRUEBA 5: eliminar pago devuelve 204 NO CONTENT
    @Test
    void eliminarPago_debeRetornar204() {
        doNothing().when(pagoServicios).eliminarPago(1L);

        ResponseEntity<Void> respuesta = pagoController.eliminarPago(1L);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(pagoServicios, times(1)).eliminarPago(1L);
    }
}

package com.HomeRentSolution.MS_Pagos.exception;

import com.HomeRentSolution.MS_Pagos.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GobalExceptionHandlerTest {

    private final GobalExceptionHandler handler = new GobalExceptionHandler();

    @Test
    void pagoNoEncontradoRetorna404() {
        ResponseEntity<ErrorResponse> response =
                handler.handlePagoNotFound(new PagoNoEncontradoException(7L));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Pago no encontrado con ID: 7", response.getBody().getMensaje());
    }

    @Test
    void validacionRetorna400ConDetalleDeCampos() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("pago", "monto", "debe ser positivo"),
                new FieldError("pago", "reserva", "es obligatoria")
        ));

        ResponseEntity<ErrorResponse> response = handler.handleValidacion(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMensaje().contains("monto: debe ser positivo"));
        assertTrue(response.getBody().getMensaje().contains("reserva: es obligatoria"));
    }

    @Test
    void argumentoInvalidoRetorna400() {
        ResponseEntity<ErrorResponse> response =
                handler.handleIllegalArgument(new IllegalArgumentException("monto inválido"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("monto inválido", response.getBody().getMensaje());
    }

    @Test
    void excepcionGeneralRetorna500SinExponerDetalle() {
        ResponseEntity<ErrorResponse> response =
                handler.handleGeneral(new Exception("detalle interno"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertFalse(response.getBody().getMensaje().contains("detalle interno"));
    }
}

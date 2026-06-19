package com.HomeRentSolution.MS_Pagos.service;

import com.HomeRentSolution.MS_Pagos.PagoService;
import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import com.HomeRentSolution.MS_Pagos.exception.PagoNoEncontradoException;
import com.HomeRentSolution.MS_Pagos.model.EstadoPago;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import com.HomeRentSolution.MS_Pagos.repository.PagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {

    // @Mock crea objetos falsos — no usan base de datos real
    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    // @InjectMocks inyecta los mocks anteriores en el servicio
    @InjectMocks
    private PagoService pagoService;

    private ReservaDTO reservaDTO;
    private Pago pago;

    // @BeforeEach se ejecuta antes de cada prueba
    // Aquí preparamos los datos de prueba
    @BeforeEach
    void setUp() {
        reservaDTO = new ReservaDTO();
        reservaDTO.setIdReserva(1L);
        reservaDTO.setIdPropiedad(2L);
        reservaDTO.setIdInquilino(3L);
        reservaDTO.setMontoTotal(new BigDecimal("100000"));
        reservaDTO.setFechaVencimiento(LocalDateTime.now().plusDays(3));

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
    }

    // PRUEBA 1: crear un pago correctamente
    @Test
    void crearPago_debeRetornarPagoConEstadoPendiente() {
        // Simulamos que el repositorio guarda y devuelve el pago
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        PagoResponseDTO resultado = pagoService.crearPago(reservaDTO);

        // Verificamos que el estado sea PENDIENTE
        assertEquals(EstadoPago.PENDIENTE, resultado.getEstadoPago());

        // Verificamos que se llamó al repositorio 1 vez
        verify(pagoRepository, times(1)).save(any(Pago.class));

        // Verificamos que se envió mensaje a RabbitMQ
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(Object.class));
    }

    // PRUEBA 2: obtener un pago por ID que existe
    @Test
    void obtenerEntidadPorId_debeRetornarPago_cuandoExiste() {
        when(pagoRepository.findByIdPago(1L)).thenReturn(Optional.of(pago));

        Pago resultado = pagoService.obtenerEntidadPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdPago());
    }

    // PRUEBA 3: obtener un pago por ID que NO existe — debe lanzar excepción
    @Test
    void obtenerEntidadPorId_debeLanzarExcepcion_cuandoNoExiste() {
        when(pagoRepository.findByIdPago(99L)).thenReturn(Optional.empty());

        assertThrows(PagoNoEncontradoException.class, () -> {
            pagoService.obtenerEntidadPorId(99L);
        });
    }

    // PRUEBA 4: obtener todos los pagos
    @Test
    void obtenerTodos_debeRetornarListaDePagos() {
        when(pagoRepository.findAll()).thenReturn(List.of(pago));

        List<Pago> resultado = pagoService.obtenerTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    // PRUEBA 5: eliminar un pago que existe
    @Test
    void eliminarPago_debeEliminarCorrectamente_cuandoExiste() {
        when(pagoRepository.findByIdPago(1L)).thenReturn(Optional.of(pago));

        pagoService.eliminarPago(1L);

        // Verificamos que se llamó al delete
        verify(pagoRepository, times(1)).delete(pago);
    }

    // PRUEBA 6: eliminar un pago que NO existe — debe lanzar excepción
    @Test
    void eliminarPago_debeLanzarExcepcion_cuandoNoExiste() {
        when(pagoRepository.findByIdPago(99L)).thenReturn(Optional.empty());

        assertThrows(PagoNoEncontradoException.class, () -> {
            pagoService.eliminarPago(99L);
        });
    }
}

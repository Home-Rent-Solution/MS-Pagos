package com.HomeRentSolution.MS_Pagos.controller;

import com.HomeRentSolution.MS_Pagos.assemblers.PagoAssembler;
import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import com.HomeRentSolution.MS_Pagos.service.PagoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoV2ControllerTest {

    @Mock
    private PagoService pagoService;

    @Mock
    private PagoAssembler assembler;

    @InjectMocks
    private PagoV2Controller controller;

    @Test
    void obtenerPorIdRetornaPagoConEnlaces() {
        Pago pago = new Pago();
        PagoResponseDTO dto = new PagoResponseDTO();
        when(pagoService.obtenerEntidadPorId(1L)).thenReturn(pago);
        when(assembler.toModel(pago)).thenReturn(dto);

        ResponseEntity<PagoResponseDTO> response = controller.obtenerPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(dto, response.getBody());
    }

    @Test
    void obtenerTodosRetornaColeccionConEnlaces() {
        List<Pago> pagos = List.of(new Pago());
        CollectionModel<PagoResponseDTO> model = CollectionModel.empty();
        when(pagoService.obtenerTodos()).thenReturn(pagos);
        when(assembler.toCollectionModel(pagos)).thenReturn(model);

        ResponseEntity<CollectionModel<PagoResponseDTO>> response =
                controller.obtenerTodosConEnlaces();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(model, response.getBody());
    }
}

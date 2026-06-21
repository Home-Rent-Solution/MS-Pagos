package com.HomeRentSolution.MS_Pagos.listeners;

import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import com.HomeRentSolution.MS_Pagos.service.PagoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PagoListenersTest {

    @Mock
    private PagoService pagoService;

    @InjectMocks
    private PagoListeners listeners;

    @Test
    void procesaEventoDeReservaCreada() {
        ReservaDTO evento = new ReservaDTO();
        evento.setIdReserva(10L);

        listeners.recibirNuevaReserva(evento);

        verify(pagoService).crearPago(evento);
    }

    @Test
    void procesaEventoDeReservaCancelada() {
        ReservaDTO evento = new ReservaDTO();
        evento.setIdReserva(10L);

        listeners.recibirReservaCancelada(evento);

        verify(pagoService).eliminarPago(10L);
    }
}

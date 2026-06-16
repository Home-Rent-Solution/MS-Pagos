package com.HomeRentSolution.MS_Pagos.listeners;

import com.HomeRentSolution.MS_Pagos.dto.PagoCancelacionEvento;
import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import com.HomeRentSolution.MS_Pagos.service.PagoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PagoListeners {

    @Autowired
    private PagoService pagoService;

    // 1. Escucha la cola de creación de reservas
    @RabbitListener(queues = "mensajeria.reserva.creada.queue")
    public void recibirNuevaReserva(ReservaDTO creacion) {
        System.out.println("Listener de Pagos: Llegó orden de creacion de pago " + creacion.getIdReserva());
        pagoService.crearPago(creacion);
    }

    // 2. Escucha la cola de cancelación de reservas
    @RabbitListener(queues = "mensajeria.reserva.cancelada.queue")
    public void recibirReservaCancelada(ReservaDTO evento) {

        System.out.println("Listener de Pagos: Llegó orden de cancelación de pago " + evento.getIdReserva());
        pagoService.cancelarPago(evento);
    }


}

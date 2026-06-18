package com.HomeRentSolution.MS_Pagos.listeners;

import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import com.HomeRentSolution.MS_Pagos.service.PagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PagoListeners {


    private final PagoService pagoService;

    @RabbitListener(queues = "pagos.reserva-creada.queue")
    public void recibirNuevaReserva(ReservaDTO creacion) {
        log.info("[RabbitMQ] MS-Pagos recibió orden de creación para la Reserva ID: {}", creacion.getIdReserva());
        pagoService.crearPago(creacion);
    }

    @RabbitListener(queues = "pagos.reserva-cancelada.queue")
    public void recibirReservaCancelada(ReservaDTO evento) {
        log.info("[RabbitMQ] MS-Pagos recibió orden de cancelación para la Reserva ID: {}", evento.getIdReserva());
        pagoService.eliminarPago(evento.getIdReserva());
    }


}

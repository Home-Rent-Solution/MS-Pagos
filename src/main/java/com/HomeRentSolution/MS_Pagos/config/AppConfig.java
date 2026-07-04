package com.HomeRentSolution.MS_Pagos.config;

import feign.Request;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(5000, 10000);
    }

    public static final String PAGOS_EXCHANGE = "pagos.exchange";
    public static final String RESERVAS_EXCHANGE = "reservas.exchange"; // NUEVO: exchange del publicador
    public static final String RESERVAS_QUEUE = "pagos.reserva-creada.queue";
    public static final String QUEUE_RESERVA_CANCELADA = "pagos.reserva-cancelada.queue";
    public static final String ROUTING_CREADO = "pago.creado";
    public static final String ROUTING_ELIMINADO = "pago.eliminado";

    @Bean
    public TopicExchange pagosExchange() {
        return new TopicExchange(PAGOS_EXCHANGE);
    }

    @Bean
    public TopicExchange reservasExchange() { // declaramos el exchange de Reserva para poder enlazarnos
        return new TopicExchange(RESERVAS_EXCHANGE);
    }

    @Bean
    public Queue reservasQueue() {
        return new Queue(RESERVAS_QUEUE, true);
    }

    @Bean
    public Queue reservaCanceladaQueue() {
        return new Queue(QUEUE_RESERVA_CANCELADA, true);
    }

    @Bean
    public Binding bindingReservaCreada(Queue reservasQueue, TopicExchange reservasExchange) {
        return BindingBuilder.bind(reservasQueue).to(reservasExchange).with("reserva.creada");
    }

    @Bean
    public Binding bindingReservaCancelada(Queue reservaCanceladaQueue, TopicExchange reservasExchange) {
        return BindingBuilder.bind(reservaCanceladaQueue).to(reservasExchange).with("reserva.cancelada");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

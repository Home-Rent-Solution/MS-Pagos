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
    public static final String RESERVAS_QUEUE = "pagos.reserva-creada.queue";
    public static final String QUEUE_RESERVA_CANCELADA = "pagos.reserva-cancelada.queue";


    public static final String ROUTING_CREADO = "pago.creado";
    public static final String ROUTING_ELIMINADO = "pago.eliminado";

    @Bean
    public TopicExchange pagosExchange() {
        return new TopicExchange(PAGOS_EXCHANGE);
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
    public Binding bindingReservas(Queue reservasQueue, TopicExchange pagosExchange) {
        return BindingBuilder.bind(reservasQueue).to(pagosExchange).with("pago.*");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}

package com.curso.payment.infra.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "rideCompleted";

    public static final String QUEUE_PROCESS_PAYMENT = "rideCompleted.processPayment";
    public static final String QUEUE_GENERATE_INVOICE = "rideCompleted.generateInvoice";
    public static final String QUEUE_SEND_RECEIPT = "rideCompleted.sendReceipt";

    @Bean
    public DirectExchange rideCompletedExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue processPaymentQueue() {
        return new Queue(QUEUE_PROCESS_PAYMENT, true);
    }

    @Bean
    public Queue generateInvoiceQueue() {
        return new Queue(QUEUE_GENERATE_INVOICE, true);
    }

    @Bean
    public Queue sendReceiptQueue() {
        return new Queue(QUEUE_SEND_RECEIPT, true);
    }

    @Bean
    public Binding bindProcessPayment() {
        return BindingBuilder.bind(processPaymentQueue()).to(rideCompletedExchange()).with("");
    }

    @Bean
    public Binding bindGenerateInvoice() {
        return BindingBuilder.bind(generateInvoiceQueue()).to(rideCompletedExchange()).with("");
    }

    @Bean
    public Binding bindSendReceipt() {
        return BindingBuilder.bind(sendReceiptQueue()).to(rideCompletedExchange()).with("");
    }

}

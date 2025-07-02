package com.curso.payment.infra.controller;

import com.curso.payment.application.usecase.ProcessPayment;
import com.curso.payment.infra.queue.QueueListenerRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class QueueController {

    private final QueueListenerRegistry queueListenerRegistry;
    private final ProcessPayment processPayment;

    public QueueController(QueueListenerRegistry queueListenerRegistry, ProcessPayment processPayment) {
        this.queueListenerRegistry = queueListenerRegistry;
        this.processPayment = processPayment;
    }

    @PostConstruct
    public void init() {
        queueListenerRegistry.register("rideCompleted.processPayment", processPayment::execute);
    }

}

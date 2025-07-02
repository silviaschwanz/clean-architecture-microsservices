package com.curso.payment.infra.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class QueueListenerRegistry {

    private static final Map<String, QueueCallback<?>> callbacks = new ConcurrentHashMap<>();
    private static final Map<String, Class<?>> queueToClass = Map.of(
            "rideCompleted.processPayment", com.curso.payment.application.dto.ProcessPaymentInput.class
            // adicione outros mapeamentos fila -> DTO aqui se precisar
    );

    private final ObjectMapper objectMapper;

    public QueueListenerRegistry(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> void register(String queue, QueueCallback<T> callback) {
        callbacks.put(queue, callback);
    }

    @RabbitListener(queues = {
            "rideCompleted.processPayment",
            "rideCompleted.generateInvoice",
            "rideCompleted.sendReceipt"
    })
    public void onMessage(String message, Message rawMessage) {
        String queue = rawMessage.getMessageProperties().getConsumerQueue();
        QueueCallback<?> callback = callbacks.get(queue);
        Class<?> clazz = queueToClass.get(queue);

        if (callback == null || clazz == null) {
            System.err.println("Callback ou classe DTO não configurados para a fila: " + queue);
            return;
        }

        try {
            Object data = objectMapper.readValue(message, clazz);
            // Cast seguro porque registramos com o tipo correto
            @SuppressWarnings("unchecked")
            QueueCallback<Object> typedCallback = (QueueCallback<Object>) callback;
            typedCallback.handle(data);
        } catch (Exception e) {
            System.err.println("Erro ao desserializar mensagem da fila " + queue + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

}

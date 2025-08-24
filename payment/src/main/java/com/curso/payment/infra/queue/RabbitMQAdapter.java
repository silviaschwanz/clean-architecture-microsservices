package com.curso.payment.infra.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQAdapter implements Queue{

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final QueueListenerRegistry queueListenerRegistry;

    public RabbitMQAdapter(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, QueueListenerRegistry queueListenerRegistry) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.queueListenerRegistry = queueListenerRegistry;
    }

    @Override
    public void connect() {
        // Não precisa fazer nada, Spring gerencia conexão
    }

    @Override
    public void publish(String exchange, Object data) {
        try {
            String json = objectMapper.writeValueAsString(data);
            rabbitTemplate.convertAndSend(exchange, "", json);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao publicar mensagem", e);
        }
    }

    @Override
    public <T> void consume(String queue, QueueCallback<T> callback) {
        queueListenerRegistry.register(queue, callback);
    }

}

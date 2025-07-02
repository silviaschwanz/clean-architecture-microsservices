package com.curso.payment.infra.queue;

public interface Queue {

    void connect();
    void publish(String exchange, Object data);
    <T> void consume(String queue, QueueCallback<T> callback);

}

package com.curso.payment.infra.queue;

@FunctionalInterface
public interface QueueCallback<T> {

    void handle(T data);

}

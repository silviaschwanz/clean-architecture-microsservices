package com.curso.payment.application;

import com.curso.payment.application.dto.InputTransaction;
import com.curso.payment.application.dto.OutputTransaction;
import com.curso.payment.infra.fallback.PaymentMethod;

// Chain of Responsibility
public interface PaymentProcessor {

    void setNext(PaymentProcessor next);
    PaymentProcessor getNext();
    PaymentMethod getSupportedMethod();
    OutputTransaction processPayment(InputTransaction input);

}

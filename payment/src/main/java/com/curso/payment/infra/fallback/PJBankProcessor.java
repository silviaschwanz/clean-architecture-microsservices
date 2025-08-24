package com.curso.payment.infra.fallback;

import com.curso.payment.application.PaymentGateway;
import com.curso.payment.application.PaymentProcessor;
import com.curso.payment.application.dto.InputTransaction;
import com.curso.payment.application.dto.OutputTransaction;

// Chain of Responsibility
public class PJBankProcessor implements PaymentProcessor {

    private PaymentProcessor next;
    private final PaymentGateway gateway;

    public PJBankProcessor(PaymentGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void setNext(PaymentProcessor next) {
        this.next = next;
    }

    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.PJ_BANK;
    }

    @Override
    public PaymentProcessor getNext() {
        if (next == null) throw new IllegalStateException(PaymentErrorMessages.OUT_OF_PROCESSORS.format());
        return next;
    }

    @Override
    public OutputTransaction processPayment(InputTransaction input) {
        try {
            return gateway.createTransaction(input);
        } catch (Exception exception) {
            return getNext().processPayment(input);
        }
    }

}

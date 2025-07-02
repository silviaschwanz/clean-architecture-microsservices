package com.curso.payment.infra.fallback;

import com.curso.payment.application.PaymentGateway;
import com.curso.payment.application.PaymentProcessor;
import com.curso.payment.application.dto.TransactionInput;
import com.curso.payment.application.dto.TransactionOutput;

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
    public TransactionOutput processPayment(TransactionInput input) {
        if (input.type().equals(PaymentMethod.CIELO)) {
            return gateway.createTransaction(input);
        } else if (next != null) {
            return next.processPayment(input);
        } else {
            throw new IllegalStateException("Unsupported payment type: " + input.type());
        }
    }

}

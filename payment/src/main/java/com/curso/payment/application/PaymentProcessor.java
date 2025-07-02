package com.curso.payment.application;

import com.curso.payment.application.dto.TransactionInput;
import com.curso.payment.application.dto.TransactionOutput;

// Chain of Responsibility
public interface PaymentProcessor {

    void setNext(PaymentProcessor next);
    TransactionOutput processPayment(TransactionInput input);

}

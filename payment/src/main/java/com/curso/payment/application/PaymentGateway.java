package com.curso.payment.application;

import com.curso.payment.application.dto.TransactionInput;
import com.curso.payment.application.dto.TransactionOutput;

public interface PaymentGateway{

    TransactionOutput createTransaction(TransactionInput input);
}

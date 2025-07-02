package com.curso.ride.infra.gateway;

import com.curso.ride.application.dto.TransactionInput;
import com.curso.ride.application.dto.TransactionOutput;

public interface PaymentGateway {

    TransactionOutput processPayment(TransactionInput input);
}

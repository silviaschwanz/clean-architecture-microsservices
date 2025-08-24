package com.curso.payment.application;

import com.curso.payment.application.dto.InputTransaction;
import com.curso.payment.application.dto.OutputTransaction;

public interface PaymentGateway{

    OutputTransaction createTransaction(InputTransaction input);
}

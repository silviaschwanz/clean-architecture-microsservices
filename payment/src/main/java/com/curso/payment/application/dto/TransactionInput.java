package com.curso.payment.application.dto;

import com.curso.payment.infra.fallback.PaymentMethod;

public record TransactionInput(
        PaymentMethod type,
        String cardHolder,
        String creditCardNumber,
        String expDate,
        String cvv,
        double amount
) {
}

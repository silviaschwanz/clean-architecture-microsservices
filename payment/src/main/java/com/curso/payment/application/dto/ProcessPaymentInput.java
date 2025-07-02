package com.curso.payment.application.dto;

import com.curso.payment.infra.fallback.PaymentMethod;

public record ProcessPaymentInput(
    String rideId,
    PaymentMethod type,
    double amount
) {
}

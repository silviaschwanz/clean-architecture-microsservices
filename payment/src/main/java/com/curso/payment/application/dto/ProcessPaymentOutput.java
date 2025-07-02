package com.curso.payment.application.dto;

public record ProcessPaymentOutput(
        String tid,
        String authorizationCode,
        String status
) {
}

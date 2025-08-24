package com.curso.payment.application.dto;

public record OutputProcessPayment(
        String tid,
        String authorizationCode,
        String status
) {
}

package com.curso.payment.application.dto;

public record InputProcessPayment(
    String rideId,
    String type,
    double amount
) {
}

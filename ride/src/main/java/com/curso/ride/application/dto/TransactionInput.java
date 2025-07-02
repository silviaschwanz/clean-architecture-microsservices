package com.curso.ride.application.dto;

public record TransactionInput(
        PaymentMethod type,
        String cardHolder,
        String creditCardNumber,
        String expDate,
        String cvv,
        double amount
) {
}

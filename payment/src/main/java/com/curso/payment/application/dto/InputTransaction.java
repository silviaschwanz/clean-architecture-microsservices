package com.curso.payment.application.dto;

public record InputTransaction(
        String type,
        String cardHolder,
        String creditCardNumber,
        String expDate,
        String cvv,
        double amount
) {
}

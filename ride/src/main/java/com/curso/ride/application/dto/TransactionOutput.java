package com.curso.ride.application.dto;

public record TransactionOutput(
        String tid,
        String authorizationCode,
        String status
) {
}

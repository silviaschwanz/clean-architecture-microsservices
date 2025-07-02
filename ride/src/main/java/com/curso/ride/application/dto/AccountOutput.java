package com.curso.ride.application.dto;

public record AccountOutput(
        String accountId,
        String name,
        String email,
        String cpf,
        String carPlate,
        Boolean isPassenger,
        Boolean isDriver
) {
}

package com.curso.account.application.dto;

public record SignupInput(
        String name,
        String email,
        String cpf,
        String carPlate,
        boolean isPassenger,
        boolean isDriver,
        String password
) {
}

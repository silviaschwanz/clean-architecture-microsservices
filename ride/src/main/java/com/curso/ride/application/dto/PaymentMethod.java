package com.curso.ride.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentMethod {
    CIELO,
    PJ_BANK;

    @JsonCreator
    public static PaymentMethod from(String value) {
        return PaymentMethod.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase(); // Para serializar como "pix", "cielo"...
    }
}

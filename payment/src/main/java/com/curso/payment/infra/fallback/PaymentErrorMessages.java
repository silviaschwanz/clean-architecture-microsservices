package com.curso.payment.infra.fallback;

import java.text.MessageFormat;

public enum PaymentErrorMessages {

    UNSUPPORTED_PAYMENT("Unsupported payment type: {0}"),
    OUT_OF_PROCESSORS("Out of processors"),
    ;

    private final String message;

    PaymentErrorMessages(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return MessageFormat.format(message, args);
    }

}

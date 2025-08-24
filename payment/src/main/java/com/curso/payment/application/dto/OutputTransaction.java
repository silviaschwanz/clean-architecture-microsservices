package com.curso.payment.application.dto;

public record OutputTransaction(
        String tid,
        String authorizationCode,
        String status
) {

    public static OutputTransaction sucesso(String tid, String authCode) {
        return new OutputTransaction(tid, authCode, "approved");
    }
}

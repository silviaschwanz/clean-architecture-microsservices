package com.curso.payment.application.dto;

public record TransactionOutput(
        String tid,
        String authorizationCode,
        String status
) {

    public static TransactionOutput sucesso(String tid, String authCode) {
        return new TransactionOutput(tid, authCode, "approved");
    }
}

package com.curso.payment.infra.gateway;

import com.curso.payment.application.PaymentGateway;
import com.curso.payment.application.dto.TransactionInput;
import com.curso.payment.application.dto.TransactionOutput;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PJBankGateway implements PaymentGateway {

    private static final String BASE_URL = "https://sandbox.pjbank.com.br/recebimentos/";
    private static final String CREDENCIAL = "e0727263cc7a983f0aae5411ad86c5a144b8ed28";
    private static final String CHAVE = "e9db986de751de918ca19a1c377f0b7c313915f8";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TransactionOutput createTransaction(TransactionInput input) {
        System.out.println("processing pjbank");

        try {
            // Divide a data (formato: MM/YYYY)
            String[] expParts = input.expDate().split("/");
            String mes = expParts[0];
            String ano = expParts[1];

            // Etapa 1: gerar token do cartão
            ObjectNode creditCard = objectMapper.createObjectNode();
            creditCard.put("nome_cartao", input.cardHolder());
            creditCard.put("numero_cartao", input.creditCardNumber());
            creditCard.put("mes_vencimento", mes);
            creditCard.put("ano_vencimento", ano);
            creditCard.put("cpf_cartao", "64111456529");
            creditCard.put("codigo_cvv", input.cvv());
            creditCard.put("email_cartao", "api@pjbank.com.br");

            String creditCardJson = objectMapper.writeValueAsString(creditCard);

            HttpRequest request1 = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + CREDENCIAL + "/tokens"))
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .header("X-CHAVE", CHAVE)
                    .POST(HttpRequest.BodyPublishers.ofString(creditCardJson))
                    .build();

            HttpResponse<String> response1 = httpClient.send(request1, HttpResponse.BodyHandlers.ofString());
            JsonNode responseBody1 = objectMapper.readTree(response1.body());

            if (responseBody1.has("msg") && !responseBody1.get("msg").asText().isEmpty()) {
                throw new RuntimeException("Erro ao gerar token: " + responseBody1.get("msg").asText());
            }

            String tokenCartao = responseBody1.get("token_cartao").asText();

            // Etapa 2: criar transação
            ObjectNode transaction = objectMapper.createObjectNode();
            transaction.put("pedido_numero", "1");
            transaction.put("token_cartao", tokenCartao);
            transaction.put("valor", input.amount());
            transaction.put("parcelas", 1);
            transaction.put("descricao_pagamento", "");

            String transactionJson = objectMapper.writeValueAsString(transaction);

            HttpRequest request2 = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + CREDENCIAL + "/transacoes"))
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .header("X-CHAVE", CHAVE)
                    .POST(HttpRequest.BodyPublishers.ofString(transactionJson))
                    .build();

            HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
            JsonNode responseBody2 = objectMapper.readTree(response2.body());

            String status = "rejected";
            if ("1".equals(responseBody2.get("autorizada").asText())) {
                status = "approved";
            }

            return new TransactionOutput(
                    responseBody2.get("tid").asText(),
                    responseBody2.get("autorizacao").asText(),
                    status
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar transação com o PJBank", e);
        }
    }

}

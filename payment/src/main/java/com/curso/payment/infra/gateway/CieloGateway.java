package com.curso.payment.infra.gateway;

import com.curso.payment.application.PaymentGateway;
import com.curso.payment.application.dto.TransactionInput;
import com.curso.payment.application.dto.TransactionOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CieloGateway implements PaymentGateway {

    private static final String MERCHANT_ID = "10631719-a8b9-44fa-8053-1d856ca3cac7";
    private static final String MERCHANT_KEY = "TWFYUFSEXRRPQGCUQLHFHGEXWEDNOPQTXGUFDSQH";
    private static final String CIELO_URL = "https://apisandbox.cieloecommerce.cielo.com.br/1/sales/";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TransactionOutput createTransaction(TransactionInput input) {

        System.out.println("processing cielo");

        try {
            ObjectNode transaction = objectMapper.createObjectNode();
            transaction.put("MerchantOrderId", "2014111701");

            ObjectNode customer = transaction.putObject("Customer");
            customer.put("Name", "Comprador Teste");
            customer.put("Identity", "11225468954");
            customer.put("IdentityType", "CPF");
            customer.put("Email", "compradorteste@teste.com");
            customer.put("Birthdate", "1991-01-02");

            ObjectNode address = customer.putObject("Address");
            address.put("Street", "Rua Teste");
            address.put("Number", "123");
            address.put("Complement", "AP 123");
            address.put("ZipCode", "12345987");
            address.put("City", "Rio de Janeiro");
            address.put("State", "RJ");
            address.put("Country", "BRA");

            ObjectNode deliveryAddress = customer.putObject("DeliveryAddress");
            deliveryAddress.put("Street", "Rua Teste");
            deliveryAddress.put("Number", "123");
            deliveryAddress.put("Complement", "AP 123");
            deliveryAddress.put("ZipCode", "12345987");
            deliveryAddress.put("City", "Rio de Janeiro");
            deliveryAddress.put("State", "RJ");
            deliveryAddress.put("Country", "BRA");

            ObjectNode payment = transaction.putObject("Payment");
            payment.put("Type", "CreditCard");
            payment.put("Amount", input.amount());
            payment.put("Currency", "BRL");
            payment.put("Country", "BRA");
            payment.put("Provider", "Simulado");
            payment.put("ServiceTaxAmount", 0);
            payment.put("Installments", 1);
            payment.put("Interest", "ByMerchant");
            payment.put("Capture", false);
            payment.put("Authenticate", false);
            payment.put("Recurrent", false);
            payment.put("SoftDescriptor", "123456789ABCD");

            ObjectNode creditCard = payment.putObject("CreditCard");
            creditCard.put("CardNumber", input.creditCardNumber());
            creditCard.put("Holder", input.cardHolder());
            creditCard.put("ExpirationDate", input.expDate());
            creditCard.put("SecurityCode", input.cvv());
            creditCard.put("SaveCard", "false");
            creditCard.put("Brand", "Visa");

            String json = objectMapper.writeValueAsString(transaction);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CIELO_URL))
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .header("MerchantId", MERCHANT_ID)
                    .header("MerchantKey", MERCHANT_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectNode responseBody = (ObjectNode) objectMapper.readTree(response.body());
            ObjectNode paymentNode = (ObjectNode) responseBody.get("Payment");

            String status = "rejected";
            if ("4".equals(paymentNode.get("ReturnCode").asText())) {
                status = "approved";
            }

            return new TransactionOutput(
                    paymentNode.get("Tid").asText(),
                    paymentNode.get("AuthorizationCode").asText(),
                    status
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar transação com a Cielo", e);
        }
    }

}

package com.curso.payment.infra.gateway;

import com.curso.payment.application.dto.InputTransaction;
import com.curso.payment.application.dto.OutputTransaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PJBankGatewayTestSandboxIT {

    @Test
    void deveProcessarTransacaoNoSandboxEPrintarResposta() throws Exception {
        PJBankGateway gateway = new PJBankGateway();
        ObjectMapper objectMapper = new ObjectMapper();

        InputTransaction input = new InputTransaction(
                "PJ_BANK",
                "Cliente Teste",
                "4111111111111111",
                "12/2025",
                "123",
                100.0
        );

        OutputTransaction result = gateway.createTransaction(input);

        System.out.println("OutputTransaction gerado:");
        System.out.println(result);

        assertNotNull(result.tid());
        assertNotNull(result.authorizationCode());
        assertTrue(result.status().equalsIgnoreCase("approved") || result.status().equalsIgnoreCase("rejected"));
    }
}
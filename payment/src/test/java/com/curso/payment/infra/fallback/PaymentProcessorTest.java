package com.curso.payment.infra.fallback;

import com.curso.payment.application.PaymentGateway;
import com.curso.payment.application.PaymentProcessor;
import com.curso.payment.application.dto.TransactionInput;
import com.curso.payment.application.dto.TransactionOutput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentProcessorTest {

    @Test
    void deveProcessarPagamentoViaPJBank() {
        PaymentGateway pjBankGateway = mock(PaymentGateway.class);
        PaymentProcessor pjBankProcessor = new PJBankProcessor(pjBankGateway);

        TransactionInput input = new TransactionInput(
                PaymentMethod.PJ_BANK,
                "Cliente Teste",
                "4111111111111111",
                "12/2025",
                "123",
                100.0
        );

        TransactionOutput expectedOutput = new TransactionOutput("pix123", "authPix", "SUCESSO");
        when(pjBankGateway.createTransaction(input)).thenReturn(expectedOutput);

        TransactionOutput result = pjBankProcessor.processPayment(input);

        assertEquals("pix123", result.tid());
        assertEquals("authPix", result.authorizationCode());
        assertEquals("SUCESSO", result.status());
    }

    @Test
    void deveProcessarPagamentoViaCieloComFallback() {
        PaymentGateway pjBankGateway = mock(PaymentGateway.class);
        PaymentGateway cieloGateway = mock(PaymentGateway.class);

        PaymentProcessor pjBankProcessor = new PJBankProcessor(pjBankGateway);
        CieloProcessor cieloProcessor = new CieloProcessor(cieloGateway);

        pjBankProcessor.setNext(cieloProcessor);

        TransactionInput input = new TransactionInput(
                PaymentMethod.CIELO,
                "Cliente Teste",
                "4111111111111111",
                "12/2025",
                "123",
                150.0
        );

        TransactionOutput expectedOutput = new TransactionOutput("cielo123", "authCielo", "SUCESSO");
        when(cieloGateway.createTransaction(input)).thenReturn(expectedOutput);

        TransactionOutput result = pjBankProcessor.processPayment(input);

        assertEquals("cielo123", result.tid());
        assertEquals("authCielo", result.authorizationCode());
        assertEquals("SUCESSO", result.status());
    }

    @Test
    void deveLancarExcecaoQuandoTipoDesconhecido() {
        PaymentGateway pixGateway = mock(PaymentGateway.class);
        PJBankProcessor pixProcessor = new PJBankProcessor(pixGateway);

        TransactionInput input = new TransactionInput(
                PaymentMethod.valueOf("PAYPAL"),
                "Cliente Teste",
                "4111111111111111",
                "12/2025",
                "123",
                200.0
        );

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            pixProcessor.processPayment(input);
        });

        assertTrue(exception.getMessage().contains("Nenhum processador"));
    }

}
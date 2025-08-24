package com.curso.payment.infra.fallback;

import com.curso.payment.application.PaymentGateway;
import com.curso.payment.application.PaymentProcessor;
import com.curso.payment.application.dto.InputTransaction;
import com.curso.payment.application.dto.OutputTransaction;
import com.curso.payment.infra.gateway.CieloGateway;
import com.curso.payment.infra.gateway.PJBankGateway;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentProcessorTest {

    @Test
    void deveRealizarFallbackParaPJBankQuandoCieloFalhar() {
        CieloGateway cieloGateway = mock(CieloGateway.class);
        PJBankGateway pjBankGateway = mock(PJBankGateway.class);

        InputTransaction input = new InputTransaction(
                PaymentMethod.CIELO.toValue(),
                "Cliente Teste",
                "4111111111111111",
                "12/2025",
                "123",
                100.0
        );

        when(cieloGateway.createTransaction(any())).thenThrow(new RuntimeException("Erro Cielo"));

        PaymentProcessor cielo = new CieloProcessor(cieloGateway);
        PaymentProcessor pjBank = new PJBankProcessor(pjBankGateway);
        cielo.setNext(pjBank);

        cielo.processPayment(input);

        verify(cieloGateway).createTransaction(any());
        verify(pjBankGateway).createTransaction(any());
    }

    @Test
    void deveRealizarFallbackParaCieloQuandoPJBanckFalhar() {
        CieloGateway cieloGateway = mock(CieloGateway.class);
        PJBankGateway pjBankGateway = mock(PJBankGateway.class);

        InputTransaction input = new InputTransaction(
                PaymentMethod.CIELO.toValue(),
                "Cliente Teste",
                "4111111111111111",
                "12/2025",
                "123",
                100.0
        );

        when(pjBankGateway.createTransaction(any())).thenThrow(new RuntimeException("Erro PJBanck"));

        PaymentProcessor cielo = new CieloProcessor(cieloGateway);
        PaymentProcessor pjBank = new PJBankProcessor(pjBankGateway);
        pjBank.setNext(cielo);

        pjBank.processPayment(input);

        verify(pjBankGateway).createTransaction(any());
        verify(cieloGateway).createTransaction(any());
    }

    @Test
    void deveProcessarPagamentoViaPJBank() {
        PaymentGateway pjBankGateway = mock(PaymentGateway.class);
        PaymentGateway cieloGateway = mock(PaymentGateway.class);

        PaymentProcessor pjBankProcessor = new PJBankProcessor(pjBankGateway);
        CieloProcessor cieloProcessor = new CieloProcessor(cieloGateway);

        pjBankProcessor.setNext(cieloProcessor);

        InputTransaction input = new InputTransaction(
                PaymentMethod.CIELO.toValue(),
                "Cliente Teste",
                "4111111111111111",
                "12/2025",
                "123",
                150.0
        );

        OutputTransaction expectedOutput = new OutputTransaction("pix123", "authPix", "SUCESSO");
        when(pjBankGateway.createTransaction(input)).thenReturn(expectedOutput);

        OutputTransaction result = pjBankProcessor.processPayment(input);

        assertEquals("pix123", result.tid());
        assertEquals("authPix", result.authorizationCode());
        assertEquals("SUCESSO", result.status());
    }

    @Test
    void deveProcessarPagamentoViaCielo() {
        PaymentGateway pjBankGateway = mock(PaymentGateway.class);
        PaymentGateway cieloGateway = mock(PaymentGateway.class);

        PaymentProcessor pjBankProcessor = new PJBankProcessor(pjBankGateway);
        CieloProcessor cieloProcessor = new CieloProcessor(cieloGateway);

        cieloProcessor.setNext(pjBankProcessor);

        InputTransaction input = new InputTransaction(
                PaymentMethod.CIELO.toValue(),
                "Cliente Teste",
                "4111111111111111",
                "12/2025",
                "123",
                150.0
        );

        OutputTransaction expectedOutput = new OutputTransaction("cielo123", "authCielo", "SUCESSO");
        when(cieloGateway.createTransaction(input)).thenReturn(expectedOutput);

        OutputTransaction result = cieloProcessor.processPayment(input);

        assertEquals("cielo123", result.tid());
        assertEquals("authCielo", result.authorizationCode());
        assertEquals("SUCESSO", result.status());
    }

}
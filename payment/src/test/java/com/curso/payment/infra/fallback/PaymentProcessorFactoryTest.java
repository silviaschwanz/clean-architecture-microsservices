package com.curso.payment.infra.fallback;

import com.curso.payment.application.PaymentProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentProcessorFactoryTest {

    @Test
    void deveCriarCadeiaComCieloPrimeiroQuandoFirstMethodForCielo() {
        PaymentProcessor chain = PaymentProcessorFactory.createChain(PaymentMethod.CIELO);

        assertEquals(PaymentMethod.CIELO, chain.getSupportedMethod());
        assertEquals(PaymentMethod.PJ_BANK, chain.getNext().getSupportedMethod());
    }

    @Test
    void deveCriarCadeiaComPJBankPrimeiroQuandoFirstMethodForPJBank() {
        PaymentProcessor chain = PaymentProcessorFactory.createChain(PaymentMethod.PJ_BANK);

        assertEquals(PaymentMethod.PJ_BANK, chain.getSupportedMethod());
        assertEquals(PaymentMethod.CIELO, chain.getNext().getSupportedMethod());
    }

    @Test
    void deveLancarExcecaoQuandoMetodoNaoForSuportado() {
        assertThrows(IllegalArgumentException.class,
                () -> PaymentProcessorFactory.createChain(PaymentMethod.valueOf("PIX")));
    }

}
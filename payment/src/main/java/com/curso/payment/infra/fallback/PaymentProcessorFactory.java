package com.curso.payment.infra.fallback;

import com.curso.payment.application.PaymentProcessor;
import com.curso.payment.infra.gateway.CieloGateway;
import com.curso.payment.infra.gateway.PJBankGateway;

// Chain of Responsibility
public class PaymentProcessorFactory {

    public static PaymentProcessor createChain() {
        PaymentProcessor cielo = new CieloProcessor(new CieloGateway());
        PaymentProcessor pjBank = new PJBankProcessor(new PJBankGateway());

        cielo.setNext(pjBank);
        return cielo;
    }

}

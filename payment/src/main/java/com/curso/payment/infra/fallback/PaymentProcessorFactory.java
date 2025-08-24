package com.curso.payment.infra.fallback;

import com.curso.payment.application.PaymentProcessor;
import com.curso.payment.infra.gateway.CieloGateway;
import com.curso.payment.infra.gateway.PJBankGateway;

import java.util.ArrayList;
import java.util.List;

// Chain of Responsibility
public class PaymentProcessorFactory {

    public static PaymentProcessor createChain(PaymentMethod firstMethod) {
        isSupported(firstMethod);
        List<PaymentProcessor> processorsOrdered = reorderProcessors(firstMethod, getProcessors());

        for (int i = 0; i < processorsOrdered.size() - 1; i++) {
            processorsOrdered.get(i).setNext(processorsOrdered.get(i + 1));
        }
        return processorsOrdered.getFirst();
    }

    public static List<PaymentProcessor> getProcessors() {
        return List.of(
                new CieloProcessor(new CieloGateway()),
                new PJBankProcessor(new PJBankGateway())
        );
    }

    private static void isSupported(PaymentMethod paymentMethod) {
        boolean methodSuported = getProcessors().stream().anyMatch(processor -> processor.getSupportedMethod() == paymentMethod);
        if(!methodSuported) throw new IllegalArgumentException(PaymentErrorMessages.UNSUPPORTED_PAYMENT.format(paymentMethod));
    }

    private static List<PaymentProcessor> reorderProcessors(PaymentMethod firstMethod, List<PaymentProcessor> processors) {
        List<PaymentProcessor> ordered = new ArrayList<>();
        processors.stream()
                .filter(processor -> processor.getSupportedMethod() == firstMethod)
                .findFirst()
                .ifPresent(ordered::add);
        processors.stream()
                .filter(processor -> processor.getSupportedMethod() != firstMethod)
                .forEach(ordered::add);
        return ordered;
    }

}

package com.curso.payment.application.usecase;

import com.curso.payment.application.PaymentProcessor;
import com.curso.payment.application.TransactionRepository;
import com.curso.payment.application.dto.ProcessPaymentInput;
import com.curso.payment.application.dto.ProcessPaymentOutput;
import com.curso.payment.application.dto.TransactionInput;
import com.curso.payment.application.dto.TransactionOutput;
import com.curso.payment.domain.Transaction;
import com.curso.payment.infra.fallback.PaymentProcessorFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessPayment {

    private final PaymentProcessor paymentProcessor;
    private final TransactionRepository transactionRepository;

    public ProcessPayment(TransactionRepository transactionRepository) {
        this.paymentProcessor = PaymentProcessorFactory.createChain();
        this.transactionRepository = transactionRepository;
    }

    public ProcessPaymentOutput execute(ProcessPaymentInput input) {
        System.out.println("processPayment: " + input);
        TransactionInput transactionInput = new TransactionInput(
                input.type(),
                 "Cliente Exemplo",
                "4012001037141112",
                "05/2027",
                "123",
                input.amount()
        );
        Transaction transaction = Transaction.create(input.rideId(), input.amount());

        try {
            TransactionOutput transactionOutput = paymentProcessor.processPayment(transactionInput);
            if ("approved".equalsIgnoreCase(transactionOutput.status())) {
                transaction.pay();
                transactionRepository.saveTransaction(transaction);
                return new ProcessPaymentOutput(
                        transactionOutput.tid(),
                        transactionOutput.authorizationCode(),
                        transactionOutput.status()
                );
            } else {
                return new ProcessPaymentOutput("false", "Pagamento rejeitado", "deny");
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return new ProcessPaymentOutput("false", "Erro ao processar pagamento: " + e.getMessage(), "error");
        }
    }

}

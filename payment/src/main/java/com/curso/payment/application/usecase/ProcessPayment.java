package com.curso.payment.application.usecase;

import com.curso.payment.application.PaymentProcessor;
import com.curso.payment.application.TransactionRepository;
import com.curso.payment.application.dto.InputProcessPayment;
import com.curso.payment.application.dto.InputTransaction;
import com.curso.payment.application.dto.OutputProcessPayment;
import com.curso.payment.application.dto.OutputTransaction;
import com.curso.payment.domain.Transaction;
import com.curso.payment.infra.fallback.PaymentMethod;
import com.curso.payment.infra.fallback.PaymentProcessorFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessPayment {

    private final TransactionRepository transactionRepository;

    public ProcessPayment(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public OutputProcessPayment execute(InputProcessPayment input) {
        System.out.println("processPayment: " + input);
        InputTransaction transactionInput = new InputTransaction(
                input.type(),
                 "Cliente Exemplo",
                "4012001037141112",
                "05/2027",
                "123",
                input.amount()
        );
        PaymentProcessor paymentProcessor = PaymentProcessorFactory.createChain(PaymentMethod.from(input.type()));
        Transaction transaction = Transaction.create(input.rideId(), input.amount());

        try {
            OutputTransaction transactionOutput = paymentProcessor.processPayment(transactionInput);
            if ("approved".equalsIgnoreCase(transactionOutput.status())) {
                transaction.pay();
                transactionRepository.saveTransaction(transaction);
                return new OutputProcessPayment(
                        transactionOutput.tid(),
                        transactionOutput.authorizationCode(),
                        transactionOutput.status()
                );
            } else {
                return new OutputProcessPayment("false", "Pagamento rejeitado", "deny");
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return new OutputProcessPayment("false", "Erro ao processar pagamento: " + e.getMessage(), "error");
        }
    }

}

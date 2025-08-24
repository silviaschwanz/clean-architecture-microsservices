package com.curso.payment.application;

import com.curso.payment.domain.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository {

    Transaction saveTransaction(Transaction transaction);
    Transaction getTransactionById(String transactionId);

}

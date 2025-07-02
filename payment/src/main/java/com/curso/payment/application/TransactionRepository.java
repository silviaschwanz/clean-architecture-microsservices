package com.curso.payment.application;

import com.curso.payment.domain.Transaction;

public interface TransactionRepository {

    Transaction saveTransaction(Transaction transaction);
    Transaction getTransactionById(String transactionId);

}

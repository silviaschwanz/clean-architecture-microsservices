package com.curso.payment.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private UUID transactionId;
    private UUID rideId;
    private String status;
    private LocalDateTime date;
    private double amount;

    private Transaction(UUID transactionId, UUID rideId, double amount, LocalDateTime date, String status) {
        this.transactionId = transactionId;
        this.rideId = rideId;
        this.amount = amount;
        this.date = date;
        this.status = status;

    }

    public static Transaction create(String rideId, double amount) {
        return new Transaction(
                UUID.randomUUID(),
                UUID.fromString(rideId),
                amount,
                LocalDateTime.now(),
                "waiting_payment"
        );
    }

    public static Transaction restore(String transactionId, String rideId, double amount, LocalDateTime date, String status) {
        return new Transaction(
                UUID.fromString(transactionId),
                UUID.fromString(rideId),
                amount,
                date,
                status
        );
    }

    public void pay() {
        status = "paid";
    }

    public String getStatus() {
        return status;
    }

    public String getTransactionId() {
        return transactionId.toString();
    }

    public String getRideId() {
        return rideId.toString();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }
}

package com.curso.payment.infra.repository;

import com.curso.payment.application.TransactionRepository;
import com.curso.payment.domain.Transaction;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

@Repository
public class TransactionPostgresRespository implements TransactionRepository {

    private final DataSource dataSource;

    public TransactionPostgresRespository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        try (Connection con = dataSource.getConnection()) {
            final PreparedStatement insertStatement = con.prepareStatement(
                    "insert into transaction " +
                            "(transaction_id, ride_id, amount, date, status)" +
                            " values (?, ?, ?, ?, ?)");
            insertStatement.setObject(1, UUID.fromString(transaction.getTransactionId()));
            insertStatement.setObject(2, UUID.fromString(transaction.getRideId()));
            insertStatement.setDouble(3, transaction.getAmount());
            insertStatement.setTimestamp(4, Timestamp.valueOf(transaction.getDate()));
            insertStatement.setString(5, transaction.getStatus());
            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted == 0) {
                throw new RuntimeException("Failed to insert transaction, no rows affected");
            }
            return getTransactionById(transaction.getTransactionId());
        } catch (SQLException e) {
            throw new RuntimeException("Error saving transaction", e);
        }
    }

    @Override
    public Transaction getTransactionById(String transactionId) {
        try (Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement("select * from transaction where transaction_id = ?");
            ps.setObject(1, UUID.fromString(transactionId));
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) return Transaction.restore(
                        rs.getObject("transaction_id", UUID.class).toString(),
                        rs.getObject("ride_id", UUID.class).toString(),
                        rs.getDouble("amount"),
                        rs.getTimestamp("date").toLocalDateTime(),
                        rs.getString("status")
                        );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get transaction tid: " + transactionId, e);
        }
        throw new EntityNotFoundException("Transaction not found");
    }

}

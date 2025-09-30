package com.blocksim.infrastructure.persistence;

import com.blocksim.domain.entity.Transaction;
import com.blocksim.domain.enums.TransactionPriority;
import com.blocksim.domain.enums.TransactionStatus;
import com.blocksim.domain.repository.TransactionRepository;
import com.blocksim.infrastructure.config.DatabaseConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class TransactionRepositoryImpl implements TransactionRepository {

    @Override
    public void save(UUID walletId, Transaction transaction) {
        String sql = "INSERT INTO transaction " +
                "(id, wallet_id, sourceAddress, destinationAddress, amount, fee, createdAt, sizeInBytes, priority, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transaction.getId().toString());
            stmt.setString(2, walletId.toString());
            stmt.setString(3, transaction.getSourceAddress());
            stmt.setString(4, transaction.getDestinationAddress());
            stmt.setDouble(5, transaction.getAmount());
            stmt.setDouble(6, transaction.getFee());
            stmt.setTimestamp(7, Timestamp.valueOf(transaction.getCreatedAt()));
            stmt.setInt(8, transaction.getSizeInBytes());
            stmt.setString(9, transaction.getPriority().name());
            stmt.setString(10, transaction.getStatus().name());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error saving transaction", e);
        }
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        String sql = "SELECT * FROM transaction WHERE id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Transaction transaction = new Transaction(
                            UUID.fromString(rs.getString("id")),
                            rs.getString("sourceAddress"),
                            rs.getString("destinationAddress"),
                            rs.getDouble("amount"),
                            rs.getDouble("fee"),
                            TransactionPriority.valueOf(rs.getString("priority")),
                            TransactionStatus.valueOf(rs.getString("status")),
                            rs.getTimestamp("createdAt").toLocalDateTime()
                    );
                    transaction.setSizeInBytes(rs.getInt("sizeInBytes"));
                    return Optional.of(transaction);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding transaction by id", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT * FROM transaction";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Transaction transaction = new Transaction(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("sourceAddress"),
                        rs.getString("destinationAddress"),
                        rs.getDouble("amount"),
                        rs.getDouble("fee"),
                        TransactionPriority.valueOf(rs.getString("priority")),
                        TransactionStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("createdAt").toLocalDateTime()
                );
                transaction.setSizeInBytes(rs.getInt("sizeInBytes"));
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all transactions", e);
        }

        return transactions;
    }
}

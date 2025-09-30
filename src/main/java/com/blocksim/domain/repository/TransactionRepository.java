package com.blocksim.domain.repository;

import com.blocksim.domain.entity.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    void save(UUID walletId, Transaction transaction);
    Optional<Transaction> findById(UUID id);
    List<Transaction> findAll();
}

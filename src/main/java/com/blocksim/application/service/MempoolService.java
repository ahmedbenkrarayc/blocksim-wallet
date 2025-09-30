package com.blocksim.application.service;

import com.blocksim.domain.entity.Transaction;
import com.blocksim.domain.enums.TransactionStatus;
import com.blocksim.domain.repository.TransactionRepository;

import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Collectors;

public class MempoolService {
    private final TransactionRepository transactionRepository;
    private final static int BLOCK_TIME_MINS = 10;

    public MempoolService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getPendingTransactions() {
        return transactionRepository.findAll().stream()
                .filter(tx -> tx.getStatus() == TransactionStatus.PENDING)
                .sorted((t1, t2) -> Double.compare(t2.getFee(), t1.getFee()))
                .collect(Collectors.toList());
    }

    public OptionalInt getTransactionPosition(UUID txId) {
        List<Transaction> pending = getPendingTransactions();
        int position = 1;
        for (Transaction tx : pending) {
            if (tx.getId().equals(txId)) {
                return OptionalInt.of(position);
            }
            position++;
        }
        return OptionalInt.empty();
    }

    public OptionalDouble estimateTime(UUID txId, int transactionsPerBlock) {
        List<Transaction> pending = getPendingTransactions();
        int position = 0;
        for (Transaction tx : pending) {
            position++;
            if (tx.getId().equals(txId)) {
                double estimatedBlocks = (double) position / transactionsPerBlock;
                return OptionalDouble.of(estimatedBlocks * BLOCK_TIME_MINS);
            }
        }
        return OptionalDouble.empty();
    }
}

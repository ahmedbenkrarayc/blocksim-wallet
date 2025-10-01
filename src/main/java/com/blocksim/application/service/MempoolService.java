package com.blocksim.application.service;

import com.blocksim.domain.entity.Transaction;
import com.blocksim.domain.entity.Wallet;
import com.blocksim.domain.enums.TransactionPriority;
import com.blocksim.domain.enums.TransactionStatus;
import com.blocksim.domain.repository.TransactionRepository;
import com.blocksim.presentation.dto.response.FeeComparisonResponseDTO;

import java.util.*;
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

    public List<FeeComparisonResponseDTO> compareFeeLevels(Transaction tx, Wallet wallet, int txPerBlock) {
        List<FeeComparisonResponseDTO> result = new ArrayList<>();

        for (TransactionPriority priority : TransactionPriority.values()) {
            Transaction tempTx = new Transaction(
                    tx.getId(),
                    tx.getSourceAddress(),
                    tx.getDestinationAddress(),
                    tx.getAmount(),
                    0.0,
                    priority,
                    TransactionStatus.PENDING,
                    null,
                    tx.getSizeInBytes()
            );

            double fee = wallet.calculateFee(tempTx);

            List<Transaction> pending = getPendingTransactions();
            int position = 1;
            for (Transaction t : pending) {
                if (t.getFee() > fee) position++;
            }

            double estTime = ((double) position / txPerBlock) * BLOCK_TIME_MINS;

            result.add(new FeeComparisonResponseDTO(priority.name(), fee, position, estTime));
        }

        return result;
    }


}

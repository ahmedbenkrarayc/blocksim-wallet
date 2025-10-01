package com.blocksim.application.service;

import com.blocksim.domain.entity.Transaction;
import com.blocksim.domain.entity.Wallet;
import com.blocksim.domain.enums.TransactionPriority;
import com.blocksim.domain.enums.TransactionStatus;
import com.blocksim.domain.repository.TransactionRepository;
import com.blocksim.infrastructure.config.LoggerConfig;
import com.blocksim.presentation.dto.response.FeeComparisonResponseDTO;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MempoolService {
    private final TransactionRepository transactionRepository;
    private final Random rand = new Random();
    private final static int BLOCK_TIME_MINS = 10;
    private static final Logger logger = LoggerConfig.getLogger(WalletService.class);

    public MempoolService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        LoggerConfig.init();
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
                logger.info("Transaction found in position "+position+" out of "+pending.size()+" in getTransactionPosition");
                return OptionalInt.of(position);
            }
            position++;
        }
        logger.info("Transaction not found in getTransactionPosition");
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
        logger.info("Transaction not found, in estimateTime");
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

    private String randomEthereumAddress() {
        String hex = UUID.randomUUID().toString().replace("-", "");
        hex += UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return "0x" + hex;
    }


    private String randomBitcoinAddress() {
        String[] prefixes = {"1", "3", "bc1"};
        String prefix = prefixes[rand.nextInt(prefixes.length)];
        String body = UUID.randomUUID().toString().replace("-", "").substring(0, 25);
        return prefix + body;
    }

    public Transaction generateRandomTransaction() {
        UUID id = UUID.randomUUID();

        boolean isEth = rand.nextBoolean();
        String src = isEth ? randomEthereumAddress() : randomBitcoinAddress();
        String dst = isEth ? randomEthereumAddress() : randomBitcoinAddress();

        double amount = Math.round((rand.nextDouble() * 4.99 + 0.01) * 100.0) / 100.0;

        double minFee = 0.00001;
        double maxFee = 0.001;
        double fee = minFee + (maxFee - minFee) * rand.nextDouble();
        fee = Math.round(fee * 100_000_000.0) / 100_000_000.0;

        TransactionPriority priority = TransactionPriority.values()[rand.nextInt(TransactionPriority.values().length)];
        TransactionStatus status = TransactionStatus.PENDING;
        LocalDateTime createdAt = LocalDateTime.now();
        int sizeInBytes = rand.nextInt(301) + 200;

        return new Transaction(id, src, dst, amount, fee, priority, status, createdAt, sizeInBytes);
    }


    public List<Transaction> getMempoolWithRandomTransactions(UUID myTxId) {
        int count = rand.nextInt(11) + 10;

        List<Transaction> mempool = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            mempool.add(generateRandomTransaction());
        }

        transactionRepository.findById(myTxId).ifPresent(mempool::add);

        mempool.sort((t1, t2) -> Double.compare(t2.getFee(), t1.getFee()));
        logger.info("Mempool found "+mempool.size()+" transactions");
        return mempool;
    }

}

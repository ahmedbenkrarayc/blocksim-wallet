package com.blocksim.application.service;

import com.blocksim.application.service.util.TransactionValidator;
import com.blocksim.application.service.util.WalletValidator;
import com.blocksim.domain.entity.BitcoinWallet;
import com.blocksim.domain.entity.EthereumWallet;
import com.blocksim.domain.entity.Transaction;
import com.blocksim.domain.entity.Wallet;
import com.blocksim.domain.enums.TransactionPriority;
import com.blocksim.domain.enums.TransactionStatus;
import com.blocksim.domain.repository.TransactionRepository;
import com.blocksim.domain.repository.WalletRepository;
import com.blocksim.infrastructure.config.LoggerConfig;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private static final Logger logger = LoggerConfig.getLogger(WalletService.class);


    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        LoggerConfig.init();
    }

    public Wallet createWallet(String type) {
        WalletValidator.validateWalletType(type);

        UUID walletId = UUID.randomUUID();
        String address;
        Wallet wallet = null;

        switch (type.trim().toLowerCase()) {
            case "bitcoin":
                address = "1" + UUID.randomUUID().toString().replace("-", "").substring(0, 25);
                wallet = new BitcoinWallet(walletId, address, 0.0, null);
                break;

            case "ethereum":
                String ethAddress = "0x" + java.util.UUID.randomUUID().toString()
                        .replace("-", "")
                        + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                address = ethAddress.substring(0, 42);
                wallet = new EthereumWallet(walletId, address, 0.0, null);
                break;

            default:
                logger.severe("Unsupported wallet type: " + type);
                throw new IllegalArgumentException("Unsupported wallet type: " + type);
        }

        walletRepository.save(wallet);
        logger.info("Wallet created: " + wallet.getId());
        return wallet;
    }

    public Transaction createTransaction(String sourceAddress, String destinationAddress, double amount, TransactionPriority priority, int sizeInByte) {
        //validation
        TransactionValidator.validateAdresses(sourceAddress, destinationAddress);
        TransactionValidator.validateAmount(amount);

        Wallet wallet = walletRepository.findWalletByAddress(sourceAddress)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found with this source address"));
        
        Transaction transaction = wallet.createTransaction(destinationAddress, amount, priority, sizeInByte);
        transactionRepository.save(wallet.getId(), transaction);
        logger.info("Transaction created: " + transaction.getId());
        return transaction;
    }
}

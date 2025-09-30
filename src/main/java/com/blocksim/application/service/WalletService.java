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

import java.time.LocalDateTime;
import java.util.UUID;

public class WalletService {
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
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
                address = "0x" + UUID.randomUUID().toString().replace("-", "").substring(0, 40);
                wallet = new EthereumWallet(walletId, address, 0.0, null);
                break;

            default:
                throw new IllegalArgumentException("Unsupported wallet type: " + type);
        }

        walletRepository.save(wallet);

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
        return transaction;
    }
}

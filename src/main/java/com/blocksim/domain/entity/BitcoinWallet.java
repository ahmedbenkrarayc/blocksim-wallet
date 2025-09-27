package com.blocksim.domain.entity;

import com.blocksim.domain.enums.TransactionPriority;

import java.util.Set;
import java.util.UUID;

public class BitcoinWallet extends Wallet {
    private final static double SATOSHI_PER_BYTE = 10.0;

    public BitcoinWallet(UUID id, String address, double balance, Set<Transaction> transactions) {
        super(id, address, balance, transactions);
    }

    @Override
    public double calculateFee(Transaction transaction){
        double multiplier;
        switch (transaction.getPriority()) {
            case ECONOMIQUE: multiplier = 0.5; break;
            case STANDARD:   multiplier = 1.0; break;
            case RAPIDE:     multiplier = 2.0; break;
            default:         multiplier = 1.0;
        }
        return transaction.getSizeInBytes() * SATOSHI_PER_BYTE * multiplier / 100_000_000.0;
    }
}

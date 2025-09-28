package com.blocksim.domain.entity;

import java.util.Set;
import java.util.UUID;

public class EthereumWallet extends Wallet {
    private final static double GAS_PRICE = 50;
    private final static double GAS_LIMIT = 21000;

    public EthereumWallet(UUID id, String address, double balance, Set<Transaction> transactions) {
        super(id, address, balance, transactions);
    }

    @Override
    public double calculateFee(Transaction transaction) {
        double gasPrice;

        switch (transaction.getPriority()) {
            case ECONOMIQUE:
                gasPrice = GAS_PRICE * 0.5;
                break;
            case STANDARD:
                gasPrice = GAS_PRICE;
                break;
            case RAPIDE:
                gasPrice = GAS_PRICE * 2;
                break;
            default:
                gasPrice = GAS_PRICE;
        }

        return GAS_LIMIT * gasPrice * 1e-9;
    }
}

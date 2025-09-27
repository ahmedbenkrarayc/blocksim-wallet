package com.blocksim.domain.entity;

import com.blocksim.domain.enums.TransactionPriority;
import com.blocksim.domain.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public abstract class Wallet {
    private UUID id;
    private String address;
    private double balance;
    private Set<Transaction> transactions;

    public Wallet(UUID id, String address, double balance, Set<Transaction> transactions) {
        this.id = id != null ? id : UUID.randomUUID();
        this.address = address;
        this.balance = balance;
        this.transactions = transactions;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    //methods
    public abstract double calculateFee(Transaction transaction);

    public Transaction createTransaction(String destinationAddress, double amount, TransactionPriority priority) {
        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                this.address,
                destinationAddress,
                amount,
                0,
                priority,
                TransactionStatus.PENDING,
                LocalDateTime.now()
        );

        transactions.add(transaction);

        return transaction;
    }

}

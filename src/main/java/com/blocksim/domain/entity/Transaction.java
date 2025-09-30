package com.blocksim.domain.entity;

import com.blocksim.domain.enums.TransactionPriority;
import com.blocksim.domain.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private String sourceAddress;
    private String destinationAddress;
    private double amount;
    private double fee;
    private int sizeInBytes;
    private TransactionPriority priority;
    private TransactionStatus status;
    private LocalDateTime createdAt;

    public Transaction(UUID id, String sourceAddress, String destinationAddress, double amount, double fee, TransactionPriority priority, TransactionStatus status, LocalDateTime createdAt, int sizeInBytes) {
        this.id = id != null ? id : UUID.randomUUID();
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.amount = amount;
        this.fee = fee;
        this.sizeInBytes = sizeInBytes;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public double getFee(){
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(int sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public TransactionPriority getPriority() {
        return priority;
    }

    public void setPriority(TransactionPriority priority) {
        this.priority = priority;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

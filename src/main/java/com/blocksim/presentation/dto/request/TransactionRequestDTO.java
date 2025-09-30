package com.blocksim.presentation.dto.request;

import com.blocksim.domain.enums.TransactionPriority;

public class TransactionRequestDTO {
    private  String sourceAddress;
    private  String destinationAddress;
    private double amount;
    private TransactionPriority priority;
    private int sizeInByte;

    public TransactionRequestDTO(String sourceAddress, String destinationAddress, double amount, int sizeInByte, TransactionPriority priority) {
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.amount = amount;
        this.sizeInByte = sizeInByte;
        this.priority = priority;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionPriority getPriority() {
        return priority;
    }

    public int getSizeInByte() {
        return sizeInByte;
    }
}

package com.blocksim.presentation.dto.request;

import com.blocksim.domain.enums.TransactionPriority;

public class TransactionRequestDTO {
    private  String sourceAddress;
    private  String destinationAddress;
    private double amount;
    private TransactionPriority priority;

    public TransactionRequestDTO(String sourceAddress, String destinationAddress, double amount) {
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.amount = amount;
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
}

package com.blocksim.presentation.dto.response;

import java.util.UUID;

public class TransactionResponseDTO {
    private UUID id;
    private String type;
    private String sourceAddress;
    private String destinationAddress;
    private double amount;

    public TransactionResponseDTO(UUID id, String sourceAddress, String destinationAddress, double amount, String type) {
        this.id = id;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.amount = amount;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
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
}

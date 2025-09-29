package com.blocksim.presentation.dto.response;

import java.util.UUID;

public class WalletResponseDTO {
    private UUID id;
    private String type;
    private String address;
    private double balance;

    public WalletResponseDTO(UUID id, String type, String address, double balance) {
        this.id = id;
        this.type = type;
        this.address = address;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public double getBalance() {
        return balance;
    }
}

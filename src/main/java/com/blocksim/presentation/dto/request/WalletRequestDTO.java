package com.blocksim.presentation.dto.request;

public class WalletRequestDTO {
    private String type;

    public WalletRequestDTO() {}

    public WalletRequestDTO(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
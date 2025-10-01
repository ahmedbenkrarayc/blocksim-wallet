package com.blocksim.presentation.dto.response;

import java.util.UUID;

public class MempoolTransactionDTO {
    private UUID id;
    private String sourceAddress;
    private double fee;
    private boolean isUserTx;

    public MempoolTransactionDTO(UUID id, String sourceAddress, double fee, boolean isUserTx) {
        this.id = id;
        this.sourceAddress = sourceAddress;
        this.fee = fee;
        this.isUserTx = isUserTx;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public boolean isUserTx() {
        return isUserTx;
    }

    public void setUserTx(boolean userTx) {
        isUserTx = userTx;
    }
}

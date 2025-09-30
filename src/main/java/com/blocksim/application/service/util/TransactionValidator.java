package com.blocksim.application.service.util;

public class TransactionValidator {
    public static void validateAdresses(String senderAddress, String receiverAddress) {
        if(senderAddress == null || receiverAddress == null || senderAddress.trim().isEmpty() || receiverAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Sender address and Receiver address cannot be null");
        }

        if(senderAddress.equalsIgnoreCase(receiverAddress)) {
            throw new IllegalArgumentException("Sender address and Receiver address cannot be the same");
        }
        String senderType = senderAddress.startsWith("0x") ? "Ethereum" : "Bitcoin";
        String receiverType = receiverAddress.startsWith("0x") ? "Ethereum" : "Bitcoin";

        if(!senderType.equalsIgnoreCase(receiverType)) {
            throw new IllegalArgumentException("Sender address and Receiver address must be of same type");
        }
    }

    public static void validateAmount(double amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }
}

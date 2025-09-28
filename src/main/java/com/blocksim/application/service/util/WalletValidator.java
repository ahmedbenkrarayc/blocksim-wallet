package com.blocksim.application.service.util;

public class WalletValidator {
    public static void validateWalletType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Wallet type must not be empty");
        }

        if (!type.equalsIgnoreCase("bitcoin") && !type.equalsIgnoreCase("ethereum")) {
            throw new IllegalArgumentException("Wallet type must either be bitcoin or ethereum");
        }
    }
}

package com.blocksim.application.service;

import com.blocksim.application.service.util.WalletValidator;
import com.blocksim.domain.entity.BitcoinWallet;
import com.blocksim.domain.entity.EthereumWallet;
import com.blocksim.domain.entity.Wallet;
import com.blocksim.domain.repository.WalletRepository;

import java.util.UUID;

public class WalletService {
    private WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(String type) {
        WalletValidator.validateWalletType(type);

        UUID walletId = UUID.randomUUID();
        String address;
        Wallet wallet = null;

        switch (type.trim().toLowerCase()) {
            case "bitcoin":
                address = "1" + UUID.randomUUID().toString().replace("-", "").substring(0, 25);
                wallet = new BitcoinWallet(walletId, address, 0.0, null);
                break;

            case "ethereum":
                address = "0x" + UUID.randomUUID().toString().replace("-", "").substring(0, 40);
                wallet = new EthereumWallet(walletId, address, 0.0, null);
                break;

            default:
                throw new IllegalArgumentException("Unsupported wallet type: " + type);
        }

        walletRepository.save(wallet);

        return wallet;
    }
}

package com.blocksim.infrastructure.config;

import com.blocksim.application.service.WalletService;
import com.blocksim.domain.repository.WalletRepository;
import com.blocksim.infrastructure.persistence.WalletRepositoryImpl;

public class ApplicationContext {

    private static ApplicationContext instance;

    private final WalletService walletService;
    private final WalletRepository walletRepository;

    private ApplicationContext() {
        this.walletRepository = new WalletRepositoryImpl();
        this.walletService = new WalletService(walletRepository);
    }

    public static synchronized ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public WalletService getWalletService() {
        return walletService;
    }

    public WalletRepository getWalletRepository() {
        return walletRepository;
    }
}

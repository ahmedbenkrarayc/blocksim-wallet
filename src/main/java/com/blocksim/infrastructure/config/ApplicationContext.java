package com.blocksim.infrastructure.config;

import com.blocksim.application.service.MempoolService;
import com.blocksim.application.service.WalletService;
import com.blocksim.domain.entity.Transaction;
import com.blocksim.domain.repository.TransactionRepository;
import com.blocksim.domain.repository.WalletRepository;
import com.blocksim.infrastructure.persistence.TransactionRepositoryImpl;
import com.blocksim.infrastructure.persistence.WalletRepositoryImpl;

public class ApplicationContext {

    private static final ApplicationContext INSTANCE = new ApplicationContext();

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final MempoolService mempoolService;

    private ApplicationContext() {
        this.walletRepository = new WalletRepositoryImpl();
        this.transactionRepository = new TransactionRepositoryImpl();
        this.walletService = new WalletService(walletRepository, transactionRepository);
        this.mempoolService = new MempoolService(transactionRepository);
    }

    public static ApplicationContext getInstance() {
        return INSTANCE;
    }

    public WalletService getWalletService() {
        return walletService;
    }

    public WalletRepository getWalletRepository() {
        return walletRepository;
    }

    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }

    public MempoolService getMempoolService() {
        return mempoolService;
    }
}

package com.blocksim.presentation.controller;

import com.blocksim.application.service.WalletService;
import com.blocksim.domain.entity.BitcoinWallet;
import com.blocksim.domain.entity.Transaction;
import com.blocksim.domain.entity.Wallet;
import com.blocksim.presentation.dto.request.TransactionRequestDTO;
import com.blocksim.presentation.dto.request.WalletRequestDTO;
import com.blocksim.presentation.dto.response.TransactionResponseDTO;
import com.blocksim.presentation.dto.response.WalletResponseDTO;

public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    public WalletResponseDTO createWallet(WalletRequestDTO requestDTO) {
        Wallet wallet = walletService.createWallet(requestDTO.getType());

        return new WalletResponseDTO(
                wallet.getId(),
                wallet instanceof BitcoinWallet ? "bitcoin" : "ethereum",
                wallet.getAddress(),
                wallet.getBalance()
        );
    }

    public TransactionResponseDTO createTransaction(TransactionRequestDTO requestDTO) {
        Transaction transaction = walletService.createTransaction(requestDTO.getSourceAddress(), requestDTO.getDestinationAddress(), requestDTO.getAmount(), requestDTO.getPriority());

        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getSourceAddress(),
                transaction.getDestinationAddress(),
                transaction.getAmount(),
                transaction.getSourceAddress().startsWith("0x") ? "Ethereum" : "Bitcoin"
        );
    }
}

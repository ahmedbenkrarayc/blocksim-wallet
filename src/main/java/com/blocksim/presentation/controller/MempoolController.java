package com.blocksim.presentation.controller;

import com.blocksim.application.service.MempoolService;
import com.blocksim.domain.entity.Transaction;
import com.blocksim.domain.entity.Wallet;
import com.blocksim.presentation.dto.request.MempoolRequestDTO;
import com.blocksim.presentation.dto.response.FeeComparisonResponseDTO;
import com.blocksim.presentation.dto.response.MempoolResponseDTO;
import com.blocksim.presentation.dto.response.MempoolTransactionDTO;
import com.blocksim.presentation.dto.response.TransactionResponseDTO;

import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Collectors;

public class MempoolController {
    private final MempoolService mempoolService;

    public MempoolController(MempoolService mempoolService) {
        this.mempoolService = mempoolService;
    }

    public MempoolResponseDTO checkTransactionPosition(MempoolRequestDTO request) {
        List<Transaction> pending = mempoolService.getPendingTransactions();

        OptionalInt position = mempoolService.getTransactionPosition(request.getTransactionId());
        OptionalDouble estimatedTime = mempoolService.estimateTime(request.getTransactionId(), 5);

        if (position.isPresent() && estimatedTime.isPresent()) {
            return new MempoolResponseDTO(
                    true,
                    position.getAsInt(),
                    pending.size(),
                    estimatedTime.getAsDouble()
            );
        } else {
            return new MempoolResponseDTO(false, -1, pending.size(), -1);
        }
    }

    public List<FeeComparisonResponseDTO> compareFeeLevels(Transaction tx, Wallet wallet, int txPerBlock) {
        return mempoolService.compareFeeLevels(tx, wallet, txPerBlock);
    }

    public List<MempoolTransactionDTO> getMempool(UUID myTxId) {
        List<Transaction> mempool = mempoolService.getMempoolWithRandomTransactions(myTxId);

        return mempool.stream()
                .map(tx -> new MempoolTransactionDTO(
                        tx.getId(),
                        tx.getSourceAddress(),
                        tx.getFee(),
                        tx.getId().equals(myTxId)
                ))
                .collect(Collectors.toList());
    }
}

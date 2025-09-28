package com.blocksim.domain.repository;

import com.blocksim.domain.entity.Wallet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {
    void save(Wallet wallet);
    Optional<Wallet> findById(UUID id);
    Optional<Wallet> findWalletByAddress(String address);
    List<Wallet> findAll();
}

package com.blocksim.infrastructure.persistence;

import com.blocksim.domain.entity.BitcoinWallet;
import com.blocksim.domain.entity.EthereumWallet;
import com.blocksim.domain.entity.Wallet;
import com.blocksim.domain.repository.WalletRepository;
import com.blocksim.infrastructure.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WalletRepositoryImpl implements WalletRepository {
    @Override
    public void save(Wallet wallet) {
        try(Connection connection = DatabaseConfig.getInstance().getConnection()){
            String sql = "INSERT INTO wallet(id, address, balance) VALUES(?,?,?)" +
                         "ON DUPLICATE KEY UPDATE address=?, balance=?";
            try(PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setString(1, wallet.getId().toString());
                stmt.setString(2, wallet.getAddress());
                stmt.setDouble(3, wallet.getBalance());
                stmt.executeUpdate();
            }

            if(wallet instanceof BitcoinWallet){
                String btSql = "INSERT INTO bitcoin_wallet (wallet_id) VALUES (?) " +
                               "ON DUPLICATE KEY UPDATE wallet_id = wallet_id";
                try(PreparedStatement btsmtmt = connection.prepareStatement(btSql)){
                    btsmtmt.setString(1, wallet.getId().toString());
                    btsmtmt.executeUpdate();
                }
            }else if(wallet instanceof EthereumWallet){
                String ethSql = "INSERT INTO ethereum_wallets (wallet_id) VALUES (?) " +
                        "ON DUPLICATE KEY UPDATE wallet_id = wallet_id";
                try (PreparedStatement ethStmt = connection.prepareStatement(ethSql)) {
                    ethStmt.setString(1, wallet.getId().toString());
                    ethStmt.executeUpdate();
                }
            }

        }catch(SQLException e){
            throw new RuntimeException("Failed to fetch user", e);
        }
    }

    @Override
    public Optional<Wallet> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Wallet> findWalletByAddress(String address) {
        return Optional.empty();
    }

    @Override
    public List<Wallet> findAll() {
        return Collections.emptyList();
    }
}

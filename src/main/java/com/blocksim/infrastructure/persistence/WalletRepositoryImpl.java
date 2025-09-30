package com.blocksim.infrastructure.persistence;

import com.blocksim.domain.entity.BitcoinWallet;
import com.blocksim.domain.entity.EthereumWallet;
import com.blocksim.domain.entity.Wallet;
import com.blocksim.domain.repository.WalletRepository;
import com.blocksim.infrastructure.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
                stmt.setString(4, wallet.getAddress());
                stmt.setDouble(5, wallet.getBalance());
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
            throw new RuntimeException("Failed to fetch wallet", e);
        }
    }

    @Override
    public Optional<Wallet> findById(UUID id) {
        String sql = "SELECT w.id, w.address, w.balance, b.wallet_id AS btc_id, e.wallet_id AS eth_id"+
            " FROM wallet w"+
            " LEFT JOIN bitcoin_wallet b ON w.id = b.wallet_id"+
            " LEFT JOIN ethereum_wallet e ON w.id = e.wallet_id"+
            " WHERE w.id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return Optional.empty();

            UUID walletId = UUID.fromString(rs.getString("id"));
            String address = rs.getString("address");
            double balance = rs.getDouble("balance");

            Wallet wallet = null;
            if (rs.getString("btc_id") != null) {
                wallet = new BitcoinWallet(walletId, address, balance, null);
            } else if (rs.getString("eth_id") != null) {
                wallet = new EthereumWallet(walletId, address, balance, null);
            }

            return Optional.ofNullable(wallet);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find wallet", e);
        }
    }

    @Override
    public Optional<Wallet> findWalletByAddress(String address) {
        String sql = "SELECT w.id, w.address, w.balance, b.wallet_id AS btc_id, e.wallet_id AS eth_id"+
                " FROM wallet w"+
                " LEFT JOIN bitcoin_wallet b ON w.id = b.wallet_id"+
                " LEFT JOIN ethereum_wallet e ON w.id = e.wallet_id"+
                " WHERE w.address = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, address);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return Optional.empty();

            UUID walletId = UUID.fromString(rs.getString("id"));
            double balance = rs.getDouble("balance");

            Wallet wallet = null;
            if (rs.getString("btc_id") != null) {
                wallet = new BitcoinWallet(walletId, address, balance, null);
            } else if (rs.getString("eth_id") != null) {
                wallet = new EthereumWallet(walletId, address, balance, null);
            }

            return Optional.ofNullable(wallet);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find wallet", e);
        }
    }

    @Override
    public List<Wallet> findAll() {
        List<Wallet> wallets = new ArrayList<>();

        String sql = "SELECT w.id, w.address, w.balance, " +
                "b.wallet_id AS btc_id, e.wallet_id AS eth_id " +
                "FROM wallet w " +
                "LEFT JOIN bitcoin_wallet b ON w.id = b.wallet_id " +
                "LEFT JOIN ethereum_wallet e ON w.id = e.wallet_id";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UUID walletId = UUID.fromString(rs.getString("id"));
                String address = rs.getString("address");
                double balance = rs.getDouble("balance");

                Wallet wallet = null;
                if (rs.getString("btc_id") != null) {
                    wallet = new BitcoinWallet(walletId, address, balance, null);
                } else if (rs.getString("eth_id") != null) {
                    wallet = new EthereumWallet(walletId, address, balance, null);
                }

                if (wallet != null) {
                    wallets.add(wallet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all wallets", e);
        }

        return wallets;
    }
}

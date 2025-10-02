package com.blocksim.domain.entity;

import com.blocksim.domain.enums.TransactionPriority;
import com.blocksim.domain.enums.TransactionStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    void testBitcoinCalculateFee() {
        Set<Transaction> transactions = new HashSet<>();
        BitcoinWallet btcWallet = new BitcoinWallet(UUID.randomUUID(), "btc-address", 1.0, transactions);

        Transaction tx = btcWallet.createTransaction("dest-address", 0.5, TransactionPriority.STANDARD, 250);

        double expectedFee = 250 * 10.0 * 1.0 / 100_000_000.0; // SATOSHI_PER_BYTE * multiplier / 100M
        assertEquals(expectedFee, tx.getFee(), 1e-12);
    }

    @Test
    void testEthereumCalculateFee() {
        Set<Transaction> transactions = new HashSet<>();
        EthereumWallet ethWallet = new EthereumWallet(UUID.randomUUID(), "eth-address", 2.0, transactions);

        Transaction tx = ethWallet.createTransaction("dest-address", 1.0, TransactionPriority.RAPIDE, 0);

        double expectedFee = 21000 * 50 * 2 * 1e-9;
        assertEquals(expectedFee, tx.getFee(), 1e-12);
    }

    @Test
    void testCreateTransactionAddsToWallet() {
        Set<Transaction> transactions = new HashSet<>();
        BitcoinWallet btcWallet = new BitcoinWallet(UUID.randomUUID(), "btc-address", 1.0, transactions);

        Transaction tx = btcWallet.createTransaction("dest-address", 0.3, TransactionPriority.ECONOMIQUE, 100);

        assertTrue(btcWallet.getTransactions().contains(tx));
        assertEquals("btc-address", tx.getSourceAddress());
        assertEquals("dest-address", tx.getDestinationAddress());
        assertEquals(TransactionStatus.PENDING, tx.getStatus());
    }
}

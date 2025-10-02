package com.blocksim.presentation.console;

import com.blocksim.domain.entity.Transaction;
import com.blocksim.domain.entity.Wallet;
import com.blocksim.domain.enums.TransactionPriority;
import com.blocksim.infrastructure.config.ApplicationContext;
import com.blocksim.infrastructure.config.LoggerConfig;
import com.blocksim.presentation.controller.MempoolController;
import com.blocksim.presentation.controller.WalletController;
import com.blocksim.presentation.dto.request.*;
import com.blocksim.presentation.dto.response.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Logger;

public class MainApp {
    private static final Logger logger = LoggerConfig.getLogger(MainApp.class);

    public static void main(String[] args) {
        LoggerConfig.init();
        ApplicationContext context = ApplicationContext.getInstance();
        WalletController walletController = new WalletController(context.getWalletService());
        MempoolController mempoolController = new MempoolController(context.getMempoolService());

        Scanner scanner = new Scanner(System.in);

        scanner.nextLine();

        System.out.println("=== Welcome to BlockSim Wallet App ===");

        int choice = -1;
        do {
            printMenu();

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                logger.warning("Invalid choice type in menu.");
                System.out.println("Invalid input, please enter a number.");
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        handleCreateWallet(scanner, walletController);
                        break;
                    case 2:
                        handleCreateTransaction(scanner, walletController);
                        break;
                    case 3:
                        handleCheckTransaction(scanner, mempoolController);
                        break;
                    case 4:
                        handleCompareFees(scanner, context, mempoolController);
                        break;
                    case 5:
                        handleViewMempool(scanner, mempoolController);
                        break;
                    case 0:
                        System.out.println("Exiting... Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } catch (IllegalArgumentException e) {
                logger.warning(e.getMessage());
                System.out.println(e.getMessage());
            } catch (Exception e) {
                logger.severe(e.getMessage());
                System.out.println("Something went wrong, please try again.");
            }

        } while (choice != 0);

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Create Wallet");
        System.out.println("2. Create Transaction");
        System.out.println("3. Check Transaction Position in Mempool");
        System.out.println("4. Compare 3 fee levels");
        System.out.println("5. View Current Mempool");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleCreateWallet(Scanner scanner, WalletController walletController) {
        System.out.print("Enter wallet type (BITCOIN or ETHEREUM): ");
        String typeInput = scanner.nextLine().trim();

        WalletRequestDTO request = new WalletRequestDTO(typeInput);
        WalletResponseDTO response = walletController.createWallet(request);

        System.out.println("\nWallet created successfully!");
        System.out.println("ID      : " + response.getId());
        System.out.println("Type    : " + response.getType());
        System.out.println("Address : " + response.getAddress());
        System.out.println("Balance : " + response.getBalance());
    }

    private static void handleCreateTransaction(Scanner scanner, WalletController walletController) {
        System.out.print("Enter source wallet address: ");
        String sourceAddress = scanner.nextLine().trim();

        System.out.print("Enter destination wallet address: ");
        String destinationAddress = scanner.nextLine().trim();

        System.out.print("Enter amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            logger.warning(e.getMessage());
            System.out.println("Invalid amount, transaction cancelled.");
            return;
        }

        System.out.print("Enter size in bytes: ");
        int sizeInBytes;
        try {
            sizeInBytes = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            logger.warning(e.getMessage());
            System.out.println("Invalid size, transaction cancelled.");
            return;
        }

        System.out.print("Enter transaction priority (ECONOMIQUE, STANDARD, RAPIDE): ");
        String priorityInput = scanner.nextLine().trim().toUpperCase();

        TransactionPriority priority;
        try {
            priority = TransactionPriority.valueOf(priorityInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid priority, using STANDARD by default.");
            return;
        }

        TransactionResponseDTO txResponse = walletController.createTransaction(
                new TransactionRequestDTO(sourceAddress, destinationAddress, amount, sizeInBytes, priority)
        );

        System.out.println("\nTransaction created successfully!");
        System.out.println("ID      : " + txResponse.getId());
        System.out.println("Type    : " + txResponse.getType());
        System.out.println("From    : " + txResponse.getSourceAddress());
        System.out.println("To      : " + txResponse.getDestinationAddress());
        System.out.println("Amount  : " + txResponse.getAmount());
    }

    private static void handleCheckTransaction(Scanner scanner, MempoolController mempoolController) {
        System.out.print("Enter transaction ID: ");
        String txIdInput = scanner.nextLine().trim();

        UUID txId = UUID.fromString(txIdInput);
        MempoolResponseDTO response = mempoolController.checkTransactionPosition(new MempoolRequestDTO(txId));

        if (response.isFound()) {
            System.out.println("Your transaction is at position " +
                    response.getPosition() + " out of " + response.getTotal());
            System.out.printf("Estimated confirmation time: %.1f minutes%n", response.getEstimatedTime());
        } else {
            System.out.println("Transaction not found in the mempool.");
        }
    }

    private static void handleCompareFees(Scanner scanner, ApplicationContext context, MempoolController mempoolController) {
        System.out.print("Enter transaction ID: ");
        String feeTxIdInput = scanner.nextLine().trim();

        UUID feeTxId = UUID.fromString(feeTxIdInput);

        Optional<Transaction> tx = context.getTransactionRepository().findById(feeTxId);
        if (!tx.isPresent()) {
            logger.warning("Transaction not found in the mempool. ID: " + feeTxId);
            System.out.println("Transaction not found.");
            return;
        }

        Optional<Wallet> wallet = context.getWalletRepository().findWalletByAddress(tx.get().getSourceAddress());
        if (!wallet.isPresent()) {
            logger.warning("Wallet not found in the mempool. Address: " + tx.get().getSourceAddress());
            System.out.println("Wallet not found.");
            return;
        }

        List<FeeComparisonResponseDTO> comparisonList =
                mempoolController.compareFeeLevels(tx.get(), wallet.get(), 5);

        System.out.println("+-----------+-----------+----------+----------------------+");
        System.out.println("| Priority  | Fee       | Position | Est. Confirmation   |");
        System.out.println("+-----------+-----------+----------+----------------------+");

        for (FeeComparisonResponseDTO row : comparisonList) {
            System.out.printf("| %-9s | %9.8f | %8d | %20.1f |\n",
                    row.getPriority(), row.getFee(), row.getPosition(), row.getEstimatedTime());
        }

        System.out.println("+-----------+-----------+----------+----------------------+");
    }

    private static void handleViewMempool(Scanner scanner, MempoolController mempoolController) {
        System.out.print("Enter your transaction ID to highlight: ");
        String myTxIdInput = scanner.nextLine().trim();

        UUID myTxId = UUID.fromString(myTxIdInput);
        List<MempoolTransactionDTO> mempool = mempoolController.getMempool(myTxId);

        System.out.println("\n=== ÉTAT DU MEMPOOL ===");
        System.out.println("Transactions en attente : " + mempool.size());
        System.out.println("┌──────────────────────────────────┬──────────────────┐");
        System.out.println("│ Transaction                      │ Frais            │");
        System.out.println("├──────────────────────────────────┼──────────────────┤");

        for (MempoolTransactionDTO tx : mempool) {
            String displayAddress = tx.isUserTx()
                    ? ">>> VOTRE TX: " + tx.getId().toString().substring(0, 10) + "..."
                    : tx.getSourceAddress().substring(0, 10) + "...";
            System.out.printf("│ %-32s │ %6.8f       │%n", displayAddress, tx.getFee());
        }

        System.out.println("└──────────────────────────────────┴──────────────────┘");
    }
}

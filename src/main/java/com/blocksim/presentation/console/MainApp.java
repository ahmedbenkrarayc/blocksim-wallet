package com.blocksim.presentation.console;

import com.blocksim.infrastructure.config.ApplicationContext;
import com.blocksim.presentation.controller.WalletController;
import com.blocksim.presentation.dto.request.TransactionRequestDTO;
import com.blocksim.presentation.dto.request.WalletRequestDTO;
import com.blocksim.presentation.dto.response.TransactionResponseDTO;
import com.blocksim.presentation.dto.response.WalletResponseDTO;

import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {
        ApplicationContext context = ApplicationContext.getInstance();
        WalletController walletController = new WalletController(context.getWalletService());

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Welcome to BlockSim Wallet App ===");

        int choice = -1;
        do {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Create Wallet");
            System.out.println("2. Create Transaction");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter wallet type (BITCOIN or ETHEREUM): ");
                    String typeInput = scanner.nextLine().trim();

                    WalletRequestDTO request = new WalletRequestDTO(typeInput);

                    try {
                        WalletResponseDTO response = walletController.createWallet(request);

                        System.out.println("\nWallet created successfully!");
                        System.out.println("ID      : " + response.getId());
                        System.out.println("Type    : " + response.getType());
                        System.out.println("Address : " + response.getAddress());
                        System.out.println("Balance : " + response.getBalance());

                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                break;
                case 2:
                    System.out.print("Enter source wallet address: ");
                    String sourceAddress = scanner.nextLine().trim();

                    System.out.print("Enter destination wallet address: ");
                    String destinationAddress = scanner.nextLine().trim();

                    System.out.print("Enter amount: ");
                    double amount;
                    try {
                        amount = Double.parseDouble(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount, transaction cancelled.");
                        continue;
                    }

                    try {
                        TransactionResponseDTO txResponse = walletController.createTransaction(
                                new TransactionRequestDTO(sourceAddress, destinationAddress, amount)
                        );

                        System.out.println("\nTransaction created successfully!");
                        System.out.println("ID      : " + txResponse.getId());
                        System.out.println("Type    : " + txResponse.getType());
                        System.out.println("From    : " + txResponse.getSourceAddress());
                        System.out.println("To      : " + txResponse.getDestinationAddress());
                        System.out.println("Amount  : " + txResponse.getAmount());

                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                break;
                case 0: System.out.println("Exiting... Goodbye!"); break;
                default: System.out.println("Invalid choice, please try again.");
            }

        } while (choice != 0);

        scanner.close();
    }
}

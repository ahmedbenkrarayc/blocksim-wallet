package com.blocksim.presentation.console;

import com.blocksim.infrastructure.config.ApplicationContext;
import com.blocksim.presentation.controller.WalletController;
import com.blocksim.presentation.dto.request.WalletRequestDTO;
import com.blocksim.presentation.dto.response.WalletResponseDTO;

import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        ApplicationContext context = ApplicationContext.getInstance();

        WalletController walletController = new WalletController(context.getWalletService());

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Crypto Wallet Simulator ===");
        System.out.print("Enter wallet type (BITCOIN or ETHEREUM): ");
        String typeInput = scanner.nextLine();

        WalletRequestDTO request = new WalletRequestDTO(typeInput);

        try {
            WalletResponseDTO response = walletController.createWallet(request);

            System.out.println("Wallet created successfully!");
            System.out.println("ID      : " + response.getId());
            System.out.println("Type    : " + response.getType());
            System.out.println("Address : " + response.getAddress());
            System.out.println("Balance : " + response.getBalance());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

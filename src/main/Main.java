package main;

import services.BankOperations;
import java.util.Scanner;

public class Main {

    // ANSI escape codes for color
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String BLUE = "\u001B[34m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Menu for the user to choose operations
        while (true) {
            clearConsole();
            printHeader();

            System.out.println(GREEN + "Welcome to the Bank System!" + RESET);
            System.out.println("Please select an option from the menu below:");
            System.out.println();

            System.out.println(YELLOW + "1. Create Account" + RESET);
            System.out.println(YELLOW + "2. Deposit" + RESET);
            System.out.println(YELLOW + "3. Withdraw" + RESET);
            System.out.println(YELLOW + "4. View Account Details" + RESET);
            System.out.println(YELLOW + "5. View Transaction History" + RESET);
            System.out.println(RED + "6. Exit" + RESET);
            System.out.print("Your choice (1-6): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline left-over

            switch (choice) {
                case 1:
                    // Create Account
                    createAccountFlow(scanner);
                    break;

                case 2:
                    // Deposit
                    depositFlow(scanner);
                    break;

                case 3:
                    // Withdraw
                    withdrawFlow(scanner);
                    break;

                case 4:
                    // View Account Details
                    viewAccountDetailsFlow(scanner);
                    break;

                case 5:
                    // View Transaction History
                    viewTransactionHistoryFlow(scanner);
                    break;

                case 6:
                    // Exit
                    System.out.println(GREEN + "Thank you for using our service! Goodbye." + RESET);
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println(RED + "Invalid choice. Please select a valid option (1-6)." + RESET);
                    break;
            }
        }
    }

    // Print header to clear console and display the title
    private static void printHeader() {
        System.out.println(BLUE + "---------------------------------------------");
        System.out.println("          *** BANKING SYSTEM ***");
        System.out.println("---------------------------------------------" + RESET);
    }

    // Clear the console (works in most environments)
    private static void clearConsole() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Error clearing console.");
        }
    }

    // Create Account flow
    private static void createAccountFlow(Scanner scanner) {
        System.out.println("Enter account number:");
        String accountNumber = scanner.nextLine();

        System.out.println("Enter account holder name:");
        String accountHolderName = scanner.nextLine();

        System.out.println("Enter initial balance:");
        double initialBalance = scanner.nextDouble();
        scanner.nextLine(); // consume newline left-over

        // Create new Account object
        BankOperations.createAccount(new models.Account(accountNumber, accountHolderName, initialBalance));
        System.out.println(GREEN + "Account created successfully!" + RESET);
        waitForUserInput();
    }

    // Deposit flow
    private static void depositFlow(Scanner scanner) {
        System.out.println("Enter account number for deposit:");
        String accountNumber = scanner.nextLine();
        System.out.println("Enter deposit amount:");
        double depositAmount = scanner.nextDouble();

        BankOperations.deposit(accountNumber, depositAmount);
        waitForUserInput();
    }

    // Withdraw flow
    private static void withdrawFlow(Scanner scanner) {
        System.out.println("Enter account number for withdrawal:");
        String accountNumber = scanner.nextLine();
        System.out.println("Enter withdrawal amount:");
        double withdrawalAmount = scanner.nextDouble();

        BankOperations.withdraw(accountNumber, withdrawalAmount);
        waitForUserInput();
    }

    // View Account Details flow
    private static void viewAccountDetailsFlow(Scanner scanner) {
        System.out.println("Enter account number to view details:");
        String accountNumber = scanner.nextLine();

        BankOperations.viewAccountDetails(accountNumber);
        waitForUserInput();
    }

    // View Transaction History flow
    private static void viewTransactionHistoryFlow(Scanner scanner) {
        System.out.println("Enter account number to view transaction history:");
        String accountNumber = scanner.nextLine();
        var transactions = BankOperations.getTransactions(accountNumber);

        System.out.println("Transaction History:");
        transactions.forEach(transaction -> {
            System.out.println("Transaction ID: " + transaction.getTransactionId() +
                    ", Date: " + transaction.getTransactionDate() +
                    ", Amount: " + transaction.getAmount() +
                    ", Type: " + transaction.getTransactionType());
        });
        waitForUserInput();
    }

    // Wait for user input to proceed
    private static void waitForUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }
}

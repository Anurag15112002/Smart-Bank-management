package services;

import models.Account;
import models.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BankOperations {

    private static Connection conn = DatabaseConnection.getConnection();

    // Create a new account in the database
    public static void createAccount(Account account) {
        String query = "INSERT INTO accounts (account_number, account_holder_name, balance) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, account.getAccountNumber());
            stmt.setString(2, account.getAccountHolderName());
            stmt.setDouble(3, account.getBalance());
            stmt.executeUpdate();
            System.out.println("Account created successfully: " + account.getAccountNumber());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error creating the account.");
        }
    }

    // Deposit method using account number
    public static void deposit(String accountNumber, double amount) {
        Account account = getAccountByAccountNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found!");
            return;
        }

        // Deposit the amount and save the transaction
        account.setBalance(account.getBalance() + amount);
        saveTransaction(account, amount, "Deposit");

        // Update the balance in the database
        updateAccountBalance(account);
        System.out.println("Deposit successful. New balance: " + account.getBalance());
    }

    // Withdraw method using account number
    public static void withdraw(String accountNumber, double amount) {
        Account account = getAccountByAccountNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found!");
            return;
        }

        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            saveTransaction(account, amount, "Withdrawal");

            // Update the balance in the database
            updateAccountBalance(account);
            System.out.println("Withdrawal successful. New balance: " + account.getBalance());
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    // View account details by account number
    public static void viewAccountDetails(String accountNumber) {
        Account account = getAccountByAccountNumber(accountNumber);
        if (account != null) {
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Holder: " + account.getAccountHolderName());
            System.out.println("Balance: " + account.getBalance());
        } else {
            System.out.println("Account not found!");
        }
    }

    // Save the transaction in the database
    private static void saveTransaction(Account account, double amount, String type) {
        String transactionId = UUID.randomUUID().toString();
        String query = "INSERT INTO transactions (transaction_id, account_number, transaction_date, amount, transaction_type) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transactionId);
            stmt.setString(2, account.getAccountNumber());
            stmt.setTimestamp(3, new Timestamp(new Date().getTime()));
            stmt.setDouble(4, amount);
            stmt.setString(5, type);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update the account balance in the database
    private static void updateAccountBalance(Account account) {
        String query = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, account.getBalance());
            stmt.setString(2, account.getAccountNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve account by account number from the database
    private static Account getAccountByAccountNumber(String accountNumber) {
        String query = "SELECT * FROM accounts WHERE account_number = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String accountHolderName = rs.getString("account_holder_name");
                double balance = rs.getDouble("balance");
                return new Account(accountNumber, accountHolderName, balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieve transaction history for a given account
    public static List<Transaction> getTransactions(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE account_number = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String transactionId = rs.getString("transaction_id");
                Date transactionDate = rs.getTimestamp("transaction_date");
                double amount = rs.getDouble("amount");
                String transactionType = rs.getString("transaction_type");
                transactions.add(new Transaction(transactionId, transactionDate, amount, transactionType));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }
}

package services;

import models.Account;

public class FraudDetection {

    private static final double THRESHOLD = 10000.00; // Define threshold for high withdrawal

    public static boolean detectFraud(Account account, double withdrawalAmount) {
        if (withdrawalAmount > THRESHOLD) {
            System.out.println("Warning: Potential fraud detected on account " + account.getAccountNumber() + " for withdrawal of " + withdrawalAmount);
            return true;
        }
        return false;
    }
}

package models;

import java.util.Date;

public class Transaction {
    private String transactionId;
    private Date transactionDate;
    private double amount;
    private String transactionType;

    public Transaction(String transactionId, Date transactionDate, double amount, String transactionType) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public double getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }
}

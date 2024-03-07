package com.fraud.detection;

public class Transaction {

    private long timestamp;
    private double amount;
    private String userID;
    private String serviceID;

    public Transaction(long timestamp, double amount, String userID, String serviceID) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.userID = userID;
        this.serviceID = serviceID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public String getUserID() {
        return userID;
    }

    public String getServiceID() {
        return serviceID;
    }
}

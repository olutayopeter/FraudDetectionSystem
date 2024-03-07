package com.fraud.detection;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        String filename = "/input.json";
        ConcurrentLinkedQueue<Transaction> transactionQueue = TransactionReader.readTransactionsFromFile(filename);

        TransactionStreamProcessor processor = new TransactionStreamProcessor();

        while (!transactionQueue.isEmpty()) {
            Transaction transaction = transactionQueue.poll();
            processor.processTransaction(transaction);
        }

        // Print fraudulent user alerts
        List<FraudulentUserAlert> alerts = processor.getFraudulentUserAlerts();
        for (FraudulentUserAlert alert : alerts) {
            System.out.println("Alert for User " + alert.getUserID() + ": " + alert.getAlertMessage());
        }
    }

}

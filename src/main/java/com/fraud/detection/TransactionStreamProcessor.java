package com.fraud.detection;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;


public class TransactionStreamProcessor {

    private ConcurrentHashMap<String, Integer> serviceCountMap;
    private ConcurrentHashMap<String, Double> userAverageAmountMap;
    private ConcurrentHashMap<String, Long> lastTransactionTimeMap;
    private ConcurrentHashMap<String, String> lastServiceIDMap;
    private List<FraudulentUserAlert> fraudulentUserAlerts;
    private PriorityQueue<Transaction> transactionQueue;

    public TransactionStreamProcessor() {
        serviceCountMap = new ConcurrentHashMap<>();
        userAverageAmountMap = new ConcurrentHashMap<>();
        lastTransactionTimeMap = new ConcurrentHashMap<>();
        lastServiceIDMap = new ConcurrentHashMap<>();
        fraudulentUserAlerts = new ArrayList<>();
        transactionQueue = new PriorityQueue<>((t1, t2) -> Long.compare(t1.getTimestamp(), t2.getTimestamp()));
    }

    public void processTransaction(Transaction transaction) {

        /**
         * Add the transaction to the priority queue, which sorts transactions based on their timestamps
         */
        transactionQueue.offer(transaction);

        /**
         * Process transactions in order to handle out-of-order events
         */
        processTransactionsInOrder();
    }

    private void processTransactionsInOrder() {
        while (!transactionQueue.isEmpty()) {
            Transaction transaction = transactionQueue.poll();
            updateServiceCount(transaction);
            updateUserAverageAmount(transaction);
            detectFraud(transaction);
        }
    }

    private void updateServiceCount(Transaction transaction) {
        serviceCountMap.compute(transaction.getUserID(), (userID, count) -> count == null ? 1 : count + 1);
    }

    private void updateUserAverageAmount(Transaction transaction) {
        userAverageAmountMap.merge(transaction.getUserID(), transaction.getAmount(), Double::sum);
    }

    private void detectFraud(Transaction transaction) {
        String userID = transaction.getUserID();
        int serviceCount = serviceCountMap.getOrDefault(userID, 0);
        double averageAmount = userAverageAmountMap.getOrDefault(userID, 0.0) / Math.max(1, serviceCount); // Avoid division by zero

        /**
         * Criteria 1: A user conducting transactions in more than 3 distinct services within a 5-minute window
         */
        if (serviceCount > 3 && isWithinFiveMinuteWindow(userID, transaction.getTimestamp())) {
            fraudulentUserAlerts.add(new FraudulentUserAlert(userID, "User conducted transactions in more than 3 distinct services within a 5-minute window."));
        }

        /**
         * Criteria 2: Transactions that are 5 times above the user's average transaction amount in the last 24 hours
         */
        if (5 * transaction.getAmount() > averageAmount) {
            fraudulentUserAlerts.add(new FraudulentUserAlert(userID, "User has a transaction significantly higher than the average amount."));
        }

        /**
         * Criteria 3: A sequence of transactions indicating "ping-pong" activity within 10 minutes
         */
        if (isPingPongActivity(userID, transaction)) {
            fraudulentUserAlerts.add(new FraudulentUserAlert(userID, "User involved in ping-pong activity within 10 minutes."));
        }

        /**
         * Update last transaction information
         */
        lastTransactionTimeMap.put(userID, transaction.getTimestamp());
        lastServiceIDMap.put(userID, transaction.getServiceID());
    }

    private boolean isWithinFiveMinuteWindow(String userID, long currentTimestamp) {
        Long lastTransactionTime = lastTransactionTimeMap.get(userID);
        return lastTransactionTime != null && currentTimestamp - lastTransactionTime <= 5 * 60 * 1000;
    }

    private boolean isPingPongActivity(String userID, Transaction transaction) {
        Long lastTransactionTime = lastTransactionTimeMap.get(userID);
        if (lastTransactionTime != null && transaction.getTimestamp() - lastTransactionTime <= 10 * 60 * 1000) {
            String lastServiceID = lastServiceIDMap.get(userID);
            if (lastServiceID != null && !lastServiceID.equals(transaction.getServiceID())) {
                // Ping-pong activity detected
                return true;
            }
        }
        return false;
    }

    public List<FraudulentUserAlert> getFraudulentUserAlerts() {
        return fraudulentUserAlerts;
    }

}


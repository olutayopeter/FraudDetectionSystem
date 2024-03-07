# Fraud Detection System Prototype

This prototype implements a real-time fraud detection system for monitoring transactions within a fintech ecosystem. The system processes a stream of transaction events and identifies potential fraudulent activities based on specified criteria. It addresses the constraint of out-of-order event processing by maintaining data structures optimized for concurrent access and thread safety.

## Setup Instructions:

To run the prototype, follow these steps:

1.  Clone the repository to your local machine.
2.  Ensure you have Java installed.
3.  Compile the Java files using a Java compiler.
4.  Run the Main class.


## Algorithm Description

The fraud detection system processes a stream of transactions in real-time, identifying potential fraud based on specified patterns. The implemented algorithm focuses on efficiency and scalability, handling large volumes of data without relying on external libraries.

## Solution Approach

The solution employs multi-threading to process transactions concurrently, ensuring scalability with large volumes of data. It utilizes concurrent data structures such as ConcurrentHashMap to handle concurrent updates safely. For out-of-order event processing, the system maintains a map of the last transaction time for each user and service ID, enabling efficient detection of time-based patterns.

### Fraud Detection Criteria
```
The Fraud Detection System Prototype detects fraudulent user activities in a stream of transaction events. It employs three criteria to identify potential fraud:

1.  A user conducting transactions in more than 3 distinct services within a 5-minute window.
2.  Transactions that are 5 times above the user's average transaction amount in the last 24 hours.
3.  A sequence of transactions indicating "ping-pong" activity within 10 minutes.
```

## Handling Out-of-Order Events

To handle out-of-order events, the system utilizes a priority queue to store incoming transactions, sorted based on their timestamps. When a new transaction arrives, it is added to the priority queue. Then, the processTransactionsInOrder() method is called to ensure transactions are processed in the correct order, even if they arrive out of order.


## Test Dataset

A sample input dataset is provided in JSON format. An input.json file is provided in src/main/resources package

### Input Data:

```json
[{ "timestamp": 1617906000, "amount": 150.00, "userID": "user1", "serviceID": "serviceA" },
 { "timestamp": 1617906060, "amount": 4500.00, "userID": "user2", "serviceID": "serviceB" },
 { "timestamp": 1617906120, "amount": 75.00, "userID": "user1", "serviceID": "serviceC" },
 { "timestamp": 1617906180, "amount": 3000.00, "userID": "user3", "serviceID": "serviceA" },
 { "timestamp": 1617906240, "amount": 200.00, "userID": "user1", "serviceID": "serviceB" },
 { "timestamp": 1617906300, "amount": 4800.00, "userID": "user2", "serviceID": "serviceC" },
 { "timestamp": 1617906360, "amount": 100.00, "userID": "user4", "serviceID": "serviceA" },
 { "timestamp": 1617906420, "amount": 4900.00, "userID": "user3", "serviceID": "serviceB" },
 { "timestamp": 1617906480, "amount": 120.00, "userID": "user1", "serviceID": "serviceD" },
 { "timestamp": 1617906540, "amount": 5000.00, "userID": "user3", "serviceID": "serviceC" }]
 
To simulate real-time data processing, you can use a script to read the transactions from the test dataset file and feed them into the Fraud Detection System Prototype. The expected output of the program when run against this dataset is provided below.

 

### Expected Results:
The expected alerts generated by the prototype when run against the test dataset are as follows:

Alert for User user1: User has a transaction significantly higher than the average amount.
Alert for User user2: User has a transaction significantly higher than the average amount.
Alert for User user1: User has a transaction significantly higher than the average amount.
Alert for User user1: User involved in ping-pong activity within 10 minutes.
Alert for User user3: User has a transaction significantly higher than the average amount.
Alert for User user1: User has a transaction significantly higher than the average amount.
Alert for User user1: User involved in ping-pong activity within 10 minutes.
Alert for User user2: User has a transaction significantly higher than the average amount.
Alert for User user2: User involved in ping-pong activity within 10 minutes.
Alert for User user4: User has a transaction significantly higher than the average amount.
Alert for User user3: User has a transaction significantly higher than the average amount.
Alert for User user3: User involved in ping-pong activity within 10 minutes.
Alert for User user1: User conducted transactions in more than 3 distinct services within a 5-minute window.
Alert for User user1: User has a transaction significantly higher than the average amount.
Alert for User user1: User involved in ping-pong activity within 10 minutes.
Alert for User user3: User has a transaction significantly higher than the average amount.
Alert for User user3: User involved in ping-pong activity within 10 minutes.
Alert for User user3: User conducted transactions in more than 3 distinct services within a 5-minute window.
Alert for User user3: User has a transaction significantly higher than the average amount.
Alert for User user3: User involved in ping-pong activity within 10 minutes.
Alert for User user2: User has a transaction significantly higher than the average amount.
Alert for User user3: User conducted transactions in more than 3 distinct services within a 5-minute window.
Alert for User user3: User has a transaction significantly higher than the average amount.
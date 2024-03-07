package com.fraud.detection;

import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TransactionReader {

    public static ConcurrentLinkedQueue<Transaction> readTransactionsFromFile(String filename) {
        ConcurrentLinkedQueue<Transaction> transactions = new ConcurrentLinkedQueue<>();
        System.out.println("file name: " + filename);

        try (InputStream inputStream = TransactionReader.class.getResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            String jsonString = jsonContent.toString();

            // Remove leading and trailing square brackets if present
            if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
                jsonString = jsonString.substring(1, jsonString.length() - 1);
            }
            
            /**
             * Split JSON string by "}," to extract individual transactions
             */
            String[] transactionStrings = jsonString.split("\\},\\s*");
            for (String transactionString : transactionStrings) {
                /**
                 * Add back "}" if it's not the last transaction string
                 */
                if (!transactionString.endsWith("}")) {
                    transactionString += "}";
                }
                
                /**
                 *  Parse each transaction string
                 */
                Transaction transaction = parseTransaction(transactionString);
                if (transaction != null) {
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    private static Transaction parseTransaction(String transactionString) {
        try {
            
            /**
             *  Extract fields from transactionString using regular expressions
             */
            long timestamp = extractLongValue(transactionString, "timestamp");
            double amount = extractDoubleValue(transactionString, "amount");
            String userID = extractStringValue(transactionString, "userID");
            String serviceID = extractStringValue(transactionString, "serviceID");
            return new Transaction(timestamp, amount, userID, serviceID);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            /**
             *  Handle parsing errors
             */
            e.printStackTrace();
            return null;
        }
    }

    private static long extractLongValue(String transactionString, String field) {
        String pattern = "\"" + field + "\":\\s*(\\d+)";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(transactionString);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        } else {
            throw new NumberFormatException("No match found for " + field);
        }
    }

    private static double extractDoubleValue(String transactionString, String field) {
        String pattern = "\"" + field + "\":\\s*(\\d+(?:\\.\\d+)?)";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(transactionString);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        } else {
            throw new NumberFormatException("No match found for " + field);
        }
    }

    private static String extractStringValue(String transactionString, String field) {
        String pattern = "\"" + field + "\":\\s*\"([^\"]+)\"";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(transactionString);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new ArrayIndexOutOfBoundsException("No match found for " + field);
        }
    }
    private static String getValue(String transactionString, String field) {
        int startIndex = transactionString.indexOf("\"" + field + "\"") + field.length() + 3; // Index after field name and colon
        int endIndex = transactionString.indexOf("\"", startIndex);
        return transactionString.substring(startIndex, endIndex);
    }


}

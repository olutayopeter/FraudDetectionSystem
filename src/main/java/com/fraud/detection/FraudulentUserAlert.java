package com.fraud.detection;

public class FraudulentUserAlert {

    private String userID;
    private String alertMessage;

    public FraudulentUserAlert(String userID, String alertMessage) {
        this.userID = userID;
        this.alertMessage = alertMessage;
    }

    public String getUserID() {
        return userID;
    }

    public String getAlertMessage() {
        return alertMessage;
    }
}

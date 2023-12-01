package com.biswanath.promhighschoolhs.StudentZone;

import java.util.Date;

public class MessageModel {
    String senderId;
    String message;
    String currentTime;
    Date currentDate;

    public MessageModel(String senderId, String message, String currentTime, Date currentDate) {
        this.senderId = senderId;
        this.message = message;
        this.currentTime = currentTime;
        this.currentDate = currentDate;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}

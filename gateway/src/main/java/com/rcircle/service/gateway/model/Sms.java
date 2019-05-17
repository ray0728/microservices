package com.rcircle.service.gateway.model;

import java.io.Serializable;

public class Sms implements Serializable {
    public static final int BROADCAST_UID = 0;
    public static final String BROADCAST_NAME = "SMS CENTER";
    private String sender_name;
    private int sender_uid;
    private int receiver_uid;
    private String message;
    private long date;

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public int getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(int sender_uid) {
        this.sender_uid = sender_uid;
    }

    public int getReceiver_uid() {
        return receiver_uid;
    }

    public void setReceiver_uid(int receiver_uid) {
        this.receiver_uid = receiver_uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}

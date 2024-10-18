package com.walhalla.smsregclient.network.beans;

/**
 * Created by combo on 25.03.2017.
 */

public class Items {
    private String sender;

    private String text;

    private String date;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ClassPojo [sender = " + sender + ", text = " + text + ", date = " + date + "]";
    }
}

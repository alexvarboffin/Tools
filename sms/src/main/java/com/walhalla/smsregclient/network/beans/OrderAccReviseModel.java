package com.walhalla.smsregclient.network.beans;

/**
 * Created by combo on 24.03.2017.
 */

public class OrderAccReviseModel {
    private String response;

    private String histid;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getHistid() {
        return histid;
    }

    public void setHistid(String histid) {
        this.histid = histid;
    }

    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", histid = " + histid + "]";
    }
}
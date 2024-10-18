package com.walhalla.smsregclient.network.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by combo on 24.03.2017.
 */

public class ListOrders {
    private String countready;

    private String countorder;

    private String answer;

    private String service;

    @SerializedName("order_id")
    private String orderId;

    public String getCountready() {
        return countready;
    }

    public void setCountready(String countready) {
        this.countready = countready;
    }

    public String getCountorder() {
        return countorder;
    }

    public void setCountorder(String countorder) {
        this.countorder = countorder;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String order_id) {
        this.orderId = order_id;
    }

    @Override
    public String toString() {
        return "ClassPojo [countready = " + countready + ", countorder = "
                + countorder + ", answer = "
                + answer + ", service = " + service + ", order_id = "
                + orderId + "]";
    }
}

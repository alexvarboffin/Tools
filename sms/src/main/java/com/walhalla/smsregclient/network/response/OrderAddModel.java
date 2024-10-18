package com.walhalla.smsregclient.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.walhalla.smsregclient.network.badbackend.BaseResponse;

/**
 * Created by combo on 24.03.2017.
 */

public class OrderAddModel extends BaseResponse {

    @SerializedName("order_id")
    @Expose
    private String order_id;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", order_id = " + order_id + "]";
    }
}


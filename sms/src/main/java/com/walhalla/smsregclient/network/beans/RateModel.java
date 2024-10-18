package com.walhalla.smsregclient.network.beans;

import com.walhalla.smsregclient.network.badbackend.BaseResponse;

/**
 * F5F8FF
 * Created by combo on 24.03.2017.
 */

public class RateModel extends BaseResponse {

    private String rate;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", rate = " + rate + "]";
    }
}

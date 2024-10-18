package com.walhalla.smsregclient.network.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.walhalla.smsregclient.network.badbackend.BaseResponse;

/**
 * Created by combo on 24.03.2017.
 * <p>
 * {"response":"ERROR","error_msg":"setReady to this TZID not applicable"}
 */

public class APIError extends BaseResponse {

    public APIError() {}

    @SerializedName("error_msg")
    @Expose
    public String error;

    @Override
    public String toString() {
        return "APIError{" +
                "error='" + error + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}

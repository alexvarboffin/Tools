package com.walhalla.smsregclient.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.walhalla.smsregclient.network.badbackend.BaseResponse;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;


@Keep
@KeepClassMembers
public class Balance extends BaseResponse {

    @SerializedName("balance")
    @Expose
    public String balance;


    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", balance = " + balance + "]";
    }
}

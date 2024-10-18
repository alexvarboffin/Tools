package com.walhalla.smsregclient.network.badbackend;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

/**
 * Created by combo on 09.04.2017.
 *
 * {response: "data"}
 */
@Keep
@KeepClassMembers
public class BaseResponse {

    @SerializedName("response")
    @Expose
    public String response;

    public BaseResponse() {
    }
}

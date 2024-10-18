package com.walhalla.smsregclient.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.walhalla.smsregclient.network.badbackend.BaseResponse;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

/**
 * Created by combo on 24.03.2017.
 * <p>
 * Метод setOperationOver
 */
@Keep
@KeepClassMembers
public class GlobalResponse extends BaseResponse {

    @SerializedName("tzid")
    @Expose
    public String tzid;

    public GlobalResponse() {}

    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", tzid = " + tzid + "]";
    }
}

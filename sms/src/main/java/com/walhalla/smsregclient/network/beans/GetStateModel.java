package com.walhalla.smsregclient.network.beans;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

/**
 * Created by combo on 24.03.2017.
 */
@Keep
@KeepClassMembers
public class GetStateModel {

    public String response;
    private String service;
    private String number;
    private String msg;


    public void setResponse(String response) {
        this.response = response;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", service = " + service + ", number = " + number + ", msg = " + msg + "]";
    }
}

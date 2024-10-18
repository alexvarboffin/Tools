package com.walhalla.smsregclient.network.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by combo on 21.03.2017.
 */

public class OperationBean {

    @SerializedName("tzid")
    @Expose
    public String tzid;
    @SerializedName("service")
    @Expose
    private String service = "";
    @SerializedName("phone")
    @Expose
    private String phone = "";
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("msg")
    @Expose
    private String msg = "";

    /**
     * No args constructor for use in serialization
     */
    public OperationBean() {
    }

    /**
     * @param phone
     * @param status
     * @param service
     * @param msg
     * @param tzid
     */
    public OperationBean(String tzid, String service, String phone, String status, String msg) {
        super();
        this.tzid = tzid;
        this.service = service;
        this.phone = phone;
        this.status = status;
        this.msg = msg;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ClassPojo [phone = " + phone + ", status = "
                + status + ", service = " + service + ", tzid = " + tzid + ", ansver"
                + msg
                //answer
                + "]";
    }
}
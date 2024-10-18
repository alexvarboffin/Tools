package com.walhalla.smsregclient.ui.adapter;

/**
 * Created by combo on 12.04.2017.
 */
public class SpinnerItem {

    String value;
    String code;


    public SpinnerItem(String value, String code) {
        this.value = value;
        this.code = code;
    }

    String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

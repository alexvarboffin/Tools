package com.walhalla.smsregclient.network.beans;

import java.util.Arrays;

/**
 * Created by combo on 24.03.2017.
 */

public class VsimGetSMSModel {
    private String response;

    private Items[] items;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Items[] getItems() {
        return items;
    }

    public void setItems(Items[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", items = " + Arrays.toString(items) + "]";
    }
}

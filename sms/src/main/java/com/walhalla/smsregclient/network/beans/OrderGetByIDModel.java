package com.walhalla.smsregclient.network.beans;

import java.util.Arrays;

/**
 * Created by combo on 24.03.2017.
 */

public class OrderGetByIDModel {
    private String response;

    private ItemsOrderByID[] items;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ItemsOrderByID[] getItems() {
        return items;
    }

    public void setItems(ItemsOrderByID[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", items = "
                + Arrays.toString(items) + "]";
    }
}
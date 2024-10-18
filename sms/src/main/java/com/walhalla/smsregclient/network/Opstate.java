package com.walhalla.smsregclient.network;

import java.io.Serializable;

public enum Opstate implements Serializable {

    COMPLETED("completed"),
    ACTIVE("active");

    private final String value;

    Opstate(String o) {
        this.value = o;
    }

    public String getValue() {
        return value;
    }
}
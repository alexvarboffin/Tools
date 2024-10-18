package com.walhalla.smsregclient.ui;

import android.graphics.drawable.Drawable;

public class TObject {

    public String name;
    public String code;
    public Drawable flag;

    public TObject(String name, String code, Drawable flag) {
        this.name = name;
        this.code = code;
        this.flag = flag;
    }
}
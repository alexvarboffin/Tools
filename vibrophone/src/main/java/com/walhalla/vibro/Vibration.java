package com.walhalla.vibro;

import androidx.annotation.NonNull;

public class Vibration {

    public static final byte TYPE_ALL = Byte.MAX_VALUE;
    public static final byte TYPE_INFINITY = 8;
    public static final byte TYPE_LONG = 4;
    public static final byte TYPE_SERVICE = 1;
    public static final byte TYPE_SHORT = 2;
    public final int id;
    protected String name;
    public final byte type;

    public String getName() {
        return this.name;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public Vibration(int _id, byte _type, String _name) {
        this.id = _id;
        this.type = _type;
        this.name = _name;
    }

    @NonNull
    public String toString() {
        return this.name;
    }
}

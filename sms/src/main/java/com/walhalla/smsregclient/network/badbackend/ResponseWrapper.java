package com.walhalla.smsregclient.network.badbackend;

import androidx.annotation.Nullable;

import com.walhalla.smsregclient.network.beans.APIError;

//import javax.annotation.Nullable;

/**
 * Created by combo on 01.04.2017.
 *
 *
 * Detect - success or error response
 *
 */

public class ResponseWrapper<T> {


    //+
    //protected final Class<T> clazz;

    @Nullable
    protected final APIError error;
    @Nullable
    private final T/*[]*/ data; //OperationBean
    public boolean success;

    public ResponseWrapper(//Class<T> clazz,
                           @Nullable T success, //OperationBean
                           @Nullable APIError error) {

        //this.clazz = clazz;
        this.data = success;
        this.error = error;
    }

    @Nullable
    public APIError getError() {
        return error;
    }

    @Nullable
    public T getData() { //OperationBean
        return data;
    }
    // Add getters and whatever you need

    @Override
    public String toString() {
        return //"Current class:: "+clazz
                //"+", [" + //((data != null) ? Arrays.toString(data) : "") +
                ", " + "error" + ((error != null) ? error.toString() : "") + "]";
    }
}

/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.walhalla.smsregclient.repository;

import com.walhalla.smsregclient.network.response.Balance;


/**
 * Created by combo on 11/15/2017.
 *
 */

public interface Repository {

    interface Callback<T> {
        void successResponse(T data);

        void falseResponse(String message);
    }

    void getBalance(Callback<String> callback);
}

/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.walhalla.smsregclient.repository;

import androidx.annotation.NonNull;

import com.walhalla.smsregclient.Application;
import com.walhalla.smsregclient.helper.PreferencesHelper;
import com.walhalla.smsregclient.network.APIService;
import com.walhalla.smsregclient.network.ErrorUtils;
import com.walhalla.smsregclient.network.badbackend.BaseResponse;
import com.walhalla.smsregclient.network.beans.APIError;
import com.walhalla.smsregclient.network.response.Balance;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by combo on 11/15/2017.
 *
 */

public class SmsRepository implements Repository {


    @Inject
    APIService service;

    @Inject
    PreferencesHelper preferencesHelper;

    public SmsRepository() {
        Application.getComponents().inject(this);
    }

    @Override
    public void getBalance(Callback<String> callback) {
        Call<Balance> task = service.getBalance(preferencesHelper.getApiKey());
        task.enqueue(new retrofit2.Callback<Balance>() {
            @Override
            public void onResponse(@NonNull Call<Balance> call, @NonNull Response<Balance> response) {




                if (response.isSuccessful()) {
                    BaseResponse bm = response.body();
                    if (bm instanceof APIError) {
                        String errorMsg = ((APIError) bm).error;
                        callback.falseResponse(errorMsg);
                    } else if (bm != null) {
                        Balance data = response.body();
                        //tvResponse.setText(data.getBalance());
                        if (data != null) {
                            String balance = (data.balance == null) ? "0" : data.balance;
                            //tvBalance.setText(result);//1 - success
                            callback.successResponse(balance);
                        }
                    }

                } else {
                    //code 200
                    //APIError error = ErrorUtils.parseError(response, retrofit);
                    callback.falseResponse(response.message());
                    //[{"response":"0","error_msg":"No active tranzactions"}]
                }

            }

            @Override
            public void onFailure(@NonNull Call<Balance> call, @NonNull Throwable t) {
                callback.falseResponse(t.getLocalizedMessage());
            }
        });
    }
}

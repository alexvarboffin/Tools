/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.walhalla.smsregclient.presentation.presenter;


import androidx.annotation.NonNull;

import com.walhalla.smsregclient.Application;
import com.walhalla.smsregclient.Const;
import com.walhalla.smsregclient.helper.PreferencesHelper;
import com.walhalla.smsregclient.network.APIService;
import com.walhalla.smsregclient.network.ErrorUtils;
import com.walhalla.smsregclient.network.badbackend.BaseResponse;
import com.walhalla.smsregclient.network.beans.APIError;
import com.walhalla.smsregclient.network.response.VsimGetModel;
import com.walhalla.smsregclient.presentation.view.VsimGetView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;


import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@InjectViewState
public class VsimGetPresenter extends MvpPresenter<VsimGetView> {

    @Inject
    Retrofit retrofit;

    @Inject
    APIService api;

    @Inject
    PreferencesHelper preferencesHelper;

    public VsimGetPresenter() {
        Application.getComponents().inject(this);
    }

    public void vsimGet(Map<String, String> options) {
        Call<VsimGetModel> call = api.vsimGet(
                preferencesHelper.getApiKey(), options);
        call.enqueue(new Callback<VsimGetModel>() {
            @Override
            public void onResponse(@NonNull Call<VsimGetModel> call, @NonNull Response<VsimGetModel> response) {
            /*Log.d("Call request " + call.request().toString());
            Log.d("Call request header " + call.request().headers().toString());
            Log.d("Response raw header " + response.headers().toString());
            Log.d("Response raw" + String.valueOf(response.raw().body()));
            Log.d("Response code " + String.valueOf(response.code()));*/

                if (response.isSuccessful()) {//Always true Response code 200 hence

                    BaseResponse bm = response.body();
                    if (bm instanceof APIError) {
                        String err = ((APIError) bm).error;
                        getViewState().showError(err);
                    } else if (bm != null) {
                        //{response: 1 }
                        VsimGetModel data = (VsimGetModel) bm;
                        if (data.response.equals(Const.RESPONSE_SUCCESS)) {
                            getViewState().showData(data);
                            //DLog.d(data.toString());
                        }
                    }

                    //[response = null, tzid = 33203680]
                    //Log.d(">>>>>>>>>>" + response.errorBody());


                    //APIError error = ErrorUtils.parseError(response);
                    //Log.d(error.getErrorMsg());

                } else {
                    //Log.d("Response errorBody " + String.valueOf(response.body()));
                    APIError error = ErrorUtils.parseError(response, retrofit);
                    getViewState().showError(error.error);
                }

            /*if (response.isSuccessful() && response.body() != null) {
                Log.d("control always here as status code 200 for error condition also");
            } else if (response.errorBody() != null) {
                Log.d("control never reaches here");
            }*/
            }

            @Override
            public void onFailure(@NonNull Call<VsimGetModel> call, @NonNull Throwable t) {
                getViewState().showError(t.getLocalizedMessage());
            }
        });
    }
}

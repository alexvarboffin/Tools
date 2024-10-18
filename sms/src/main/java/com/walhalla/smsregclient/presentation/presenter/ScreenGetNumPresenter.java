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
import com.walhalla.smsregclient.presentation.view.ScreenGetNumView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.walhalla.smsregclient.network.APIService;
import com.walhalla.smsregclient.network.ErrorUtils;
import com.walhalla.smsregclient.network.badbackend.BaseResponse;
import com.walhalla.smsregclient.network.beans.APIError;
import com.walhalla.smsregclient.network.response.NumModel;
import com.walhalla.ui.DLog;


import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@InjectViewState
public class ScreenGetNumPresenter extends MvpPresenter<ScreenGetNumView> {

    @Inject
    Retrofit retrofit;

    @Inject
    APIService api;


    @Inject
    PreferencesHelper preferencesHelper;

    private final Callback<NumModel> aaa = new Callback<NumModel>() {
        /**
         * ####################################################
         * RESPONSE
         * ####################################################
         */
        @Override
        public void onResponse(@NonNull Call<NumModel> call, @NonNull Response<NumModel> response) {

//            DLog.d("Call request " + call.request().toString());
//            DLog.d("Call request header " + call.request().headers().toString());
//            DLog.d("Response raw header " + response.headers().toString());
//            DLog.d("Response raw" + response.raw().body());
//            DLog.d("Response code " + response.code());

            if (response.isSuccessful()) {//Always true Response code 200 hence

                final BaseResponse baseResponse = response.body();
                DLog.d("@" +(baseResponse instanceof APIError)+ baseResponse);

                if (baseResponse instanceof APIError) {
                    String err = ((APIError) baseResponse).error;
                    getViewState().showError(err);

                } else if (baseResponse != null) {
                    //{response: 1 }
                    NumModel data = (NumModel) baseResponse;

                    if (data.response.equals(Const.RESPONSE_SUCCESS)) {
                        getViewState().showData(data);
                        //DLog.d(data.toString());
                    }
//                    else if (data.response.equals(Const.RESPONSE_WARNING_LOW_BALANCE)) {
//                        getViewState().showData(data);
//                        //DLog.d(data.toString());
//                    }


                }

                //[response = null, tzid = 33203680]
                //Log.d(">>>>>>>>>>" + response.errorBody());


                //APIError error = ErrorUtils.parseError(response);
                //Log.d(error.getErrorMsg());

            } else {
                DLog.d("Response errorBody " + response.body());

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
        public void onFailure(@NonNull Call<NumModel> call, @NonNull Throwable t) {
            getViewState().showError(t.getLocalizedMessage());
        }
    };

    public ScreenGetNumPresenter() {
        Application.getComponents().inject(this);
    }


    public void getNum(Map<String, String> options) {
        getViewState().showLoading();
        api.getNum(preferencesHelper.getApiKey(), options).enqueue(aaa);
    }


}

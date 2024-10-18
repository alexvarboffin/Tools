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
import com.walhalla.smsregclient.presentation.view.RateView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.walhalla.smsregclient.network.APIService;
import com.walhalla.smsregclient.network.beans.RateModel;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class RatePresenter extends MvpPresenter<RateView> {

    @Inject
    PreferencesHelper mpm;

    @Inject
    APIService api;

    public RatePresenter() {
        Application.getComponents().inject(this);

        float rate = mpm.getRate();
        getViewState().showRate(rate);
    }


    private Callback<RateModel> callback = new Callback<RateModel>() {
        @Override
        public void onResponse(@NonNull Call<RateModel> call, @NonNull Response<RateModel> response) {
            if (response.isSuccessful()) {
                RateModel data = response.body();

                if (data != null && data.response.equals(Const.RESPONSE_SUCCESS)) {
                    float rate = (data.getRate() == null) ? 0.0f : Float.parseFloat(data.getRate());
                    mpm.setRate(rate);
                    getViewState().successRateResponse(rate);
                }

            }
        }

        @Override
        public void onFailure(@NonNull Call<RateModel> call, @NonNull Throwable t) {
            getViewState().showError(t.getLocalizedMessage());
        }
    };


    public void setRate(String rate) {
        api.setRate(
                mpm.getApiKey(),
                Float.parseFloat(rate)).enqueue(callback);
    }
}

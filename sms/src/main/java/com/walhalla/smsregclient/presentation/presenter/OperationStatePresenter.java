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
import com.walhalla.smsregclient.R;
import com.walhalla.smsregclient.helper.PreferencesHelper;
import com.walhalla.smsregclient.network.APIService;
import com.walhalla.smsregclient.network.badbackend.BaseResponse;
import com.walhalla.smsregclient.network.beans.APIError;
import com.walhalla.smsregclient.network.beans.GetStateModel;
import com.walhalla.smsregclient.network.response.GlobalResponse;
import com.walhalla.smsregclient.network.response.ReadyModel;
import com.walhalla.smsregclient.presentation.view.OperationStateView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class OperationStatePresenter extends MvpPresenter<OperationStateView> {

    private static final String SUCCESS_RESPONSE = String.valueOf(1);

    @Inject
    APIService apiService;

    @Inject
    PreferencesHelper preferencesHelper;

    private String tzid;

    public OperationStatePresenter() {
        Application.getComponents().inject(this);
    }


    public void getNumRepeat() {
        apiService.getNumRepeat(
                preferencesHelper.getApiKey(),
                Integer.parseInt(tzid)).enqueue(globalModelCallback1);
    }

    private Callback<GlobalResponse> globalModelCallback0 = new Callback<GlobalResponse>() {
        @Override
        public void onResponse(@NonNull Call<GlobalResponse> call, @NonNull Response<GlobalResponse> response) {
            if (response.isSuccessful()) {
                BaseResponse bm = response.body();

                if (bm instanceof APIError) {
                    String msg = ((APIError) bm).error;
                    getViewState().showError(msg);
                } else if (bm instanceof GlobalResponse) {
                    //success
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<GlobalResponse> call, @NonNull Throwable t) {

        }
    };


    public void setTzid(String tzid) {
        this.tzid = tzid;
    }

    private Callback<GetStateModel> operationsCallback = new Callback<GetStateModel>() {
        @Override
        public void onResponse(@NonNull Call<GetStateModel> call, @NonNull Response<GetStateModel> response) {


            if (response.isSuccessful()) {
                GetStateModel data = response.body();
                getViewState().showData(data);

            }
        }

        @Override
        public void onFailure(@NonNull Call<GetStateModel> call, @NonNull Throwable t) {
            getViewState().showError(t.getLocalizedMessage());
        }
    };

    private Callback<GlobalResponse> globalModelCallback1 = new Callback<GlobalResponse>() {
        @Override
        public void onResponse(@NonNull Call<GlobalResponse> call, @NonNull Response<GlobalResponse> response) {
            if (response.isSuccessful()) {
                BaseResponse bm = response.body();

                if (bm instanceof APIError) {
                    /*
    0 — повтор по указанной операции невозможен;
    1 — запрос выполнен успешно;
    2 — номер оффлайн, используйте метод getNumRepeatOffline;
    3 — Этот номер сейчас занят. Попробуйте позже.

NEWTZID = id новой операции операции.*/

                    String msg = ((APIError) bm).error;
                    getViewState().showError(msg);

                } else if (bm != null) {

                    GlobalResponse bean = (GlobalResponse) bm;
                    String resp = bean.response;

                    if (null != bean.tzid) {
                        //работаем с тзид
                    } else {
                        int index = Integer.parseInt(bean.response);
                        getViewState().getNumError(index);
                    }

                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<GlobalResponse> call, @NonNull Throwable t) {
            getViewState().showError(t.getLocalizedMessage());
        }
    };
    //
    private Callback<GlobalResponse> globalModelCallback2 = new Callback<GlobalResponse>() {
        @Override
        public void onResponse(@NonNull Call<GlobalResponse> call, @NonNull Response<GlobalResponse> response) {
            if (response.isSuccessful()) {
                BaseResponse bm = response.body();

                if (bm instanceof APIError) {
                    String msg = ((APIError) bm).error;
                    getViewState().showError(msg);
                } else if (bm != null) {

                    GlobalResponse bean = (GlobalResponse) bm;
                    String resp = bean.response;

                    if (null != bean.tzid) {
                        //работаем с тзид
                        //response=1

                        getViewState().showSuccess(R.string.request_successful);
                    } else {
                        //Toast.makeText(getContext(), arr[index], Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<GlobalResponse> call, @NonNull Throwable t) {
            getViewState().showError(t.getLocalizedMessage());
        }
    };
    private Callback<ReadyModel> setReadyCallback = new Callback<ReadyModel>() {
        @Override
        public void onResponse(Call<ReadyModel> call, Response<ReadyModel> response) {
            if (response.isSuccessful()) {
                BaseResponse bm = response.body();

                if (bm instanceof APIError) {
                    String msg = ((APIError) bm).error;
                    getViewState().showError(msg);
                } else if (bm != null) {
                    //{response: 1 }
                    ReadyModel been = (ReadyModel) bm;
                    if (been.response.equals(SUCCESS_RESPONSE)) {
                        getViewState().showSuccess(R.string.request_successful);
                    }
                }

                /*if("ERROR".equals(readyModel.response)){
                    APIError error = ErrorUtils.parseError(response, Application.retrofit());
                    Log.d("###" + error.toString());
                }*/
            }
        }

        @Override
        public void onFailure(@NonNull Call<ReadyModel> call, @NonNull Throwable t) {
            getViewState().showError(t.getLocalizedMessage());
        }
    };


    public void setReady() {
        apiService.setReady(
                preferencesHelper.getApiKey(),
                tzid
        ).enqueue(setReadyCallback);
    }

    public void setOperationOver() {
        apiService.setOperationOver(
                tzid,
                preferencesHelper.getApiKey()
        )
                .enqueue(globalModelCallback0);
    }

    public void setOperationOk() {
        apiService.setOperationOk(
                preferencesHelper.getApiKey(),
                tzid).enqueue(globalModelCallback2);
    }

    public void setOperationUsed() {
        apiService.setOperationUsed(preferencesHelper.getApiKey(),
                tzid).enqueue(globalModelCallback2);
    }

    public void setOperationRevise() {
        apiService.setOperationRevise(preferencesHelper.getApiKey(),
                tzid).enqueue(globalModelCallback2);
    }

    public void getState() {
        apiService.getState(preferencesHelper.getApiKey(),
                tzid).enqueue(operationsCallback);
    }
}

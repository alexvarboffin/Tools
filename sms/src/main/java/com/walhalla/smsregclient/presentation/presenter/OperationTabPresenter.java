

package com.walhalla.smsregclient.presentation.presenter;


import androidx.annotation.NonNull;
import android.view.View;

import com.walhalla.smsregclient.Application;
import com.walhalla.smsregclient.R;

import com.walhalla.smsregclient.helper.PreferencesHelper;
import com.walhalla.smsregclient.network.APIService;
import com.walhalla.smsregclient.network.ErrorUtils;
import com.walhalla.smsregclient.network.Opstate;
import com.walhalla.smsregclient.network.badbackend.ResponseWrapper;
import com.walhalla.smsregclient.network.beans.APIError;
import com.walhalla.smsregclient.network.beans.OperationBean;
import com.walhalla.smsregclient.presentation.view.OperationTabView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.walhalla.smsregclient.ui.adapter.ComplexRecyclerViewAdapter;
import com.walhalla.ui.DLog;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@InjectViewState
public class OperationTabPresenter extends MvpPresenter<OperationTabView>
        implements ComplexRecyclerViewAdapter.ChildItemClickListener
{

    @Inject
    Retrofit retrofit;

    @Inject
    APIService apiService;


    @Inject
    PreferencesHelper preferencesHelper;

    private Opstate opstate;
    private ArrayList<Object> dataset;

    public OperationTabPresenter() {
        Application.getComponents().inject(this);
    }

    public void getOperations() {
        apiService.getOperations(
                preferencesHelper.getApiKey(),
                opstate.getValue(), 100).enqueue(operationsCallback);
    }

    public void setOpstate(@NonNull Opstate opstate) {
        this.opstate = opstate;
    }




    private Callback<ResponseWrapper<OperationBean[]>> operationsCallback =
            new Callback<ResponseWrapper<OperationBean[]>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseWrapper<OperationBean[]>> call,
                                       @NonNull Response<ResponseWrapper<OperationBean[]>> response) {


//                    Log.d("Call request " + call.request().toString());
//                    Log.d("Call request header " + call.request().headers().toString());
//                    Log.d("Response raw header " + response.headers().toString());
//                    Log.d("Response raw" + String.valueOf(response.raw().body()));
//                    Log.d("Response code " + String.valueOf(response.code()));
//                    Log.d("Response body> " + String.valueOf(response.body()));
//                    Log.d("Response errbody>> " + String.valueOf(response.errorBody()));


                    dataset = new ArrayList<>();
                    if (response.isSuccessful()) {

                        ResponseWrapper<OperationBean[]> response1 = response.body();

                        OperationBean[] data = new OperationBean[0];
                        if (response1 != null) {
                            data = response1.getData();
                            APIError error = response1.getError();

                            if (null != data) {
                                dataset.addAll(Arrays.asList(data));
                                //data.add(spinner.getSelectedItem());
                                //data = new ArrayList<>();
                                //data.addAll(response.body().getOperationBean());

                                getViewState().showData(dataset);

                                //List<OperationBean> list = response.body();
                                //Log.d(">>" + list.toString());
                /*for (OperationBean operation : list) {
                    String tzid = operation.getTzid();
                    Log.d(tzid);

                    //=====================================
                Response<GetStateModel> state = App.getApi().getState(tzid).execute();
                if (state.isSuccessful()) {
                    System.out.println(state.body().toString());

                    System.out.println(state.body().toString());
                }
                    //=====================================

                    String number = operation.getPhone();
                    Log.d(operation.getService() + " " + number);
                    Log.d(operation.getAnswer()
                            + " "
                            + hm.get(operation.getStatus()) + "->" + operation.getStatus());
                    Log.d("------------------------------");

                }*/
                            } else if (error != null) {
                                if("ERROR_NO_KEY".equals(error.error)){
                                    error.error = "Укажите API ключ с личного кабинета sms-reg.com\n" +
                                            " в настройках программы";
                                }
                                dataset.add(error);
                                getViewState().showData(dataset);
                            }
                        }

                    } else {
                        //code 200
                        APIError error = ErrorUtils.parseError(response, retrofit);
                        getViewState().showError(response.message());
                        //[{"response":"0","error_msg":"No active tranzactions"}]
                    }


                    //@           Log.d("##" + ScreenOperationTab1.this.hashCode() + dataset.toString());

                }

                @Override
                public void onFailure(@NonNull Call<ResponseWrapper<OperationBean[]>> call, @NonNull Throwable t) {
                    getViewState().showError(R.string.err_server_access);
                }
            };



    @Override
    public void onClick(View v, int position) {

        switch (v.getId()) {
            //case R.id.rl_item://R.id.tv_tzid:
            //    break;
            default:
                //Log.d(dataset.toString());

                Object current = dataset.get(position);
                if (current instanceof OperationBean) {
                    String tzid = ((OperationBean) current).tzid;
                    getViewState().getMoreInfo(tzid);
                }
                break;
        }
    }
}

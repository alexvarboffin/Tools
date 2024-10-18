package com.walhalla.smsregclient.screens.balance;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.walhalla.smsregclient.R;
import com.walhalla.smsregclient.databinding.ScreenBalanceBinding;
import com.walhalla.smsregclient.helper.PreferencesHelper;
import com.walhalla.smsregclient.network.APIService;
import com.walhalla.smsregclient.network.badbackend.BaseResponse;
import com.walhalla.smsregclient.network.beans.APIError;
import com.walhalla.smsregclient.network.response.Balance;
import com.walhalla.smsregclient.core.BaseFragment;
import com.walhalla.smsregclient.ui.adapter.ComplexRecyclerViewAdapter;

import javax.inject.Inject;


import androidx.databinding.DataBindingUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ScreenBalance extends BaseFragment implements ComplexRecyclerViewAdapter.ChildItemClickListener {


    private ScreenBalanceBinding mBinding;

    @Inject
    APIService apiService;

    @Inject
    PreferencesHelper preferencesHelper;

    private Callback<Balance> callback = new Callback<Balance>() {
        @Override
        public void onResponse(@NonNull Call<Balance> call, @NonNull Response<Balance> response) {

            if (response.isSuccessful()) {
                BaseResponse bm = response.body();

                if (bm instanceof APIError) {
                    Toast.makeText(getContext(), ((APIError) bm).error, Toast.LENGTH_SHORT).show();
                } else if (bm != null) {
                    Balance data = response.body();
                    //tvResponse.setText(data.getBalance());

                    if (data != null) {
                        String b = (data.balance == null) ? "" : data.balance;
                    }
                    //String result = String.format(Locale.getDefault(), "%.2f", Float.valueOf(b));
                    //tvBalance.setText(result);//1 - success
                }
            } else {
                //code 200
                //APIError error = ErrorUtils.parseError(response, retrofit);
                //callback.onFailure(response.message());
                //[{"response":"0","error_msg":"No active tranzactions"}]
            }

        }

        @Override
        public void onFailure(@NonNull Call<Balance> call, @NonNull Throwable t) {

        }
    };

    public static ScreenBalance newInstance() {
        return new ScreenBalance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.screen_balance, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.modifyContainer(getString(R.string.title_balance), false);
        Call<Balance> task = apiService.getBalance(preferencesHelper.getApiKey());
        task.enqueue(callback);
    }


    @Override
    public void onClick(View v, int position) {

    }
}
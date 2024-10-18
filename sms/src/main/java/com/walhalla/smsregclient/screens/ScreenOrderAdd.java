package com.walhalla.smsregclient.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.walhalla.smsregclient.Application;
import com.walhalla.smsregclient.Const;
import com.walhalla.smsregclient.R;
import com.walhalla.smsregclient.databinding.FragmentOrderAddBinding;
import com.walhalla.smsregclient.helper.PreferencesHelper;
import com.walhalla.smsregclient.network.APIService;
import com.walhalla.smsregclient.network.badbackend.BaseResponse;
import com.walhalla.smsregclient.network.beans.APIError;
import com.walhalla.smsregclient.network.response.OrderAddModel;
import com.walhalla.smsregclient.core.BaseFragment;
import com.walhalla.ui.DLog;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


import androidx.databinding.DataBindingUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScreenOrderAdd extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    private FragmentOrderAddBinding mBinding;

    @Inject
    PreferencesHelper preferencesHelper;

    private APIService service;
    private Map<String, String> options;
    private AdapterView.OnItemSelectedListener genderListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String current = (String) parent.getSelectedItem();
            //Log.d(current.getKey());
            //Toast.makeText(getContext(), "TObject ID: " + current.getKey()
            //        + ",  TObject Name : " + current.getValue(), Toast.LENGTH_SHORT).show();
            options.put("gender", current);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private Callback<OrderAddModel> addOrderCallback = new Callback<OrderAddModel>() {
        @Override
        public void onResponse(@NonNull Call<OrderAddModel> call, @NonNull Response<OrderAddModel> response) {
            if (response.isSuccessful()) {

                //{"response":"ERROR","error_msg":"Wrong count value"}


                BaseResponse bm = response.body();

                if (bm instanceof APIError) {
                    Toast.makeText(getContext(), ((APIError) bm).error, Toast.LENGTH_SHORT).show();
                    DLog.d("Error: " + bm.toString());
                } else if (bm != null) {
                    OrderAddModel bean = (OrderAddModel) bm;
                    DLog.d("Success: " + bean.toString());

                    if (bean.response.equals(Const.RESPONSE_SUCCESS)) {
                        String id = bean.getOrder_id();//id заказа
                    }
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<OrderAddModel> call, @NonNull Throwable throwable) {
            showError(throwable.getLocalizedMessage());
        }
    };

    public ScreenOrderAdd() {
        Application.getComponents().inject(this);
    }

    private void showError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private AdapterView.OnItemSelectedListener selectedListener;


    private String[] countriesCodes;
    private String[] servicesCodes;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_add, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.btnOrderAdd.setOnClickListener(v -> {
            options.put("name", mBinding.inputName.getText().toString());
            options.put("age", mBinding.inputAge.getText().toString());
            options.put("city", mBinding.inputCity.getText().toString());
            options.put("count", mBinding.inputCount.getText().toString());

            DLog.d(options.toString());
            service.orderAdd(
                    preferencesHelper.getApiKey(),
                    options).enqueue(addOrderCallback);
        });

        this.modifyContainer(getString(R.string.title_order_add), false);

        options = new HashMap<>();
        selectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String code = "";
                int parentId = parent.getId();
                if (parentId == R.id.country) {
                    code = countriesCodes[position];
                    options.put("country", code);
                } else if (parentId == R.id.service) {
                    code = servicesCodes[position];
                    options.put("service", code);
                }

                Toast.makeText(getContext(), code, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        countriesCodes = getResources().getStringArray(R.array.country_codes);
        servicesCodes = getResources().getStringArray(R.array.service_codes);

        final String[] countries = getResources().getStringArray(R.array.country_names);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item, countries);

        mBinding.country.setAdapter(adapter);
        mBinding.country.setOnItemSelectedListener(selectedListener);

        String handler = getString(R.string.service_handler);
        String[] array = getResources().getStringArray(R.array.service_names);
        for (int i = 0; i < array.length; i++) {
            array[i] = String.format(handler, array[i]);
        }

        ArrayAdapter<String> adapterService = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, array);
        mBinding.service.setAdapter(adapterService);
        mBinding.service.setOnItemSelectedListener(selectedListener);

        //gender
        ArrayList<String> genderList = new ArrayList<>();
        genderList.add("male");
        genderList.add("female");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, genderList);
        mBinding.sGender.setAdapter(adp);
        //dropdownGender.setSelection(adapter.getPosition("###"));
        mBinding.sGender.setOnItemSelectedListener(genderListener);
        mBinding.cbOptions.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        options.put("options", (isChecked) ? "on" : "");
        enableExtarnalOptions(isChecked);
    }

    private void enableExtarnalOptions(boolean isChecked) {
        mBinding.inputAge.setEnabled(isChecked);
        mBinding.inputCity.setEnabled(isChecked);
        mBinding.inputName.setEnabled(isChecked);
        mBinding.sGender.setEnabled(isChecked);
    }

    /*private OnItemSelectedListener typeSelectedListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            Log.d(TAG, "user selected : "
                    + typeSpinner.getSelectedItem().toString());

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private OnTouchListener typeSpinnerTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            selected = true;
            ((BaseAdapter) typeSpinnerAdapter).notifyDataSetChanged();
            return false;
        }
    };*/
}

/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.walhalla.smsregclient.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.walhalla.smsregclient.Application;
import com.walhalla.smsregclient.Const;
import com.walhalla.smsregclient.R;
import com.walhalla.smsregclient.Utils;
import com.walhalla.smsregclient.core.BaseFragment;
import com.walhalla.smsregclient.databinding.ScreenVsimGetBinding;
import com.walhalla.smsregclient.helper.PreferencesHelper;
import com.walhalla.smsregclient.network.response.VsimGetModel;
import com.walhalla.smsregclient.presentation.presenter.VsimGetPresenter;
import com.walhalla.smsregclient.presentation.view.VsimGetView;
import com.walhalla.smsregclient.ui.adapter.CustomSpinnerAdapter;
import com.walhalla.smsregclient.ui.adapter.NothingSelectedSpinnerAdapter;
import com.walhalla.smsregclient.ui.adapter.SpinnerItem;
import com.walhalla.smsregclient.ui.dialog.CountrycodeActivity;
import com.walhalla.smsregclient.ui.dialog.ServiceCodeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class VsimGetFragment extends BaseFragment implements VsimGetView, View.OnClickListener {


    private static final String TAG_COUNTRY = "@country";
    private static final String TAG_PERIOD = "@period";

    @Inject
    PreferencesHelper preferencesHelper;
    private ScreenVsimGetBinding mBinding;

    private List<SpinnerItem> spinnerItems1, spinnerItems2;


    private boolean isStarted = false;


    //Request data
    private Map<String, String> options;


    @InjectPresenter
    VsimGetPresenter presenter;
    private VsimGetModel data;

    public VsimGetFragment() {
        Application.getComponents().inject(this);
    }


    public static VsimGetFragment newInstance() {
        VsimGetFragment fragment = new VsimGetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.screen_vsim_get, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            String tzid = savedInstanceState.getString(Const.ARG_TZID, "#");
            mBinding.number.setText(tzid);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //R.id.btn_select_country,
        //R.id.btn_select_service,
        mBinding.btnGetNum.setOnClickListener(this);
        mBinding.number.setOnClickListener(this);


        options = new HashMap<>();

        this.modifyContainer(getString(R.string.title_vsim_get), false);

        String[] countryValues = getResources().getStringArray(R.array.vsim_country_names);
        String[] countryCodes = getResources().getStringArray(R.array.vsim_country_codes);

        String[] periodValues = getResources().getStringArray(R.array.vsim_period_name);
        String[] periodCodes = getResources().getStringArray(R.array.vsim_period_code);

        spinnerItems1 = new ArrayList<>();
        spinnerItems2 = new ArrayList<>();

        for (int i = 0; i < countryValues.length; i++) {
            spinnerItems1.add(new SpinnerItem(countryValues[i], countryCodes[i]));
        }

        for (int i = 0; i < periodValues.length; i++) {
            spinnerItems2.add(new SpinnerItem(periodValues[i], periodCodes[i]));
        }


        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), R.layout.spinner_activity_countrycode, spinnerItems1);
        mBinding.country.setAdapter(adapter);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.country.setPrompt(getString(R.string.prompt_select_country));

        mBinding.country.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter, R.layout.spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getContext(), getString(R.string.prompt_select_country)));


        mBinding.country.setSelection(preferencesHelper.getSelected(R.id.country));

        adapter = new CustomSpinnerAdapter(getActivity(), R.layout.spinner_activity_countrycode, spinnerItems2);
        mBinding.service.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter, R.layout.spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getContext(), getString(R.string.lease_term_handler)));
        mBinding.service.setPrompt(getString(R.string.lease_term_handler));
        mBinding.service.setSelection(preferencesHelper.getSelected(R.id.service));
        mBinding.country.setOnItemSelectedListener(spinnerItemSelectListener);
        mBinding.service.setOnItemSelectedListener(spinnerItemSelectListener);
        isStarted = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (data == null) {
            mBinding.number.setText(R.string.empty_number);
        } else {
            mBinding.number.setText(data.number);
        }
    }

    AdapterView.OnItemSelectedListener spinnerItemSelectListener = new AdapterView.OnItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position <= 0) return;
            String selected = "";

            int viewId = parent.getId();
            if (viewId == R.id.country) {
                selected = //item.getCode(); //
                        spinnerItems1.get(position - 1).getCode();
                preferencesHelper.putSelected(viewId, position);
                options.put(TAG_COUNTRY, selected);
            } else if (viewId == R.id.service) {
                selected = spinnerItems2.get(position - 1).getCode();
                preferencesHelper.putSelected(viewId, position);
                options.put(TAG_PERIOD, selected);
            }

            //if (isStarted) Toast.makeText(getContext(), selected, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }


    // Get Result Back
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String countryCode = data.getStringExtra(CountrycodeActivity.RESULT_COUNTRY_CODE);
            options.put(TAG_COUNTRY, countryCode);
            Toast.makeText(getContext(), "Номер: " + countryCode, Toast.LENGTH_SHORT).show();
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            String service = data.getStringExtra(ServiceCodeActivity.RESULT_SERVICE_CODE);
            options.put(TAG_PERIOD, service);
            Toast.makeText(getContext(), "Сервис: " + service, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Const.ARG_TZID, mBinding.number.getText().toString());
    }

    @Override
    public void showError(String errorMsg) {
        mListener.makeSnack(errorMsg);
    }

    @Override
    public void showError(int err) {
        showError(getString(err));
    }

    @Override
    public void showData(@NonNull VsimGetModel data) {
        this.data = data;
        if (this.data.response.equals(Const.RESPONSE_SUCCESS)) {
            mBinding.response.setText(R.string.dedicated_virtual_number);
            mBinding.number.setVisibility(View.VISIBLE);
            mBinding.number.setText(data.number);
            mBinding.start.setText(getString(R.string.time_to_start_renting, data.getStart()));
            mBinding.stop.setText(getString(R.string.rent_end_time, data.getStop()));
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        int id = view.getId();/*case R.id.btn_select_country:
                Intent intent = new Intent(getContext(), CountrycodeActivity.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.btn_select_service:
                intent = new Intent(getContext(), ServiceCodeActivity.class);
                startActivityForResult(intent, 2);
                break;*/
        if (id == R.id.btn_get_num) {//if (options.get("country").isEmpty()) {
            //    Toast.makeText(getContext(), "", Toast.LENGTH_LONG).show();
            //} else
            if (options.get(TAG_PERIOD) == null) {
                Toast.makeText(getContext(), R.string.choose_rental_period, Toast.LENGTH_SHORT).show();
            } else {
                presenter.vsimGet(options);

            }
        } else if (id == R.id.number) {//mListener.replaceFragment(OperationStateFragment.newInstance(number.getText().toString()));
            if (this.data != null && getContext() != null) {
                Utils.copyToClipboard(getContext(), mBinding.number.getText().toString());
            }
        }
    }
}

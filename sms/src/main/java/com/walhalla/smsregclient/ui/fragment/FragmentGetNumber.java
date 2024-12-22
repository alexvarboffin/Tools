

package com.walhalla.smsregclient.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.walhalla.smsregclient.Application;
import com.walhalla.smsregclient.Const;
import com.walhalla.smsregclient.core.BaseFragment;
import com.walhalla.smsregclient.databinding.FragmentGetNumberBinding;
import com.walhalla.smsregclient.helper.PreferencesHelper;
import com.walhalla.smsregclient.presentation.view.ScreenGetNumView;
import com.walhalla.smsregclient.presentation.presenter.ScreenGetNumPresenter;


import com.walhalla.smsregclient.R;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.walhalla.smsregclient.network.response.NumModel;

import com.walhalla.smsregclient.ui.adapter.CustomSpinnerAdapter;
import com.walhalla.smsregclient.ui.adapter.NothingSelectedSpinnerAdapter;
import com.walhalla.smsregclient.ui.adapter.SpinnerItem;
import com.walhalla.smsregclient.ui.dialog.CountrycodeActivity;
import com.walhalla.smsregclient.ui.dialog.ServiceCodeActivity;
import com.walhalla.ui.DLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


import static android.widget.Toast.LENGTH_SHORT;


/**
 * Get Number for services
 */
public class FragmentGetNumber extends BaseFragment implements ScreenGetNumView, View.OnClickListener {


    @Inject
    PreferencesHelper mPref;
    private FragmentGetNumberBinding mBinding;


    private boolean isStarted = false;


    //Request data
    private Map<String, String> options;


    @InjectPresenter
    ScreenGetNumPresenter mScreenGetNumPresenter;
    private List<SpinnerItem> spinnerItems1;
    private List<SpinnerItem> spinnerItems2;

    public FragmentGetNumber() {
        Application.getComponents().inject(this);
    }


    public static FragmentGetNumber newInstance() {
        FragmentGetNumber fragment = new FragmentGetNumber();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_number, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //R.id.btn_select_country,
        //R.id.btn_select_service,
        mBinding.btnGetNum.setOnClickListener(this);
        mBinding.tzid.setOnClickListener(this);

        options = new HashMap<>();

        this.modifyContainer(getString(R.string.title_get_num), false);
        String handler = getString(R.string.service_handler);

        String[] countryValues = getResources().getStringArray(R.array.country_names);
        String[] countryCodes = getResources().getStringArray(R.array.country_codes);

        String[] array = getResources().getStringArray(R.array.service_names);
        String[] serviceCodes = getResources().getStringArray(R.array.service_codes);


        spinnerItems1 = new ArrayList<>();
        spinnerItems2 = new ArrayList<>();

        for (int i = 0; i < countryValues.length; i++) {
            spinnerItems1.add(new SpinnerItem(countryValues[i], countryCodes[i]));
        }

        for (int i = 0; i < array.length; i++) {
            spinnerItems2.add(new SpinnerItem(String.format(handler, array[i]), serviceCodes[i]));
        }

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(),
                R.layout.spinner_activity_countrycode, spinnerItems1);
        mBinding.country.setAdapter(adapter);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.country.setPrompt(getString(R.string.prompt_select_country));

        mBinding.country.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter, R.layout.spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getContext(), getString(R.string.prompt_select_country)));


        adapter = new CustomSpinnerAdapter(getActivity(), R.layout.spinner_activity_countrycode, spinnerItems2);
        mBinding.service.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter, R.layout.spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getContext(), getString(R.string.prompt_select_service)));
        mBinding.service.setPrompt(getString(R.string.prompt_select_service));


        loadState();

        mBinding.country.setOnItemSelectedListener(spinnerItemSelectListener);
        mBinding.service.setOnItemSelectedListener(spinnerItemSelectListener);


        isStarted = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            String tzid = savedInstanceState.getString("tzid", "#");
            mBinding.tzid.setText(tzid);
        }

    }

    private void loadState() {
        mBinding.service.setSelection(mPref.getSelected(77));
        mBinding.country.setSelection(mPref.getSelected(78));
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
                mPref.putSelected(78, position);
                options.put(Const.ARG_COUNTY, selected);
            } else if (viewId == R.id.service) {
                selected = spinnerItems2.get(position - 1).getCode();
                mPref.putSelected(77, position);
                options.put(Const.ARG_SERVICE, selected);
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
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }


    // Get Result Back
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String countryCode = data.getStringExtra(CountrycodeActivity.RESULT_COUNTRY_CODE);
            options.put(Const.ARG_COUNTY, countryCode);
            Toast.makeText(getContext(), "Номер: " + countryCode, LENGTH_SHORT).show();
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            String service = data.getStringExtra(ServiceCodeActivity.RESULT_SERVICE_CODE);
            options.put(Const.ARG_SERVICE, service);
            Toast.makeText(getContext(), getString(R.string.service_handler_2, service), LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tzid", mBinding.tzid.getText().toString());
    }

    @Override
    public void showError(String errorMsg) {
        mListener.makeSnack("" + errorMsg);
    }

    @Override
    public void showError(int err) {
        showError(getString(err));
    }

    @Override
    public void showData(NumModel data) {
        mBinding.response.setText(R.string.operation_id);
        mBinding.tzid.setVisibility(View.VISIBLE);

        String tmp = (data.tzid == null || data.tzid.isEmpty()) ? "(Empty)" : data.tzid;
        mBinding.tzid.setText(tmp);
    }

    @Override
    public void showLoading() {
        DLog.d("000000");
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
            if (options.get(Const.ARG_SERVICE) == null) {
                Toast.makeText(getContext(), getString(R.string.err_select_service), LENGTH_SHORT).show();
            } else {
                mScreenGetNumPresenter.getNum(options);
            }
        } else if (id == R.id.tzid) {
            mListener.replaceFragment(OperationStateFragment.newInstance(mBinding.tzid.getText().toString()));
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.findItem(R.id.action_get_num).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        //showData(new NumModel());
    }
}

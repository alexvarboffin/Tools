/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.walhalla.smsregclient.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.walhalla.smsregclient.core.BaseFragment;

import com.walhalla.smsregclient.databinding.FragmentOperationTabBinding;
import com.walhalla.smsregclient.network.Opstate;
import com.walhalla.smsregclient.network.beans.APIError;
import com.walhalla.smsregclient.presentation.view.OperationTabView;
import com.walhalla.smsregclient.presentation.presenter.OperationTabPresenter;


import com.walhalla.smsregclient.R;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.walhalla.smsregclient.ui.adapter.ComplexRecyclerViewAdapter;
import com.walhalla.smsregclient.ui.common.DividerItemDecoration;
import com.walhalla.ui.DLog;

import java.util.List;


public class OperationTabFragment extends BaseFragment implements OperationTabView {

    @InjectPresenter
    OperationTabPresenter mOperationTabPresenter;
    private ComplexRecyclerViewAdapter adapter;

    private static final String ARG_OPSTATE = "oper_state";

    private FragmentOperationTabBinding mBinding;

    public static OperationTabFragment newInstance(Opstate type) {
        OperationTabFragment fragment = new OperationTabFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_OPSTATE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_operation_tab, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseInstance(savedInstanceState);


        if (adapter == null) {
            adapter = new ComplexRecyclerViewAdapter(getContext());
        }
        adapter.setChildItemClickListener(mOperationTabPresenter);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //onresponse
        //recyclerView.setAdapter(new ComplexRecyclerViewAdapter(null, getContext()));
        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Balance balance = (Balance) parent.getAdapter().getItem(position);


                api.getOperations(
                        opstate//"completed"
                        , 100).enqueue(operationsCallback);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        //spinner.setEnabled(false);
        mBinding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        mBinding.recyclerView.setLayoutManager(manager);
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            mOperationTabPresenter.getOperations();
            //mOperationTabPresenter.getOperations(opstate);

            // Stop refresh animation
            mBinding.swipeRefreshLayout.setRefreshing(false);
        });
        mBinding.recyclerView.setAdapter(adapter);

        mOperationTabPresenter.getOperations();
        //mOperationTabPresenter.getOperations(opstate);

        // Stop refresh animation
        mBinding.swipeRefreshLayout.setRefreshing(false);
        showError(getString(R.string.app_name) + "\n" + DLog.getAppVersion(getContext()));
    }

    private void parseInstance(Bundle savedInstanceState) {

        Opstate opstate;
        if (savedInstanceState != null) {
            opstate = (Opstate) savedInstanceState.getSerializable(ARG_OPSTATE);
        } else if (null != getArguments()) {
            opstate = (Opstate) getArguments().getSerializable(ARG_OPSTATE);
        } else {
            throw new RuntimeException();
        }

        if (opstate != null) {
            mOperationTabPresenter.setOpstate(opstate);
        }
    }


    //From Presenter

    @Override
    public void getMoreInfo(String tzid) {
        mListener.replaceFragment(OperationStateFragment.newInstance(tzid));
    }

    @Override
    public void showError(String errorMsg) {
        APIError err = new APIError();
        err.error = errorMsg;
        adapter.swap(err);

        //Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(int err) {
        showError(getString(err));
    }

    @Override
    public void showData(List<Object> data) {
        adapter.swap(data);
    }


}

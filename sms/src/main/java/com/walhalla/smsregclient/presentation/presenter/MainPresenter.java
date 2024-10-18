/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.walhalla.smsregclient.presentation.presenter;

import com.walhalla.smsregclient.Application;
import com.walhalla.smsregclient.presentation.view.MainView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.walhalla.smsregclient.repository.Repository;


import javax.inject.Inject;


@InjectViewState
public class MainPresenter extends MvpPresenter<MainView>  {

    @Inject
    Repository repository;

    public MainPresenter() {
        Application.getComponents().inject(this);
    }

    public void balanceRequest() {
        repository.getBalance(new Repository.Callback<String>() {
            @Override
            public void successResponse(String data) {
                getViewState().showUserBalance(data);
            }

            @Override
            public void falseResponse(String message) {
                getViewState().successData(message);
            }
        });
    }


}

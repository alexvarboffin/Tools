

package com.walhalla.smsregclient.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.walhalla.smsregclient.network.response.NumModel;

public interface ScreenGetNumView extends MvpView {
    void showError(String errorMsg);

    void showError(int err);

    void showData(NumModel data);

    void showLoading();
}

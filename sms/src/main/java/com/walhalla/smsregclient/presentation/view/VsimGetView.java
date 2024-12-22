

package com.walhalla.smsregclient.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.walhalla.smsregclient.network.response.VsimGetModel;

public interface VsimGetView extends MvpView {

    void showError(String err);

    void showError(int err);

    void showData(VsimGetModel data);
}

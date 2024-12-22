

package com.walhalla.smsregclient.presentation.view;

import com.arellomobile.mvp.MvpView;

public interface RateView extends MvpView {

    void successRateResponse(float rate);

    void showError(String localizedMessage);

    void showRate(float rate);
}

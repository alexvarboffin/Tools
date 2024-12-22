

package com.walhalla.smsregclient.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface MainView extends MvpView {

    @StateStrategyType(SkipStrategy.class)
    void showUserBalance(String b);

    @StateStrategyType(SkipStrategy.class)
    void makeSnack(String msg);

    void successData(String errorMsg);
}

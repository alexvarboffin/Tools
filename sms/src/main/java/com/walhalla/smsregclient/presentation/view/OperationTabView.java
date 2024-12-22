

package com.walhalla.smsregclient.presentation.view;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

public interface OperationTabView extends BaseView {

    @StateStrategyType(SkipStrategy.class)
    void getMoreInfo(String tzid);//Navigation


    void showData(List<Object> data);
}

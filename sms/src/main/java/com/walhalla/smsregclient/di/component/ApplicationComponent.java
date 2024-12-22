

package com.walhalla.smsregclient.di.component;


import com.walhalla.smsregclient.Application;
import com.walhalla.smsregclient.di.module.AnalyticsModule;
import com.walhalla.smsregclient.di.module.ApplicationModule;
import com.walhalla.smsregclient.di.module.NetworkModule;
import com.walhalla.smsregclient.presentation.presenter.MainPresenter;
import com.walhalla.smsregclient.presentation.presenter.OperationStatePresenter;
import com.walhalla.smsregclient.presentation.presenter.OperationTabPresenter;
import com.walhalla.smsregclient.presentation.presenter.RatePresenter;
import com.walhalla.smsregclient.presentation.presenter.ScreenGetNumPresenter;
import com.walhalla.smsregclient.presentation.presenter.VsimGetPresenter;
import com.walhalla.smsregclient.repository.SmsRepository;

import com.walhalla.smsregclient.screens.ScreenOrderAdd;
import com.walhalla.smsregclient.ui.dialog.KeyDialogFragment;
import com.walhalla.smsregclient.ui.fragment.OperationTabFragment;
import com.walhalla.smsregclient.ui.fragment.FragmentGetNumber;
import com.walhalla.smsregclient.ui.fragment.VsimGetFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class, AnalyticsModule.class})
public interface ApplicationComponent {
    void inject(Application application);

    void inject(MainPresenter mainPresenter);

    void inject(SmsRepository smsRegRepo);


    void inject(OperationTabFragment operationTabFragment);

    void inject(RatePresenter ratePresenter);

    void inject(FragmentGetNumber fragmentGetNumber);

    void inject(KeyDialogFragment apiKeyDialog);

    void inject(OperationStatePresenter operationStatePresenter);

    void inject(OperationTabPresenter operationTabPresenter);

    void inject(ScreenGetNumPresenter screenGetNumPresenter);

    void inject(VsimGetFragment vsimGetNumFragment);

    void inject(VsimGetPresenter vsimGetPresenter);

    void inject(ScreenOrderAdd screenOrderAdd);
}

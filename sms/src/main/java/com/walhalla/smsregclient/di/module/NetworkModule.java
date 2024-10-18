/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.walhalla.smsregclient.di.module;

import android.content.Context;

import com.walhalla.smsregclient.helper.PreferencesHelper;
import com.walhalla.smsregclient.network.NetworkProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;


/**
 * Created by combo on 11/16/2017.
 *
 */
@Module
public class NetworkModule {


    @Provides
    @Singleton
    NetworkProvider provideNetworkProvider(PreferencesHelper preferencesHelper, Context context) {
        return new NetworkProvider(preferencesHelper, context);
    }


    @Provides
    @Singleton
    Retrofit provideRetrofit(NetworkProvider networkProvider){
        return networkProvider.getmRetrofit();
    }
}

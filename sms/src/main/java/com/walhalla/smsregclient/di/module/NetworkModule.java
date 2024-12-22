

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

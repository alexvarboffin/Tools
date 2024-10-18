package com.walhalla.smsregclient.di.module;

import android.content.Context;
import androidx.annotation.NonNull;

import com.walhalla.smsregclient.Application;
import com.walhalla.smsregclient.helper.PreferencesHelper;
import com.walhalla.smsregclient.repository.Repository;
import com.walhalla.smsregclient.repository.SmsRepository;
import com.walhalla.smsregclient.network.APIService;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by combo on 11/15/2017.
 *
 */
@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(@NonNull Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }


    @Provides
    @Singleton
    public PreferencesHelper providePreferencesHelper(Context context) {
        return new PreferencesHelper(context);
    }


    @Provides
    @Singleton
    public Repository provideRepository() {
        return new SmsRepository();
    }



    @Provides
    @Singleton
    public APIService provideAPIService(Retrofit retrofit) {
        return retrofit.create(APIService.class);
    }
}

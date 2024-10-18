package com.walhalla.smsregclient;

import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;
import com.walhalla.smsregclient.di.component.ApplicationComponent;
import com.walhalla.smsregclient.di.component.DaggerApplicationComponent;
import com.walhalla.smsregclient.di.module.ApplicationModule;

public class Application extends MultiDexApplication {

    private static ApplicationComponent var0;

    public static ApplicationComponent getComponents() {
        return var0;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //import io.fabric.sdk.android.Fabric.with(this, new Crashlytics());
        //MobileAds.initialize(this, getString(R.string.app_id));
        //Logger.addLogAdapter(new AndroidLogAdapter());

        var0 = buildComponent();
        var0.inject(this);
    }

    private ApplicationComponent buildComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}

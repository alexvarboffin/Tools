package com.walhalla.qrcode.ui.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import com.walhalla.qrcode.BuildConfig;
import com.walhalla.qrcode.R;
import com.walhalla.ui.observer.RateAppModule;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    private RateAppModule var1;
    private AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);

        var1 = new RateAppModule(this);
        getLifecycle().addObserver(var1);
        // Initialize the Mobile Ads SDK.

        //adView = findViewById(R.id.b1);
        //adView.setAdUnitId(AD_UNIT_ID); //banner b1
        //adView.setAdSize(AdSize.SMART_BANNER);
        initializeAdMobAndConfigureTestDevices(this);

        AdRequest mm = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("28964E2506C9A8C6400A9E8FF42D3486")
                .build();
        adView.loadAd(mm);
    }

    public static void initializeAdMobAndConfigureTestDevices(Context context) {
        List<String> list = new ArrayList<>();
        if (!BuildConfig.DEBUG) {
            list.add(AdRequest.DEVICE_ID_EMULATOR);
            list.add("A8A2F7804653E219880030864C1F32E4");
            list.add("5D5A89BC6372A49242D138B9AC352894");
            list.add("A2A86E2966898F258CB671EE038C2703");
            list.add("8CAA90D84051B086BE2AE92278905B28");
            list.add("5ABD38516C43C42937310610289DFB8E");
        }
        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(list)
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);
        MobileAds.initialize(context, initializationStatus -> {
            //getString(R.string.app_id)
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (var1 != null) {
            var1.appReloadedHandler();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}

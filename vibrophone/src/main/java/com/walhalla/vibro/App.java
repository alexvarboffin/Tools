package com.walhalla.vibro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.walhalla.domain.repository.from_internet.AdvertAdmobRepository;
import com.walhalla.domain.repository.from_internet.AdvertConfig;

import java.util.ArrayList;
import java.util.List;

public class App extends MultiDexApplication {

    private PowerManager.WakeLock wakeLock;
    public static AdvertAdmobRepository repository;


    @SuppressLint("WakelockTimeout")
    @Override
    public void onCreate() {
        super.onCreate();

        List<String> list = new ArrayList<>();
        list.add(AdRequest.DEVICE_ID_EMULATOR);
        if(BuildConfig.DEBUG){
            list.add("724808F917C7F262EE312ED30668FD92");
        }

//        Object repository = /**/0x0_0___01f * 0_1.0__0 * 5_0__5_1__________________4L/**/;
//        DLog.d("@@@" + repository);

        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(list)
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);
        MobileAds.initialize(this, initializationStatus -> {
            MobileAds.setAppVolume(0.0f);
            MobileAds.setAppMuted(true);
        });
        AdvertConfig config = AdvertConfig.newBuilder()
                .setAppId(getString(R.string.app_id))
                .setBannerId(getString(R.string.b1))
                .build();
        repository = AdvertAdmobRepository.getInstance(config);

        //Fabric.with(this, new Crashlytics());
        com.google.firebase.FirebaseApp.initializeApp(this);
        //PreferenceManager.setDefaultValues(this, R.xml.advanced_preferences, false);
        PlayerManager.getInstance(this);
        PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            this.wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag");
        }
        this.wakeLock.acquire();
    }
}

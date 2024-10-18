package com.walhalla.qrcode.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.walhalla.qrcode.R;
import com.walhalla.qrcode.helpers.util.SharedPrefUtil;
import com.walhalla.qrcode.helpers.util.database.DatabaseUtil;
import com.walhalla.qrcode.ui.activity.BaseActivity;

import com.walhalla.ui.DLog;
import com.walhalla.wads.AppOpenAdManager;
import com.walhalla.wads.OnShowAdCompleteListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RunApp extends androidx.multidex.MultiDexApplication
        implements Application.ActivityLifecycleCallbacks
        //, LifecycleObserver
        , DefaultLifecycleObserver {

    private static final boolean ACTIVITY_MOVES_TO_FOREGROUND_HANDLE = true;

    private AppOpenAdManager appOpenAdManager;
    private Activity currentActivity;

    public static final int NB_VIDEOS_BEFORE_AD = 3;
    public static final int SECONDS_TO_COUNT_VIDEOS = 5;
    //private static final String ONESIGNAL_APP_ID = "8fcf9ed0-0064-49ee-9d27-aca9bb3bd972";

    private static RunApp sInstance;
    private int intersticialCounter = 0;

    private boolean disableWads;


    public boolean hasToDisplayIntersticiel() {
        return this.intersticialCounter >= 3;
    }

    public void incIntersticialCounter() {
        DLog.d("Counter " + this.intersticialCounter);
        this.intersticialCounter++;
    }

    public void resetIntersticialCounter() {
        this.intersticialCounter = 0;
    }

    public int getSecondsBeforeIntersticial() {
        return 5000;
    }


//    public static synchronized RunApp getInstance() {
//        RunApp aaaaa;
//        synchronized (RunApp.class) {
//            aaaaa = sInstance;
//        }
//        return aaaaa;
//    }


//rts
//    public void setConnectivityListener(NetworkStateReceiver.NetworkStateReceiverListener listener)
//    {
//        NetworkStateReceiver.networkStateReceiverListener = listener;
//    }

//Valley
//    private RequestQueue mRequestQueue;
//
//    public ImageLoader getmImageLoader() {
//        return mImageLoader;
//    }

//    private final ImageLoader mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
//
//        private final LruCache<String, Bitmap> cache = new LruCache<>(20);
//
//        @Override
//        public Bitmap getBitmap(String url) {
//            return this.cache.get(url);
//        }
//
//        @Override
//        public void putBitmap(String url, Bitmap bitmap) {
//            this.cache.put(url, bitmap);
//        }
//    });


//    public RequestQueue getRequestQueue() {
//        return mRequestQueue;
//    }


    //public AdvertAdmobRepository mR;

    public static Context getContext() {
        return sInstance.getApplicationContext();
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        SharedPrefUtil.init(getApplicationContext());
        DatabaseUtil.init(getApplicationContext());


//        mR = AdvertAdmobRepository.getInstance(new AdvertConfig() {
//            @Override
//            public String application_id() {
//                return getString(R.string.app_id);
//            }
//
//            @Override
//            public SparseArray<String> banner_ad_unit_id() {
//                SparseArray<String> arr = new SparseArray<>();
//                arr.put(R.id.top_banner0, getString(R.string.b1));
//                arr.put(R.id.top_banner_scd, getString(R.string.b2));
//                return arr;
//            }
//
//            @Override
//            public String interstitial_ad_unit_id() {
//                return null;
//            }
//        });
        this.registerActivityLifecycleCallbacks(this);

        BaseActivity.initializeAdMobAndConfigureTestDevices(this);


        //Thread.setDefaultUncaughtExceptionHandler(CustomExceptionHandler.getInstance(getApplicationContext()));
        //mRequestQueue = Volley.newRequestQueue(this);
        //Fabric.with(this, new Crashlytics());

        // Obtain the FirebaseAnalytics instance.
        //FirebaseAnalytics.getInstance(this);
        //getKeyHash("SHA");
        //getKeyHash("MD5");


        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        appOpenAdManager = new AppOpenAdManager(getString(R.string.admob_run_id));
    }


    @SuppressLint("PackageManagerGetSignatures")
    private void getKeyHash(String hashStretagy) {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance(hashStretagy);
                md.update(signature.toByteArray());


                final byte[] digest = md.digest();
                final StringBuilder toRet = new StringBuilder();
                for (int i = 0; i < digest.length; i++) {
                    if (i != 0) {
                        //toRet.append(":");
                    }
                    int b = digest[i] & 0xff;
                    String hex = Integer.toHexString(b);
                    if (hex.length() == 1) toRet.append("0");
                    toRet.append(hex);
                }

                DLog.d(getPackageName() + " || " + toRet);
                String something = new String(Base64.encode(md.digest(), 0));
                DLog.d("KeyHash  -->>>>>>>>>>>>" + something);

                // Notification.registerGCM(this);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e1) {
            DLog.handleException(e1);
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    void log(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        FirebaseAnalytics.getInstance(this)
                .logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    /**
     * LifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
//    @OnLifecycleEvent(Lifecycle.Event.ON_START)
//    protected void onMoveToForeground() {
//        // Show the ad (if available) when the app moves to foreground.
//        appOpenAdManager.showAdIfAvailable(currentActivity);
//    }
    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        showrequest();
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        //showrequest();
    }

    private void showrequest() {
        DLog.d("@@@@" + currentActivity + "@@@");
        // Show the ad (if available) when the app moves to foreground.
        if (ACTIVITY_MOVES_TO_FOREGROUND_HANDLE && !disableWads) {
            //From Gallery @_com.walhalla.qrcode.ui.activity.MainActivity_@
            appOpenAdManager.showAdIfAvailable(currentActivity);
        }

        //reset lock!!!!
        disableWads = false;
    }

    /**
     * ActivityLifecycleCallback methods.
     */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    /**
     * Shows an app open ad.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable0(@NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
    }


    //Проблема... Fragment.onActivityResult срабатывает раньше Activity.onResume
    public void onActivityResult() {
        //disableWads = false;
        DLog.d("@@@reset@@@" + disableWads);
    }

    public void setBannerLock() {
        disableWads = true;//button pressed
        DLog.d("@@@set@@@" + disableWads);
    }
}

package com.walhalla.vibro.adapter;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.walhalla.ui.DLog;
import com.walhalla.vibro.BuildConfig;
import com.walhalla.vibro.activity.Main;

public class MainAdapter {

    public static boolean isPro0 = false;
    private static final String AD_UNIT_ID = "ca-app-pub-5111357348858303/9317790194";
    private final Context context;

    private int current_click_count = 0;
    private final int total_click_count = 15;
    private InterstitialAd mInterstitialAd;

    private final FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {

        @Override
        public void onAdDismissedFullScreenContent() {
            // Called when fullscreen content is dismissed.
            DLog.d("Закрыли рекламу");
            if (context != null) {
                makeInterstitialAd(context);
            }
        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            // Called when fullscreen content failed to show.
            DLog.d("The ad failed to show.");
        }

        @Override
        public void onAdShowedFullScreenContent() {
            // Called when fullscreen content is shown.
            // Make sure to set your reference to null so you don't
            // show it a second time.
            mInterstitialAd = null;
            DLog.d("The ad was shown....");
        }

    };

    public MainAdapter(Context context) {
        this.context = context;
        makeInterstitialAd(this.context);
    }


    private void makeInterstitialAd(Context context) {

        current_click_count = 0;

        InterstitialAd.load(context, AD_UNIT_ID, new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(fullScreenContentCallback);
                        current_click_count = 0;
                        DLog.d("onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        DLog.d(loadAdError.getMessage());
                        mInterstitialAd = null;
                    }

                });
    }

    public void click() {
        ++current_click_count;
        DLog.d("@@ current_click_count: " + current_click_count);
    }

    public void interstitialAd(Activity activity) {
        if (mInterstitialAd == null) {
            DLog.d("The interstitial ad wasn't ready yet.");
            return;
        }
        if (isNeedAds()) {
            mInterstitialAd.show(activity);
        }
    }

    private boolean isNeedAds() {
        boolean r = (!isPro0 && current_click_count > 0
                /*&& current_click_count % total_click_count == 0*/
                && current_click_count >= total_click_count
        );
        if (BuildConfig.DEBUG) {
            DLog.d("current_click_count -->" + current_click_count + " " + r);
        }
        return r;
    }
}

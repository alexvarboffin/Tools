package a.bubblelevel.spiritpro;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.walhalla.ui.DLog;


public class AdAdmob {

    public static final String TEST_BANNER_ADS = "ca-app-pub-3940256099942544/6300978111";
    public static String BannerAdID, FullscreenAdID = "ca-app-pub-3940256099942544/1033173712";

    static ProgressDialog progressDialog;

    public AdAdmob(Activity activity) {


        MobileAds.initialize(activity, initializationStatus -> {
        });


    }


    public void BannerAd(final RelativeLayout Ad_Layout, Activity activity) {
        BannerAdID = BuildConfig.DEBUG ? TEST_BANNER_ADS : activity.getString(R.string.b1);

        AdView mAdView = new AdView(activity);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(BannerAdID);
        AdRequest adore = new AdRequest.Builder().build();
        mAdView.loadAd(adore);
        Ad_Layout.addView(mAdView);


        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                Ad_Layout.setVisibility(View.VISIBLE);
                super.onAdLoaded();

                DLog.d("ddddddddd");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Ad_Layout.setVisibility(View.GONE);
                DLog.d("ddddd1dddd");

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mAdView.destroy();
                Ad_Layout.setVisibility(View.GONE);
                DLog.d("ddddd2dddd" + loadAdError.getMessage());

            }
        });


    }

    public void FullscreenAd(final Activity activity) {
        Ad_Popup(activity);

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(activity, FullscreenAdID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        interstitialAd.show(activity);
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        progressDialog.dismiss();
                    }
                });
    }


    private static final String Count_Ads = "Count_Ads";


    public static void setCount_Ads(Context mContext, int string) {
        mContext.getSharedPreferences(mContext.getPackageName(), 0).edit()
                .putInt(Count_Ads, string).apply();
    }

    public static int getCount_Ads(Context mContext) {
        return mContext.getSharedPreferences(mContext.getPackageName(), 0)
                .getInt(Count_Ads, 1);
    }

    public void FullscreenAd_Counter(final Activity activity) {


//        int counter_ads = getCount_Ads(activity);
//
//        if (counter_ads >= 3) {
//
//            setCount_Ads(activity, 1);
//
//            try {
//
//                Ad_Popup(activity);
//
//                AdRequest adRequest = new AdRequest.Builder().build();
//
//                InterstitialAd.load(activity, FullscreenAdID, adRequest,
//                        new InterstitialAdLoadCallback() {
//                            @Override
//                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//
//                                interstitialAd.show(activity);
//                                progressDialog.dismiss();
//
//                            }
//
//                            @Override
//                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//
//                                progressDialog.dismiss();
//                            }
//                        });
//
//            } catch (Exception e) {
//
//            }
//
//        } else {
//            counter_ads = counter_ads + 1;
//            setCount_Ads(activity, counter_ads);
//
//        }


    }


    private static void Ad_Popup(Context mContext) {


        progressDialog = ProgressDialog.show(mContext, "", "Ad Loading . . .", true);
        progressDialog.setCancelable(true);
        progressDialog.show();

    }


}
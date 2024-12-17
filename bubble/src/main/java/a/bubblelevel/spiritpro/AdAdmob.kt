package a.bubblelevel.spiritpro

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.walhalla.ui.DLog


class AdAdmob(activity: Activity?) {
    fun BannerAd(Ad_Layout: RelativeLayout, activity: Activity) {
        BannerAdID = if (BuildConfig.DEBUG) TEST_BANNER_ADS else activity.getString(R.string.b1)

        val mAdView = AdView(activity)
        mAdView.setAdSize(AdSize.BANNER)
        mAdView.adUnitId = BannerAdID!!
        val adore = AdRequest.Builder().build()
        mAdView.loadAd(adore)
        Ad_Layout.addView(mAdView)


        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Ad_Layout.visibility = View.VISIBLE
                super.onAdLoaded()

                DLog.d("ddddddddd")
            }

            override fun onAdOpened() {
                super.onAdOpened()
                Ad_Layout.visibility = View.GONE
                DLog.d("ddddd1dddd")
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                mAdView.destroy()
                Ad_Layout.visibility = View.GONE
                DLog.d("ddddd2dddd" + loadAdError.message)
            }
        }
    }

    fun FullscreenAd(activity: Activity) {
        Ad_Popup(activity)

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(activity, FullscreenAdID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    interstitialAd.show(activity)
                    progressDialog!!.dismiss()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    progressDialog!!.dismiss()
                }
            })
    }


    init {
        MobileAds.initialize(activity!!) { initializationStatus: InitializationStatus? -> }
    }


    fun FullscreenAd_Counter(activity: Activity?) {
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


    companion object {
        const val TEST_BANNER_ADS: String = "ca-app-pub-3940256099942544/6300978111"
        var BannerAdID: String? = null
        var FullscreenAdID: String = "ca-app-pub-3940256099942544/1033173712"

        var progressDialog: ProgressDialog? = null

        private const val Count_Ads = "Count_Ads"


        fun setCount_Ads(mContext: Context, string: Int) {
            mContext.getSharedPreferences(mContext.packageName, 0).edit()
                .putInt(Count_Ads, string).apply()
        }

        fun getCount_Ads(mContext: Context): Int {
            return mContext.getSharedPreferences(mContext.packageName, 0)
                .getInt(Count_Ads, 1)
        }

        private fun Ad_Popup(mContext: Context) {
            progressDialog = ProgressDialog(mContext).apply {
                setMessage("Ad Loading . . .")
                setCancelable(true)
                show()
            }
        }
    }
}
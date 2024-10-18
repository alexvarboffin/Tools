package com.walhalla.qrcode;

public class AdManager {
//
//    private static final String TAG = "@@@";
//    private static List<InterstitialAd> adsList = new ArrayList<>();
//    private static AdManager instance;
//
//    private static int index = 0;
//
//
//    private AdRequest builder = new AdRequest.Builder()
////            .addTestDevice("D835BCD2872E5FA7FB21AB05AB396F5C")
////            .addTestDevice("085F431DB4C4ACA8649B1E07D61A5333")
//            .build();
//
//    private boolean show_ads = false;
//    private Callback callback;
//
//
//    public interface Callback {
//        void successResponse();
//
//        void onAdClosed();
//
//        void errorResponse();
//    }
//
//
////    public void setListener(Callback callback) {
////        this.callback = callback;
////    }
//
//    private AdManager() {
//    }
//
//    public void init(Context context) {
//        initAds(context);
//        incrementIndex();
//        initAds(context);
//    }
//
//    public static AdManager getInstance() {
//        if (instance == null) {
//            instance = new AdManager();
//        }
//        return instance;
//    }
//
//    private void initAds(Context context) {
//        if (adsList.size() == index + 1) {
//            adsList.set(index, createAd(context));
//        } else {
//            adsList.add(index, createAd(context));
//        }
//    }
//
//
//    public void runNow(Context context, Callback callback) {
//        this.show_ads = true;
//        this.callback = callback;
//    }
//
//
//    public void showAds(Context context, Callback callback) {
//
//        boolean is_lock = false;
//
//        for (int i = index; i < adsList.size(); i++) {
//            if (adsList.get(i).isLoaded() && !is_lock) {
//                adsList.get(i).show();
//                if (callback != null) {
//                    callback.successResponse();
//                }
//                initAds(context);
//                //           incrementIndex();
//                is_lock = true;
//            } else {
//                adsList.get(index).loadAd(builder);
//                //           incrementIndex();
//            }
//        }
//
//        if (callback != null) {
//            callback.errorResponse();
//        }
//        incrementIndex();
//    }
//
//    private InterstitialAd createAd(Context context) {
//        InterstitialAd interstitialAd = new InterstitialAd(context);
//        interstitialAd.setAdUnitId(context.getResources()
//                .getString(R.string.i1));
//        interstitialAd.loadAd(builder);
//        interstitialAd.setAdListener(new CListener(interstitialAd));
//        return interstitialAd;
//    }
//
//    //    0or1
//    private void incrementIndex() {
//        index++;
//        index %= 2;
//    }
//
//    private class CListener extends AdListener {
//
//
//        private final InterstitialAd obj;
//
//        CListener(InterstitialAd obj) {
//            this.obj = obj;
//        }
//
//        @Override
//        public void onAdClosed() {
//            Log.i(TAG, "onAdClosed: " + obj.getAdUnitId() + "|" + obj.hashCode());
//            super.onAdClosed();
//            obj.loadAd(builder);
//            if (callback != null) {
//                callback.onAdClosed();
//            }
//        }
//
//        @Override
//        public void onAdFailedToLoad(int i) {
//            Log.i(TAG, "onAdFailedToLoad: " + obj.getAdUnitId() + "|" + obj.hashCode() + "|" + i);
//            //initAds(context);
//            super.onAdFailedToLoad(i);
//        }
//
//        @Override
//        public void onAdLeftApplication() {
//            Log.i(TAG, "onAdLeftApplication: " + obj.getAdUnitId() + "|" + obj.hashCode());
//            super.onAdLeftApplication();
//        }
//
//        @Override
//        public void onAdOpened() {
//            Log.i(TAG, "onAdOpened: " + obj.getAdUnitId() + "|" + obj.hashCode());
//            super.onAdOpened();
//        }
//
//        @Override
//        public void onAdLoaded() {
//            super.onAdLoaded();
//
//            Log.i(TAG, "onAdLoaded: " + obj.getAdUnitId() + "|" + obj.hashCode());
//            if (show_ads) {
//                show_ads = false;
//                obj.show();
//                if (callback != null) {
//                    callback.successResponse();
//                }
//            }
//        }
//
//        @Override
//        public void onAdClicked() {
//            Log.i(TAG, "onAdClicked: " + obj.getAdUnitId() + "|" + obj.hashCode());
//            super.onAdClicked();
//        }
//
//        @Override
//        public void onAdImpression() {
//            Log.i(TAG, "onAdImpression: " + obj.getAdUnitId() + "|" + obj.hashCode());
//            super.onAdImpression();
//        }
//    }

}

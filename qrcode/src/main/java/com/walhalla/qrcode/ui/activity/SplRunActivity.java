//package com.walhalla.qrcode.ui.activity;
//
//import android.annotation.SuppressLint;
//import android.app.Application;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.animation.AnimationSet;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.walhalla.core.SharedPref;
//import com.walhalla.qrcode.R;
//import com.walhalla.qrcode.app.RunApp;
//import com.walhalla.ui.DLog;
//import com.walhalla.wads.OnShowAdCompleteListener;
//
///*
//Покажите свое первое объявление об открытии приложения после того,
//как ваши пользователи воспользуются вашим приложением несколько раз.
//
//     Показывайте объявления об открытии приложения в то время, когда ваши пользователи
//     в противном случае ждали бы загрузки вашего приложения.
//
//     Если у вас есть экран загрузки под объявлением об открытии приложения, и ваш экран загрузки завершает загрузку до закрытия объявления, вы можете закрыть экран
//     загрузки в методе onAdDismissedFullScreenContent().
// */
//public class SplRunActivity extends AppCompatActivity {
//
//    private static final String LOG_TAG = "@@@";
//    private static final String KEY_DISM_COUNT = "key_dism";
//
//    private boolean REQURST_RUN_ADS;
//    //private PulsatorLayout pulsator;
//
//    private View viewProgress;
//    /**
//     * Number of seconds to count down before showing the app open ad. This simulates the time needed
//     * to load the app.
//     */
//
//
//    private long secondsRemaining;
//    private int COUNTER_TIME;
//
//
////    public static class Loading extends AsyncTask<Void, Void, Void> {
////        private final WeakReference<Activity> mWeakReference;
////
////
////        public Loading(Activity activity) {
////            mWeakReference = new WeakReference<>(activity);
////        }
////
////        protected Void doInBackground(Void... voids) {
////            try {
////                Thread.sleep(1100);
////            } catch (InterruptedException ie) {
////                ie.printStackTrace();
////            }
////            return null;
////        }
////
////        protected void onPostExecute(Void aVoid) {
////            super.onPostExecute(aVoid);
////            Activity activity = mWeakReference.get();
////            if (activity != null) {
////                activity.startActivity(
////                        new Intent(activity.getApplicationContext(),
////                                MainActivity.class));
////                activity.overridePendingTransition(
////                        R.anim.open_next, R.anim.close_main);
////            }
////
////        }
////    }
//
//    @SuppressLint("ObsoleteSdkInt")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        //Thread.setDefaultUncaughtExceptionHandler(CustomExceptionHandler.getInstance(getApplicationContext()));
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_lottie);
//
//        final TextView textView = findViewById(R.id.text_ver);
//        textView.setText(DLog.getAppVersion(this));
//        SharedPref pref = SharedPref.getInstance(this);
//        //mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        int dism = 0;//mSharedPreferences.getLong(KEY_DISM_COUNT, 0);
//        boolean isRated = pref.appRated();
//        int cc = pref.appResumeCount(); //onResume
//        int count = pref.getLaunchCount();
//        if (count == 0) {
//            DLog.d("@aaa@ :: First launch " + isRated + " " + cc + " " + dism);
//            //if (isRated) {
//                pref.setLaunchCount(1);//level2
//            //}
//            REQURST_RUN_ADS = false;
//            COUNTER_TIME = 4;
//        } else if (count == 1) {//level2
//            pref.setLaunchCount(2);
//            COUNTER_TIME = 3;
//            REQURST_RUN_ADS = true;
//            DLog.d("@aaa@ second launch " + isRated + " " + cc + " " + dism);
//        } else {
//            COUNTER_TIME = 3;
//            REQURST_RUN_ADS = true;
//            DLog.d("@aaa@ >2 launch " + isRated + " " + cc + " " + dism);
//        }
//        //show
//        //skip
//        //skip
////        viewProgress = findViewById(R.id.view_progress);
////        int viewWidth = viewProgress.getWidth();
////
////        TranslateAnimation move = new TranslateAnimation(-(getScreenWidth() / 2) + viewWidth / 2, (getScreenWidth() / 2) + viewWidth / 2 + viewWidth, 0, 0);
////        move.setDuration(1000);
////        TranslateAnimation move1 = new TranslateAnimation(-viewWidth, 0, 0, 0);
////        move1.setDuration(500);
////        ScaleAnimation laftOut = new ScaleAnimation(0, 1, 1, 1);
////        laftOut.setDuration(500);
////
////        AnimationSet animationSet = new AnimationSet(true);
////        animationSet.addAnimation(move);
////        animationSet.addAnimation(move1);
////        animationSet.addAnimation(laftOut);
////        animationSet.addAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideout));
////        startAnimation(animationSet);
//
//        //@@@ Loading task = new Loading(this);
//        //@@@ if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//        //@@@     task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        //@@@ else
//        //@@@     task.execute();
//
//        // Create a timer so the SplashActivity will be displayed for a fixed amount of time.
//        createTimer(COUNTER_TIME);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (COUNTER_TIME > 1) {
////            this.pulsator = findViewById(R.id.pulsator);
////            this.pulsator.setCount(3);
////            this.pulsator.setDuration(2_200);//single pulse duration
////            this.pulsator.start();
//        }
//    }
//
//    @SuppressLint("ObsoleteSdkInt")
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (Build.VERSION.SDK_INT >= 19 && hasFocus) {
//            getWindow().getDecorView().setSystemUiVisibility(5126);
//        }
//    }
//
//    private void startAnimation(AnimationSet animationSet) {
//        viewProgress.startAnimation(animationSet);
//        new android.os.Handler().postDelayed(() -> startAnimation(animationSet), 10);
//    }
//
//    public static int getScreenWidth() {
//        return Resources.getSystem().getDisplayMetrics().widthPixels;
//    }
//
//
//    /**
//     * Create the countdown timer, which counts down to zero and show the app open ad.
//     *
//     * @param seconds the number of seconds that the timer counts down from
//     */
//    private void createTimer(long seconds) {
//        //final TextView counterTextView = findViewById(R.id.timer);
//        CountDownTimer countDownTimer =
//                new CountDownTimer(seconds * 1000, 1000) {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                        secondsRemaining = ((millisUntilFinished / 1000) + 1);
//                        //counterTextView.setText("" + secondsRemaining);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        secondsRemaining = 0;
//                        //counterTextView.setText("Done.");
//
//                        Application application = getApplication();
//
//                        // If the application is not an instance of MyApplication, log an error message and
//                        // start the MainActivity without showing the app open ad.
//                        if (!(application instanceof RunApp)) {
//                            Log.e(LOG_TAG, "Failed to cast application to MyApplication.");
//                            startMainActivity();
//                            return;
//                        }
//
//                        if (REQURST_RUN_ADS) {
//                            // Show the app open ad.
//                            ((RunApp) application)
//                                    .showAdIfAvailable0(
//                                            SplRunActivity.this,
//                                            new OnShowAdCompleteListener() {
//                                                @Override
//                                                public void onShowAdComplete() {
//                                                    startMainActivity();
//                                                }
//
//                                                @Override
//                                                public void adAdDismissedBackPressed() {
//                                                    //++dism;
//                                                    //mSharedPreferences.edit().putLong(KEY_DISM_COUNT, dism).apply();
//                                                    startMainActivity();
//                                                }
//                                            });
//                        } else {
//                            startMainActivity();
//                        }
//                    }
//                };
//        countDownTimer.start();
//    }
//
//
//    /**
//     * Start the MainActivity.
//     */
//    private void startMainActivity() {
//        this.startActivity(new Intent(this.getApplicationContext(), MainActivity.class));
//        this.overridePendingTransition(R.anim.open_next, R.anim.close_main);
//    }
//}

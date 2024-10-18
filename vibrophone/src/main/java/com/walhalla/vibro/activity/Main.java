package com.walhalla.vibro.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;
//import com.android.billingclient.api.BillingClient;
//import com.android.billingclient.api.BillingClient.SkuType;
//import com.android.billingclient.api.BillingClientStateListener;
//import com.android.billingclient.api.BillingFlowParams;
//import com.android.billingclient.api.Purchase;
//import com.android.billingclient.api.PurchasesUpdatedListener;
//import com.android.billingclient.api.SkuDetails;
//import com.android.billingclient.api.SkuDetailsParams;
//import com.android.billingclient.api.SkuDetailsResponseListener;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

//import com.google.android.gms.ads.AdListener;
////import com.google.android.gms.ads.AdRequest;
////import com.google.android.gms.ads.AdSize;
////import com.google.android.gms.ads.AdView;
////import com.google.android.gms.ads.InterstitialAd;
////import com.google.android.gms.ads.MobileAds;
////import com.google.android.gms.ads.reward.RewardedVideoAd;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import com.walhalla.boilerplate.domain.executor.impl.ThreadExecutor;
import com.walhalla.boilerplate.threading.MainThreadImpl;
import com.walhalla.common.KonfettiPresenter;
import com.walhalla.domain.interactors.AdvertInteractor;
import com.walhalla.domain.interactors.impl.AdvertInteractorImpl;

import com.walhalla.ui.DLog;

import com.walhalla.ui.plugins.Launcher;
import com.walhalla.ui.plugins.Module_U;
import com.walhalla.vibro.App;

import com.walhalla.vibro.Constants;
import com.walhalla.vibro.LStorage;
import com.walhalla.vibro.PlayerManager;
import com.walhalla.vibro.R;
import com.walhalla.vibro.adapter.MainAdapter;
import com.walhalla.vibro.databinding.ActivityMainBinding;
import com.walhalla.vibro.fragment.TabHolder2Fragment;
import com.walhalla.vibro.fragment.VibrationModeFragment;
import com.walhalla.vibro.helpers.VibrationModeSwitcher;
import com.walhalla.vibro.service.MediaPlayerService;
import com.walhalla.vibro.service.ServiceHelper;

import androidx.appcompat.app.AppCompaqActivity;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import static android.media.AudioManager.RINGER_MODE_VIBRATE;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class Main extends AppCompaqActivity

        //extends com.walhalla.domain.interactors.PromoActivity
        implements VibrationModeFragment.Callback, VibrationModeSwitcher, PlayerManager.Listener {


    private ScheduledThreadPoolExecutor mExecutor;
    private FrameLayout bottomButton;

    private final AdvertInteractor.Callback<View> callback = new AdvertInteractor.Callback<>() {
        @Override
        public void onMessageRetrieved(int id, View message) {
            DLog.d(message.getClass().getName() + " --> " + message.hashCode());

            if (bottomButton != null) {
                DLog.d("@@@" + bottomButton.getClass().getName());
                try {
                    //content.removeView(message);
                    if (message.getParent() != null) {
                        ((ViewGroup) message.getParent()).removeView(message);
                    }
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.BOTTOM | Gravity.CENTER;
                    message.setLayoutParams(params);


                    ViewTreeObserver vto = message.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @SuppressLint("ObsoleteSdkInt")
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT < 16) {
                                message.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                message.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                            //int width = message.getMeasuredWidth();
                            //int height = message.getMeasuredHeight();
                            //DLog.i("@@@@" + height + "x" + width);
                            //setSpaceForAd(height);
                        }
                    });
                    bottomButton.addView(message);

                } catch (Exception e) {
                    DLog.handleException(e);
                }
            }
        }

        @Override
        public void onRetrievalFailed(String error) {
            DLog.d("---->" + error);
        }
    };


    //Wads
    //private AdView adView;

//    private RewardedVideoAd mAd;

    //Vibrator
    //private Vibrator mVibrator = null;


    //private RateAppModule mRateAppModule;
    private CoordinatorLayout coordinatorLayout;
    //private VibrationMode currentMode = null;

    private final Handler handler = new Handler(Looper.getMainLooper());


    private PlayerManager manager;
    private MainAdapter ff;
    private ActivityMainBinding binding;
    private KonfettiPresenter k;



    @Override
    public void troubleReport(String err) {
        handler.post(() -> Toast.makeText(Main.this, err, Toast.LENGTH_SHORT).show());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        manager = PlayerManager.getInstance(this);
        manager.setListener(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        k = new KonfettiPresenter(binding.konfettiView);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setSupportActionBar(binding.toolbar);
        mExecutor = new ScheduledThreadPoolExecutor(1);

        //toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        binding.toolbar.setNavigationOnClickListener(v -> {
            Module_U.aboutDialog(this);
        });
        LStorage.getInstance(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //getSupportActionBar().setSubtitle(Dlog.getAppVersion(this));
            getSupportActionBar().setSubtitle(DLog.getAppVersion(this));
        }

        ff = new MainAdapter(this);

//        MainActivity.this.interstitialAd.setAdListener(new AdListener() {
//            public void onAdClosed() {
//                interstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//        });

        //this.adView = findViewById(R.id.adView);
        bottomButton = binding.bottomButton;


        //this.adView.setVisibility(View.GONE);

        // Start loading the ad in the background.
        //this.adView.setAdListener(new AdListener0(adView));


//            this.adView.loadAd(new AdRequest.Builder().build());
//            //Module_U.checkUpdate(Main.this);
//            toolbar.post(() -> {});

        setupAdAtBottom(bottomButton);

        if (PlayerManager.getInstance(this).noVibro()) {
            makeToast(getString(R.string.error_not_have_vibrator));
        }

//        Log.d(TAG, "###: " + mVibrator.hasVibrator());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Log.d(TAG, "###: " + mVibrator.hasAmplitudeControl());
//        }


        //this.mVibrator.cancel()

        //mRateAppModule = new RateAppModule(this);
        //getLifecycle().addObserver(mRateAppModule);        //WhiteScreen

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        binding.fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            makeToast("123");

        });

//        if (appReloaded == 0) // Если пользователь запускает программу впервые, ему можно показать какое-нибудь сообщение
//        {
//            Toast.makeText(MainActivity.this, getString(R.string.welcome), Toast.LENGTH_LONG).show();
//        }

        if (savedInstanceState != null) {
            return;
        }
        //currentMode = Mode.EXTENDED;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,
                        //new SettingsPreferenceFragment()
                        //CoreFragment.newInstance(currentMode)
                        TabHolder2Fragment.newInstance("", "")
                )
                .commit();


//        AccountHeader headerResult = new AccountHeaderBuilder()
//                .withActivity(this)
//                .withHeaderBackground(R.drawable.nav_header)
//                .addProfiles(
//                        new ProfileDrawerItem()
//                                .withName(R.string.app_name)
//                                .withEmail("mikepenz@gmail.com")
//                                .withIcon(getResources()
//                                        .getDrawable(R.mipmap.ic_launcher))
//                )
//                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
//                    @Override
//                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
//                        return false;
//                    }
//                })
//                .build();
//        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1)
//                .withName(R.string.mode_random);
//        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2)
//                .withName(R.string.mode_simple);
//
//        Drawer result = new DrawerBuilder()
//                .withActivity(this)
//                .withAccountHeader(headerResult)
//                .withToolbar(toolbar)
//                .addDrawerItems(
//                        item1,
//                        new DividerDrawerItem(),
//                        item2,
//                        new SecondaryDrawerItem()
//                                .withName(R.string.app_name)
//                )
//                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
//                    //...
//                    return false;
//                })
//                .build();
//        result.addStickyFooterItem(new PrimaryDrawerItem()
//                .withName("StickyFooterItem"));

    }

    private void setupAdAtBottom(FrameLayout bottomButton) {
        AdvertInteractorImpl interactor = new AdvertInteractorImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(), App.repository);
        //aa.attach(this);
        //DLog.d("---->" + aa.hashCode());
        interactor.selectView(bottomButton, callback);
    }


    @Override
    public void onBackPressed() {

//        this.onClickVibratorStop();
//        PlayerManager.getInstance(this).stop();

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        super.onBackPressed();
    }


    // Start without a delay
// Vibrate for 100 milliseconds
// Sleep for 1000 milliseconds
    long[] pattern1 = {0, 100, 1000};


    @Override
    public void onClickVibratorStop() {
        //DLog.d("@@@@@@@@@@@@");
        PlayerManager.getInstance(this).stopVibration0();
    }

    @Override
    public void showToolbar(boolean visible) {
        binding.toolbar.setVisibility((visible) ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onClickVibratorTestBtn(String status) {
        if (status.equals(getString(R.string.action_start))) {
            //DLog.d(DLog.nonNull(getVibrator()) + "\t" + mVibrator.hasVibrator());


            if (PlayerManager.getInstance(this).noVibro()) {
                makeToast(getString(R.string.error_not_have_vibrator));
            }
            PlayerManager.getInstance(this).start();
            ff.click();

        } else if (status.equals(getString(R.string.action_stop))) {
            ff.click();
            this.onClickVibratorStop();
            ff.interstitialAd(this);
        }
    }


    @Override
    public void changeMode(final int modeIndex) {
        PlayerManager.getInstance(this).setMode00(modeIndex);
        PlayerManager.getInstance(this).stopVibration0();

//        if (mInterstitialAd == null) {
//            //           replaceFragment(VibrationModeFragment.newInstance(mode));
//        } else if (isNeedAds()) {
////            this.interstitialAd.setAdListener(new AdListener() {
////                public void onAdClosed() {
////            replaceFragment(VibrationModeFragment.newInstance(mode));
////                }
////            });
//            mInterstitialAd.show(this);
//        } else {
////            replaceFragment(VibrationModeFragment.newInstance(mode));
//        }

        ff.interstitialAd(this);
    }


    @Override
    public void waitTimeProgress(int curProcess) {
        PlayerManager.instance.waitTimeProgress(curProcess);
    }

    @Override
    public void vibroTimeProgress(int curProcess) {
        PlayerManager.instance.vibroTimeProgress(curProcess);
    }

    @Override
    public void randomTime(int i) {
        PlayerManager.instance.randomTime0 = i;
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        menu.add("crash").setOnMenuItemClickListener(v -> {
//            throw new RuntimeException("Test Crash"); // Force a crash
//        });
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.action_not_vibrate) {
            bugResolver();
            return true;
//            case R.id.action_refresh:
//                return false;
//
//            case android.R.id.home:
//                Intent intent = m1.newIntent(this);
//                startActivity(intent);
//                return true;


//            case R.string.start_test_again:
//                return false;
        } else if (itemId == R.id.action_about) {
            Module_U.aboutDialog(this);
            return true;
        } else if (itemId == R.id.action_privacy_policy) {
            Launcher.openBrowser(this, getString(R.string.url_privacy_policy));
            return true;
        } else if (itemId == R.id.action_rate_app) {
            k.explode();
            Launcher.rateUs(this);
            return true;

//            case R.id.action_more_app_01:
//                Module_U.openMarket(this, "com.walhalla.ttloader");
//                return true;

//            case R.id.action_more_app_02:
//                Module_U.moreApp(this, "com.walhalla.vibro");
//                return true;
        } else if (itemId == R.id.action_share_app) {
            k.rain();
            Module_U.shareThisApp(this);
            return true;
        } else if (itemId == R.id.action_discover_more_app) {
            Module_U.moreApp(this);
            return true;

//            case R.id.action_exit:
//                this.finish();
//                return true;
        } else if (itemId == R.id.action_feedback) {
            Module_U.feedback(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    protected void bugResolver() {
        String mode1 = "";

        AudioManager audioManager = ((AudioManager) getSystemService(AUDIO_SERVICE));
        int mode;
        if (audioManager != null) {
            mode = audioManager.getRingerMode();
            if (mode == AudioManager.RINGER_MODE_SILENT) { //0
                mode1 = "SILENT";
            } else if (mode == AudioManager.RINGER_MODE_NORMAL) {//2
                mode1 = "NORMAL";
            } else if (mode == RINGER_MODE_VIBRATE) { //1
                mode1 = "VIBRATE";
            }
        }
        String warn = getString(R.string.vibration_bug_text);

        SpannableStringBuilder value = new SpannableStringBuilder();
        Spannable none = new SpannableStringBuilder(warn);
//        none.setSpan(new ForegroundColorSpan(Color.RED), 0, none.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
//        none.setSpan(new BackgroundColorSpan(Color.BLACK), 0, none.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        none.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, none.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        value.append(none);
        value.append("\n");

        value.append(getString(R.string.vibration_bug_one));
        value.append("\n");
        value.append("\n");

        Spannable www = new SpannableStringBuilder("[+] Has Vibrator = ");
        www.setSpan(new ForegroundColorSpan(Color.BLACK), 0, www.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        www.setSpan(new BackgroundColorSpan(Color.YELLOW), 0, www.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        value.append(www);

        String status = PlayerManager.instance.noVibro()
                ? getString(R.string.error_not_have_vibrator) : "true";

        Spannable qqqq = new SpannableStringBuilder(status);
        qqqq.setSpan(new ForegroundColorSpan(Color.BLACK), 0, qqqq.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        qqqq.setSpan(new BackgroundColorSpan(Color.YELLOW), 0, qqqq.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        value.append(qqqq);
        value.append("\n");


        Spannable spannable = new SpannableStringBuilder("[+] RINGER_MODE = ");
        spannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannable.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new BackgroundColorSpan(Color.YELLOW), 0, spannable.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        value.append(spannable);


        Spannable spannable1 = new SpannableStringBuilder(mode1);
        spannable1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannable1.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable1.setSpan(new BackgroundColorSpan(Color.YELLOW), 0, spannable1.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        value.append(spannable1);
        value.append("\n");

        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_label_editor, null);
        builder.setView(dialogView);

        builder.setTitle(R.string.action_not_vibrate)
                .setMessage(value)
                .setIcon(R.drawable.ic_bug_report_black_24dp)
                .setCancelable(false)
                //.setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.cancel())
                .setPositiveButton(android.R.string.ok,
                        (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }


//    @Override
//    public void onBackPressed() {
//        finish();
//    }

    public void replaceFragment0(Fragment fragment) {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
//        ft.replace(R.id.container, fragment);
//        //ft.addToBackStack(null);
//        ft.commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        if (mRateAppModule != null) {
//            mRateAppModule.appReloadedHandler();
//        }
        super.onSaveInstanceState(outState);
        makeBackgroundService();
    }

    private void makeBackgroundService() {

        ServiceHelper utils = new ServiceHelper();
        boolean isRun = utils.isMyServiceRunning(this, MediaPlayerService.class);

        if (PlayerManager.instance.isVibrating) {

            if (!isRun) {
                Intent intent = new Intent(Main.this, MediaPlayerService.class);
                intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                intent.putExtra(Constants.EXTRA.PLAY_EXTRA, PlayerManager.all_mode);
                //ForegroundService.IS_SERVICE_RUNNING = true;
                //button.setText("Stop Service");
                startService(intent);
            }
            MediaPlayerService.play(this, PlayerManager.all_mode, PlayerManager.instance.mode_index);
            //DLog.d("##" + PlayerManager.instance.isVibrating);
        } else {
            if (isRun) {
                Intent intent = new Intent(Main.this, MediaPlayerService.class);
                intent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                startService(intent);
            }
        }
    }

//    @Override
//    public void onClick(MenuKey navItem) {
//
//        switch (navItem.getIc1()) {
//            case R.drawable.ic_menu_01: //Fart
//                replaceFragment(new OopsFragment());
//                break;
//            case R.drawable.ic_piano: //Piano
//                //startActivity(new Intent(this,BrowserActivity.class));
//                startActivity(new Intent(this, FartpianoActivity.class));
//                break;
//
//            case R.drawable.ic_grenade: //Timer
//                replaceFragment(new GrenadeFragment());
//                break;
//

//
////            case R.drawable.ic_chat:
////                Uri chat = Uri.parse("market://details?id=com.walhalla.anonymouschatroulette"); // Go to Android market
////                Intent goToChat = new Intent(Intent.ACTION_VIEW, chat);
////                try {
////                    startActivity(goToChat);
////                } catch (ActivityNotFoundException e) {
////                    Toast.makeText(this, R.string.err_network, Toast.LENGTH_LONG).show();
////                }
////                break;
//
//
////            case R.drawable.ic_mail:
////                startActivity(new Intent(this, SendMailActivity.class));
////                break;
//
//            default:
//                break;
//        }
//    }


    //Wads
    private void loadRewardedVideoAd() {
        //    this.mAd.loadAd(getString(R.string.b1), new AdRequest.Builder().build());
    }


    @Override
    public void makeToast(String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(title);
//        builder.setNeutralButton(17039369, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//        builder.create().show();


        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        Snackbar snackbar = Snackbar//Snackbar.LENGTH_LONG
                .make(findViewById(android.R.id.content), message, 4000)
                .setAction(android.R.string.cancel, view -> {
//                        Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Message is restored!", Snackbar.LENGTH_SHORT);
//                        snackbar1.show();
                });
        snackbar.show();
    }

    /**
     * Called when leaving the activity
     */
//    @Override
//    public void onPause() {
//        if (adView != null) {
//            adView.pause();
//        }
//        super.onPause();
//    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (binding.konfettiView.isActive()) {
            binding.konfettiView.reset();
        }
//        if (adView != null) {
//            adView.resume();
//        }
        //PlayerManager.instance.patternVibro();

        try {
            AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            if (am != null) {
                int mode = am.getRingerMode();
                if (mode == AudioManager.RINGER_MODE_SILENT) { //0
                    //@@@ Util.makeRingerMode(context);
                } else if (mode == AudioManager.RINGER_MODE_NORMAL) {//2
                    //new update
                    //@@@ Util.makeRingerMode(context);
                } else if (mode == RINGER_MODE_VIBRATE) { //1
                    //DLog.d("VIBRATE MODE ENABLED");
                }
//                DLog.d((vibrator == null) ? "\t NULL" : "\t hasVibrator? " + vibrator.hasVibrator() + "\t" + mode + "\t"
//                        //@@@+ Util.isVibrateWhenRinging(context)
//                );

                //@@@   am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                //AudioManager.RINGER_MODE_NORMAL
                //AudioManager.RINGER_MODE_SILENT
                // 0 - silent
                //am.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);


                mode = am.getRingerMode();
                if (mode == AudioManager.RINGER_MODE_SILENT) {
                    makeToast("Vibration may not work check the settings...");

                    am.setRingerMode(RINGER_MODE_VIBRATE);
                    int currentStreamRing = am.getStreamVolume(AudioManager.STREAM_RING);
                    am.setStreamVolume(
                            AudioManager.STREAM_RING, currentStreamRing,
                            AudioManager.FLAG_VIBRATE + AudioManager.FLAG_SHOW_UI);

                } else if (mode == AudioManager.RINGER_MODE_NORMAL && 1 != Settings.System.getInt(this.getContentResolver(), "vibrate_when_ringing", 0)) {
                    makeToast("Vibration may not work Check the settings...");

                    am.setRingerMode(RINGER_MODE_VIBRATE);
                    int currentStreamRing = am.getStreamVolume(AudioManager.STREAM_RING);
                    am.setStreamVolume(
                            AudioManager.STREAM_RING, currentStreamRing,
                            AudioManager.FLAG_VIBRATE + AudioManager.FLAG_SHOW_UI);
                }

            }
        } catch (Exception r) {
            DLog.handleException(r); //Not allowed to change Do Not Disturb state
        }

        int aaa = 0;
        boolean mIsVibrateWhenRinging = false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mIsVibrateWhenRinging = Settings.System.getInt(this.getContentResolver(), Settings.System.VIBRATE_WHEN_RINGING) == 1;
            } else {
                mIsVibrateWhenRinging = Settings.System.getInt(this.getContentResolver(), "vibrate_when_ringing") == 1;
            }
            aaa = Settings.System.getInt(getContentResolver(), "vibrate_when_ringing");
            if (!mIsVibrateWhenRinging) {
                try {
                    //SecurityException
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.System.canWrite(this)) {
                            //val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + ctx.packageName))
                            //ctx.startActivity(intent)
                        } else {
                            //onGranted(true)
                            Settings.System.putInt(this.getContentResolver(), "vibrate_when_ringing", 1);
                        }
                    } else {
                        Settings.System.putInt(this.getContentResolver(), "vibrate_when_ringing", 1);
                    }
                } catch (SecurityException r) {
                    DLog.handleException(r);
                    DLog.d("----------------");
                } catch (Exception r0) {
                    DLog.handleException(r0);
                }
            }
            DLog.d("aaa: " + aaa + "");
        } catch (Settings.SettingNotFoundException e) {
            DLog.handleException(e);
        }
        DLog.d("mIsVibrateWhenRinging --> " + mIsVibrateWhenRinging + "::" + aaa);
    }

//    private void test() {
//        NotificationManager notificationManager = (NotificationManager)
//                this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
//
//            Intent intent = new Intent(
//                    android.provider.Settings
//                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
//
//            startActivity(intent);
//        } else {
//            Util.makeRingerMode(this);
//        }
//
////        try {
////            Intent intent = new Intent(
////                    AudioManager.VIBRATE_SETTING_ON);
////
////            startActivity(intent);
////        }catch (Exception e){
////            DLog.handleException(e);
////        }
//
//
//        //Write settings
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(this)) {
//            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
//            startActivityForResult(intent, 888);
//            // now wait for onActivityResult()
//            return;
//        }
//    }


    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
//        if (adView != null) {
//            adView.destroy();
//        }
        //this.wakeLock.release();
        super.onDestroy();
    }
}

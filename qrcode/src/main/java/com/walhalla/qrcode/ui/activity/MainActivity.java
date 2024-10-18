package com.walhalla.qrcode.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.gms.ads.AdRequest;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.snackbar.Snackbar;
import com.walhalla.common.KonfettiPresenter;
import com.walhalla.qrcode.BuildConfig;
import com.walhalla.qrcode.Config;
import com.walhalla.qrcode.R;

import com.walhalla.qrcode.databinding.ActivityHomeBinding;
import com.walhalla.qrcode.helpers.util.PermissionUtil;

import com.walhalla.qrcode.ui.SettingsActivity;
import com.walhalla.qrcode.ui.generate.GenerateFragment;
import com.walhalla.qrcode.ui.history.HistoryFragment;
import com.walhalla.qrcode.ui.scan.ScanFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


import com.walhalla.ui.plugins.Launcher;
import com.walhalla.ui.plugins.Module_U;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private Menu mToolbarMenu;
    private MenuItem prevMenuItem;
    private int subTitle = R.id.action_scan;

    //Rate us
    //private RateAppModule mRateAppModule;
    private final BottomNavigationView.OnItemSelectedListener onItemSelectedListener = new BottomNavigationView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment fragment = null;


            if (menuItem.equals(prevMenuItem)) {
                return false;
            }

            if (prevMenuItem != null) {
                prevMenuItem.setChecked(false);
            } else {
                binding.bottomNavigation.getMenu().getItem(0).setChecked(false);
            }

            prevMenuItem = menuItem;
            menuItem.setChecked(true);

            int itemId = menuItem.getItemId();
            //Toast.makeText(this, "def", Toast.LENGTH_SHORT).show();
            if (itemId == R.id.action_scan) {
                onClickScanRequest();
                return true;
            } else if (itemId == R.id.action_history) {
                subTitle = R.string.toolbar_title_history;
                //        mBinding.textViewGenerate.setTextColor(
//                ContextCompat.getColor(this, R.color.bottom_bar_normal));
//
//        mBinding.textViewScan.setTextColor(
//                ContextCompat.getColor(this, R.color.bottom_bar_normal));
//
//        mBinding.textViewHistory.setTextColor(
//                ContextCompat.getColor(this, R.color.bottom_bar_selected));
//
//        mBinding.imageViewGenerate.setVisibility(View.VISIBLE);
//        mBinding.imageViewGenerateActive.setVisibility(View.INVISIBLE);
//
//        mBinding.imageViewScan.setVisibility(View.VISIBLE);
//        mBinding.imageViewScanActive.setVisibility(View.INVISIBLE);
//
//        mBinding.imageViewHistory.setVisibility(View.INVISIBLE);
//        mBinding.imageViewHistoryActive.setVisibility(View.VISIBLE);
                fragment = HistoryFragment.newInstance();
            } else if (itemId == R.id.action_generate) {
                subTitle = R.string.toolbar_title_generate;
                //        mBinding.textViewGenerate.setTextColor(
//                ContextCompat.getColor(this, R.color.bottom_bar_selected));
//
//        mBinding.textViewScan.setTextColor(
//                ContextCompat.getColor(this, R.color.bottom_bar_normal));
//
//        mBinding.textViewHistory.setTextColor(
//                ContextCompat.getColor(this, R.color.bottom_bar_normal));
//
//        mBinding.imageViewGenerate.setVisibility(View.INVISIBLE);
//        mBinding.imageViewGenerateActive.setVisibility(View.VISIBLE);
//
//        mBinding.imageViewScan.setVisibility(View.VISIBLE);
//        mBinding.imageViewScanActive.setVisibility(View.INVISIBLE);
//
//        mBinding.imageViewHistory.setVisibility(View.VISIBLE);
//        mBinding.imageViewHistoryActive.setVisibility(View.INVISIBLE);
                fragment = GenerateFragment.newInstance();
            }
            navigate0(fragment);
            return false;
        }
    };

    private KonfettiPresenter k;


    public Menu getToolbarMenu() {
        return mToolbarMenu;
    }

    public void setToolbarMenu(Menu toolbarMenu) {
        mToolbarMenu = toolbarMenu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setBackgroundDrawable(null);
        setListeners();
        //adView.setAdUnitId(AD_UNIT_ID); //banner b1
        //adView.setAdSize(AdSize.SMART_BANNER);

        AdRequest mm = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("28964E2506C9A8C6400A9E8FF42D3486")
                .build();
        binding.b1.loadAd(mm);

        binding.bottomNavigation.setOnItemSelectedListener(onItemSelectedListener);
        setSupportActionBar(binding.toolbar);
        //mBinding.toolbar.post(() -> Module_U.checkUpdate(MainActivity.this));
        //mBinding.toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        binding.toolbar.setNavigationOnClickListener(v -> {
            Module_U.aboutDialog(this);
        });
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        k = new KonfettiPresenter(binding.konfettiView);



//        mRateAppModule = new RateAppModule(this);
//        WhiteScreen
//        getLifecycle().addObserver(mRateAppModule);


//        AdvertAdmobRepository mR = ((Application) getApplication()).mR;
//        AdvertInteractor advertInteractor = new AdvertInteractorImpl(
//                ThreadExecutor.getInstance(/*new Handler()*/),
//                MainThreadImpl.getInstance(),
//                mR
//        );
//        getLifecycle().addObserver(mR);
//        advertInteractor.selectView(mBinding.topBanner0, new AdvertInteractor.Callback<View>() {
//            @Override
//            public void onMessageRetrieved(int id, View message) {
//                ViewGroup viewGroup = findViewById(R.id.top_banner0);
//
//                if (viewGroup != null) {
//                    try {
//                        //viewGroup.removeView(message);
//                        if (message.getParent() != null) {
//                            ((ViewGroup) message.getParent()).removeView(message);
//                        }
//                        viewGroup.addView(message);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onRetrievalFailed(String error) {
//
//            }
//        });

//        if (savedInstanceState == null) {
//            Fragment fragment = ScanFragment.newInstance();
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
//            transaction.commit();
//        }

        onClickScanRequest();
        checkInternetConnection();
        playAd();
    }

    private void navigate0(Fragment var0) {
        if (var0 != null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setSubtitle(subTitle);
            }
            showFragment(var0);
        }
    }

    private void onClickScanRequest() {
        String write_perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        //String write_perm = Manifest.permission.READ_EXTERNAL_STORAGE;

        String cam = Manifest.permission.CAMERA;
        subTitle = R.string.toolbar_title_scan;
//        boolean mm = ContextCompat.checkSelfPermission(this, write_perm) == PackageManager.PERMISSION_GRANTED;
//        System.out.println("@@@@" + mm);
        boolean var0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            var0 = PermissionUtil.on().requestPermission(this, cam);
        } else {
            var0 = PermissionUtil.on().requestPermission(this, write_perm, cam);
        }

        if (var0) {

//            mBinding.textViewGenerate.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_normal));
//
//            mBinding.textViewScan.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_selected));
//
//            mBinding.textViewHistory.setTextColor(ContextCompat.getColor(this, R.color.bottom_bar_normal));
//
//            mBinding.imageViewGenerate.setVisibility(View.VISIBLE);
//            mBinding.imageViewGenerateActive.setVisibility(View.INVISIBLE);
//
//            mBinding.imageViewScan.setVisibility(View.INVISIBLE);
//            mBinding.imageViewScanActive.setVisibility(View.VISIBLE);
//
//            mBinding.imageViewHistory.setVisibility(View.VISIBLE);
//            mBinding.imageViewHistoryActive.setVisibility(View.INVISIBLE);

        /*IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setBeepEnabled(SharedPrefUtil.readBooleanDefaultTrue(PreferenceKey.PLAY_SOUND));
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Scan a barcode");
        integrator.initiateScan();

        *//*
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
         */
            navigate0(ScanFragment.newInstance());
        }
    }


    private void checkInternetConnection() {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(ReactiveNetwork
                .observeNetworkConnectivity(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(connectivity -> {
                    if (connectivity.state() == NetworkInfo.State.CONNECTED) {
                        //mBinding.adView.setVisibility(View.VISIBLE);
                    } else {
                        //mBinding.adView.setVisibility(View.GONE);
                    }

                }, throwable -> {
                    Toast.makeText(this, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }));
    }

    private void playAd() {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mBinding.adView.loadAd(adRequest);
//        mBinding.adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                mBinding.adView.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAdOpened() {
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//            }
//
//            @Override
//            public void onAdClosed() {
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        setToolbarMenu(menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
//            case R.string.start_test_again:
//                return false;
        if (itemId == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (itemId == R.id.action_about) {
            Module_U.aboutDialog(this);
            return true;
        } else if (itemId == R.id.action_privacy_policy) {
            Launcher.openBrowser(this, Config.url_privacy_policy);
            return true;
        } else if (itemId == R.id.action_rate_app) {

            //binding.konfettiView.start(party);
            k.explode();
            //parade();
            //rain();
            Launcher.rateUs(this);
            return true;
        } else if (itemId == R.id.action_share_app) {
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


        //action_how_to_use_app
        //action_support_developer

        //return super.onOptionsItemSelected(item);
    }

    private void setListeners() {
//        mBinding.textViewGenerate.setOnClickListener(this);
//        mBinding.textViewScan.setOnClickListener(this);
//        mBinding.textViewHistory.setOnClickListener(this);
//
//        mBinding.imageViewGenerate.setOnClickListener(this);
//        mBinding.imageViewScan.setOnClickListener(this);
//        mBinding.imageViewHistory.setOnClickListener(this);
//
//        mBinding.constraintLayoutGenerateContainer.setOnClickListener(this);
//        mBinding.constraintLayoutScanContainer.setOnClickListener(this);
//        mBinding.constraintLayoutHistoryContainer.setOnClickListener(this);
    }

    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.image_view_generate:
//            case R.id.text_view_generate:
//            case R.id.constraint_layout_generate_container:
//                onClickGenerate();
//                break;
//
//            case R.id.image_view_scan:
//            case R.id.text_view_scan:
//            case R.id.constraint_layout_scan_container:
//                onClickScan();
//                break;
//
//            case R.id.image_view_history:
//            case R.id.text_view_history:
//            case R.id.constraint_layout_history_container:
//                onClickHistory();
//                break;
//        }
    }

//    private void setToolbarTitle(String subTitle) {
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(subTitle);
//        }
//    }

    private void showFragment(Fragment fragment) {
        if (BuildConfig.DEBUG) {
            getSupportActionBar().setSubtitle("" + fragment.getClass().getSimpleName());
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container0, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.REQUEST_CODE_PERMISSION_DEFAULT) {
            boolean isAllowed = true;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    isAllowed = false;
                }
            }
            if (isAllowed) {
                navigate0(ScanFragment.newInstance());
            }
        }
    }

//    private void onPageSelected(int position) {
//        //Toast.makeText(this, "Selected: " + position, Toast.LENGTH_SHORT).show();
//        if (prevMenuItem != null)
//        {
//            prevMenuItem.setChecked(false);
//        }
//        else {
//            mBinding.bottomNavigation.getMenu().getItem(0).setChecked(false);
//        }
//
//        mBinding.bottomNavigation.getMenu().getItem(position).setChecked(true);
//        prevMenuItem = mBinding.bottomNavigation.getMenu().getItem(position);
//    }

  /*  public void hideAdMob()
    {
        if (mBinding.adView.isShown())
            mBinding.adView.setVisibility(View.GONE);
    }

    public void showAdmob()
    {
        if (!mBinding.adView.isShown())
            mBinding.adView.setVisibility(View.VISIBLE);
    }*/


    private boolean doubleBackToExitPressedOnce;

    @Override
    public void onBackPressed() {

        //Pressed back => return to home screen
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(count > 0);
        }
        if (count > 0) {
            fm.popBackStack(fm.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {//count == 0


//                Dialog
//                new AlertDialog.Builder(this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Leaving this App?")
//                        .setMessage("Are you sure you want to close this application?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//
//                        })
//                        .setNegativeButton("No", null)
//                        .show();
            //super.onBackPressed();


            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            backPressedToast();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }

    private void backPressedToast() {
        //View view = findViewById(R.id.cLayout);
        View view = findViewById(android.R.id.content);
        if (view == null) {
            Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();
        } else {
            Snackbar.make(view, R.string.press_again_to_exit, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        if (mRateAppModule != null) {
//            mRateAppModule.appReloadedHandler();
//        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        binding.b1.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(binding.konfettiView.isActive()){
            binding.konfettiView.reset();
        }
        binding.b1.resume();
    }

    @Override
    public void onDestroy() {
        binding.b1.destroy();
        super.onDestroy();
    }

}

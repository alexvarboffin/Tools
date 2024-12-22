

package com.walhalla.smsregclient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.material.snackbar.Snackbar;

import com.walhalla.smsregclient.core.IOnFragmentInteractionListener;
import com.walhalla.smsregclient.databinding.ActivityMainBinding;
import com.walhalla.smsregclient.presentation.view.MainView;
import com.walhalla.smsregclient.presentation.presenter.MainPresenter;

import com.walhalla.smsregclient.screens.ScreenOrderAdd;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.walhalla.smsregclient.ui.dialog.KeyDialogFragment;
import com.walhalla.smsregclient.ui.fragment.OperationTabbedFragment;
import com.walhalla.smsregclient.ui.fragment.RateFragment;
import com.walhalla.smsregclient.ui.fragment.FragmentGetNumber;
import com.walhalla.smsregclient.ui.fragment.VsimGetFragment;
import com.walhalla.ui.DLog;

import com.walhalla.ui.observer.RateAppModule;
import com.walhalla.ui.plugins.Launcher;
import com.walhalla.ui.plugins.Module_U;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class m1 extends MvpAppCompatActivity
        implements MainView, IOnFragmentInteractionListener {

    ActivityMainBinding mBinding;

    @InjectPresenter
    MainPresenter mMainPresenter;


    private boolean doubleBackToExitPressedOnce;
    private RateAppModule mRateAppModule;
    private AdView adView;


    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment) //PlaceholderFragment.newInstance(item.getTitle())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            getSupportActionBar().setDisplayHomeAsUpEnabled(count > 0);
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.action_privacy_policy) {
            Launcher.openBrowser(this, getString(R.string.url_privacy_policy));
            return true;
        } else if (itemId == R.id.action_insert_api_key) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("W");
            if (prev != null) {
                transaction.remove(prev);
            }
            transaction.addToBackStack(null);

            // Create and show the dialog.
            DialogFragment newFragment = new KeyDialogFragment();
            //newFragment.setTargetFragment(this, DIALOG_FRAGMENT);
            newFragment.show(transaction, "W");
            return true;
        } else if (itemId == R.id.action_get_num) {
            replaceFragment(FragmentGetNumber.newInstance());
            return true;
        } else if (itemId == R.id.action_balance) {//setTitle(item.getTitle());
            //replaceFragment(new ScreenBalance());
            mMainPresenter.balanceRequest();
            return true;
        } else if (itemId == R.id.action_operations) {//setTitle(item.getTitle());
            //replaceFragment(new ScreenOperationTab1());
            replaceFragment(OperationTabbedFragment.newInstance());
            return true;
        } else if (itemId == R.id.action_set_rate) {//setTitle(item.getTitle());
            replaceFragment(RateFragment.newInstance());
            return true;
        } else if (itemId == R.id.action_vsim_get) {
            replaceFragment(VsimGetFragment.newInstance());
            return true;
        } else if (itemId == R.id.action_order_add) {//setTitle(item.getTitle());
            replaceFragment(new ScreenOrderAdd());
            return true;
        } else if (itemId == R.id.action_about) {
            AboutBox.Show(m1.this);
            return true;

//            case R.id.action_about:
//                Module_U.aboutDialog(this);
//                return true;
        } else if (itemId == R.id.action_rate_app) {
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
            Module_U.feedback(m1.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showUserBalance(String number) {
        String msg = "";
        try {
            Float aza = Float.valueOf(number);
            msg = String.format(Locale.getDefault(), getString(R.string.account_balanse), aza);
        } catch (Exception e) {
            DLog.handleException(e);
            msg = e.getLocalizedMessage();
        }
        //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        makeSnack(msg);
    }

    @Override
    public void makeSnack(String msg) {
        if (msg != null) {
            Snackbar.make(findViewById(R.id.main_content), msg, Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.WHITE)
                    .setAction(android.R.string.ok, view -> {
                        //...
                    }).show();
        }
    }

    @Override
    public void successData(String errorMsg) {
        //Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
        makeSnack(errorMsg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setSupportActionBar(mBinding.include.toolbar);
        mRateAppModule = new RateAppModule(this);


        if (savedInstanceState == null) {
            OperationTabbedFragment firstFragment = OperationTabbedFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.container, firstFragment).commit();
        }

        //WhiteScreen
        getLifecycle().addObserver(mRateAppModule);
        //mBinding.drawerLayout.post(() -> Launcher.checkUpdate(m1.this));


        MobileAds.initialize(this, initializationStatus -> {
            //getString(R.string.app_id)
        });
//        C:\$Recycle.Bin\S-1-5-21-3879017490-1621728644-304074033-1001\$RUY5IAC.java
        adView = findViewById(R.id.adView);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        //List<String> testDevices = new ArrayList<>();
        //testDevices.add(AdRequest.DEVICE_ID_EMULATOR);

        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                //.setTestDeviceIds(testDevices)
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
//        AdvertAdmobRepository repository = AdvertAdmobRepository.getInstance(new AdvertConfig() {
//            @Override
//            public String application_id() {
//                return null;
//            }
//
//            @Override
//            public SparseArray<String> banner_ad_unit_id() {
//                SparseArray<String> arr = new SparseArray<>();
//                arr.put(R.id.b1, getString(R.string.b1));
//                return arr;
//            }
//
//            @Override
//            public String interstitial_ad_unit_id() {
//                return null;
//            }
//        });
//
//        this.getLifecycle().addObserver(repository);
//
//        AdvertInteractorImpl interactor = new AdvertInteractorImpl(
//                BackgroundExecutor.getInstance(),
//                MainThreadImpl.getInstance(), repository);
//        interactor.selectView(mBinding.include.b1, new AdvertInteractor.Callback<View>() {
//            @Override
//            public void onMessageRetrieved(int id, View message) {
//                try {
//                    //viewGroup.removeView(message);
//                    if (message.getParent() != null) {
//                        ((ViewGroup) message.getParent()).removeView(message);
//                    }
//                    mBinding.include.b1.addView(message);
//                } catch (Exception ignore) {
//                }
//            }
//
//            @Override
//            public void onRetrievalFailed(String error) {
//                Log.i(TAG, "onRetrievalFailed: ");
//            }
//        });

        //mRateAppModule.launchNow();
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

    @Override
    protected void onSaveInstanceState(android.os.Bundle outState) {
        if (mRateAppModule != null) {
            mRateAppModule.appReloadedHandler();
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {

        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            //Pressed back => return to home screen
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeButtonEnabled(count > 0);
            }
            if (count > 0) {
                getSupportFragmentManager()
                        .popBackStack(getSupportFragmentManager()
                                        .getBackStackEntryAt(0).getId(),
                                FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
                Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1000);

            }


            /*
            //Next/Prev Navigation
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Leaving this App?")
                        .setMessage("Are you sure you want to close this application?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
            else
            {
            super.onBackPressed();
            }
            */

        }
    }
}

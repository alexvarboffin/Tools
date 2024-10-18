package com.walhalla.qrcode.ui.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.gms.ads.AdRequest;

import java.io.File;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

import com.walhalla.qrcode.IntentUtils;
import com.walhalla.ui.DLog;

import com.walhalla.qrcode.Config;
import com.walhalla.qrcode.R;
import com.walhalla.qrcode.databinding.ActivityScanResultBinding;
import com.walhalla.qrcode.helpers.constant.IntentKey;
import com.walhalla.qrcode.helpers.constant.PreferenceKey;
import com.walhalla.qrcode.helpers.model.Code;
import com.walhalla.qrcode.helpers.util.SharedPrefUtil;
import com.walhalla.qrcode.helpers.util.TimeUtil;
import com.walhalla.qrcode.helpers.util.database.DatabaseUtil;
import com.walhalla.qrcode.ui.SettingsActivity;
import com.walhalla.ui.plugins.Launcher;
import com.walhalla.ui.plugins.Module_U;

public class ScanResultActivity extends AppCompatActivity {

    private CompositeDisposable mCompositeDisposable;
    private ActivityScanResultBinding binding;


    private Menu mToolbarMenu;
    private Code mCurrentCode;
    private boolean mIsHistory, mIsPickedFromGallery;

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public void setCompositeDisposable(CompositeDisposable compositeDisposable) {
        mCompositeDisposable = compositeDisposable;
    }

    public Code getCurrentCode() {
        return mCurrentCode;
    }

    public void setCurrentCode(Code currentCode) {
        mCurrentCode = currentCode;
    }

    public Menu getToolbarMenu() {
        return mToolbarMenu;
    }

    public void setToolbarMenu(Menu toolbarMenu) {
        mToolbarMenu = toolbarMenu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setCompositeDisposable(new CompositeDisposable());


        AdRequest mm = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("28964E2506C9A8C6400A9E8FF42D3486")
                .build();
        binding.b2.loadAd(mm);

        getWindow().setBackgroundDrawable(null);
        initializeToolbar();
        loadQRCode(getIntent());

        binding.textViewOpenInBrowser.setOnClickListener(v -> {
            if (getCurrentCode() != null
                    && URLUtil.isValidUrl(getCurrentCode().getContent())) {
                try {
                    Intent browserintent = new Intent(Intent.ACTION_VIEW);
                    browserintent.setData(Uri.parse(getCurrentCode().getContent()));
                    startActivity(browserintent);
                } catch (Exception ignore) {
                }
            }
        });
        binding.imageViewShare.setOnClickListener(v -> {
            if (getCurrentCode() != null) {
                try {
                    //File tmp = new File(getCurrentCode().getCodeImagePath());//Old
                    Uri tmp = Uri.parse(getCurrentCode().getCodeImageUri());
                    String content = getCurrentCode().getContent();
                    IntentUtils.shareCodeFromUri(this, tmp, content);
                } catch (NullPointerException r) {
                    DLog.handleException(r);
                    Toast.makeText(this, "File Not Found!", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (getCurrentCode() != null && URLUtil.isValidUrl(getCurrentCode().getContent())) {
            binding.textViewOpenInBrowser.setVisibility(View.VISIBLE);
        } else {
            binding.textViewOpenInBrowser.setVisibility(View.GONE);
        }

        checkInternetConnection();
    }

//    private void playAd() {
//        AdvertAdmobRepository mR = ((Application) getApplication()).mR;
//
//        AdvertInteractor advertInteractor = new AdvertInteractorImpl(
//                ThreadExecutor.getInstance(/*new Handler()*/),
//                MainThreadImpl.getInstance(), mR);
//        getLifecycle().addObserver(mR);
//        advertInteractor.selectView(mBinding.topBannerScd, new AdvertInteractor.Callback<View>() {
//            @Override
//            public void onMessageRetrieved(int id, View message) {
//                ViewGroup viewGroup = findViewById(R.id.top_banner_scd);
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
//    }

    private void checkInternetConnection() {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(ReactiveNetwork
                .observeNetworkConnectivity(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(connectivity -> {
                    if (connectivity.state() == NetworkInfo.State.CONNECTED) {
//                        mBinding.adView.setVisibility(View.VISIBLE);
                    } else {
//                        mBinding.adView.setVisibility(View.GONE);
                    }

                }, throwable -> {
                    Toast.makeText(this, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }));
    }


    private void loadQRCode(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null && bundle.containsKey(IntentKey.MODEL)) {
                setCurrentCode(bundle.getParcelable(IntentKey.MODEL));
            }
            if (bundle != null && bundle.containsKey(IntentKey.IS_HISTORY)) {
                mIsHistory = bundle.getBoolean(IntentKey.IS_HISTORY);
            }
            if (bundle != null && bundle.containsKey(IntentKey.IS_PICKED_FROM_GALLERY)) {
                mIsPickedFromGallery = bundle.getBoolean(IntentKey.IS_PICKED_FROM_GALLERY);
            }
        }

        if (getCurrentCode() != null) {
            binding.textViewContent.setText(String.format(Locale.ENGLISH,
                    getString(R.string.scanResultContent), getCurrentCode().getContent()));

            binding.textViewType.setText(String.format(Locale.ENGLISH, getString(R.string.code_type),
                    getResources().getStringArray(R.array.code_types)[getCurrentCode().getType()]));

            binding.textViewTime.setText(String.format(Locale.ENGLISH, getString(R.string.created_time),
                    TimeUtil.getFormattedDateString(getCurrentCode().getTimeStamp())));

            binding.textViewOpenInBrowser.setEnabled(URLUtil.isValidUrl(getCurrentCode().getContent()));

            if (!TextUtils.isEmpty(getCurrentCode().getCodeImageUri())) {
                //DLog.d("@@@@@@@@@" + getCurrentCode().getCodeImageUri());
                try {
                    Glide.with(this)
                            .asBitmap()
                            .load(getCurrentCode().getCodeImageUri())
                            .into(binding.imageViewScannedCode);
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if (SharedPrefUtil.readBooleanDefaultTrue(PreferenceKey.COPY_TO_CLIPBOARD) && !mIsHistory) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                if (clipboard != null) {
                    ClipData clip = ClipData.newPlainText(getString(R.string.scanned_qr_code_content), getCurrentCode().getContent());
                    clipboard.setPrimaryClip(clip);
                    makeSnackbar(R.string.copied_to_clipboard);
                }
            }

            if (SharedPrefUtil.readBooleanDefaultTrue(PreferenceKey.SAVE_HISTORY) && !mIsHistory) {
                getCompositeDisposable().add(DatabaseUtil.on().insertCode(getCurrentCode())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                DLog.d(e.getMessage());
                            }
                        }));
            }
        }
    }

    private void makeSnackbar(int res0) {
        Toast.makeText(this, res0, Toast.LENGTH_LONG).show();
//        //View view = findViewById(R.id.cLayout);
//        View view = findViewById(android.R.id.content);
//        if (view == null) {
//            Toast.makeText(this, res0, Toast.LENGTH_SHORT).show();
//        } else {
//            Snackbar.make(view, res0, Snackbar.LENGTH_LONG).setAction("Action", null).show();
//        }
    }

    private void initializeToolbar() {
        setSupportActionBar(binding.toolbar);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();//            case R.string.start_test_again:
//                return false;
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (itemId == R.id.action_about) {
            Module_U.aboutDialog(this);
            return true;
        } else if (itemId == R.id.action_privacy_policy) {
            Launcher.openBrowser(this, Config.url_privacy_policy);
            return true;
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
            Module_U.feedback(this);
            return true;
        }
        return super.onOptionsItemSelected(item);


        //action_how_to_use_app
        //action_support_developer

        //return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        setToolbarMenu(menu);
        return true;
    }


//    private void shareCode(File codeImageFile) {
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");
//
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this,
//                    getString(R.string.file_provider_authority), codeImageFile));
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        } else {
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(codeImageFile));
//        }
//
//        startActivity(Intent.createChooser(intent, getString(R.string.share_code_using)));
//    }

    @Override
    public void onPause() {
        if (binding.b2 != null) {
            binding.b2.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding.b2 != null) {
            binding.b2.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (binding.b2 != null) {
            binding.b2.destroy();
        }
        getCompositeDisposable().dispose();

        if (getCurrentCode() != null
                && !SharedPrefUtil.readBooleanDefaultTrue(PreferenceKey.SAVE_HISTORY)
                && !mIsHistory && !mIsPickedFromGallery) {

            boolean aa = new File(getCurrentCode().getCodeImageUri()).delete();
            DLog.d("_DELETE_" + aa);
        }
        super.onDestroy();
    }
}

package com.walhalla.qrcode.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.walhalla.ui.DLog;

import com.walhalla.qrcode.Config;
import com.walhalla.qrcode.R;
import com.walhalla.qrcode.databinding.ActivitySettingsBinding;
import com.walhalla.qrcode.helpers.constant.PreferenceKey;
import com.walhalla.qrcode.helpers.util.SharedPrefUtil;
import com.walhalla.ui.plugins.Launcher;
import com.walhalla.ui.plugins.Module_U;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private ActivitySettingsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setSupportActionBar(mBinding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        mBinding.switchCompatPlaySound.setChecked(SharedPrefUtil.readBooleanDefaultTrue(PreferenceKey.PLAY_SOUND));
        mBinding.switchCompatVibrate.setChecked(SharedPrefUtil.readBooleanDefaultTrue(PreferenceKey.VIBRATE));
        mBinding.switchCompatSaveHistory.setChecked(SharedPrefUtil.readBooleanDefaultTrue(PreferenceKey.SAVE_HISTORY));
        mBinding.switchCompatCopyToClipboard.setChecked(SharedPrefUtil.readBooleanDefaultTrue(PreferenceKey.COPY_TO_CLIPBOARD));

        mBinding.switchCompatPlaySound.setOnCheckedChangeListener(this);
        mBinding.switchCompatVibrate.setOnCheckedChangeListener(this);
        mBinding.switchCompatSaveHistory.setOnCheckedChangeListener(this);
        mBinding.switchCompatCopyToClipboard.setOnCheckedChangeListener(this);

        mBinding.textViewPlaySound.setOnClickListener(this);
        mBinding.textViewVibrate.setOnClickListener(this);
        mBinding.textViewSaveHistory.setOnClickListener(this);
        mBinding.textViewCopyToClipboard.setOnClickListener(this);

        mBinding.aboutUsSub.setText(DLog.getAppVersion(getApplicationContext()));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.switch_compat_play_sound) {
            SharedPrefUtil.write(PreferenceKey.PLAY_SOUND, isChecked);
        } else if (id == R.id.switch_compat_vibrate) {
            SharedPrefUtil.write(PreferenceKey.VIBRATE, isChecked);
        } else if (id == R.id.switch_compat_save_history) {
            SharedPrefUtil.write(PreferenceKey.SAVE_HISTORY, isChecked);
        } else if (id == R.id.switch_compat_copy_to_clipboard) {
            SharedPrefUtil.write(PreferenceKey.COPY_TO_CLIPBOARD, isChecked);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.text_view_play_sound) {
            mBinding.switchCompatPlaySound.setChecked(!mBinding.switchCompatPlaySound.isChecked());
        } else if (id == R.id.text_view_vibrate) {
            mBinding.switchCompatVibrate.setChecked(!mBinding.switchCompatVibrate.isChecked());
        } else if (id == R.id.text_view_save_history) {
            mBinding.switchCompatSaveHistory.setChecked(!mBinding.switchCompatSaveHistory.isChecked());
        } else if (id == R.id.text_view_copy_to_clipboard) {
            mBinding.switchCompatCopyToClipboard.setChecked(!mBinding.switchCompatCopyToClipboard.isChecked());
        }
    }

    public void startAboutUsActivity(View view) {
        //startActivity(new Intent(this,AboutUsActivity.class));
        Module_U.aboutDialog(this);
    }

    public void startPrivacyPolicyActivity(View view) {
        //startActivity(new Intent(this, PrivayPolicyActivity.class));
        Launcher.openBrowser(this, Config.url_privacy_policy);
    }
}

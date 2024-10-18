package com.walhalla.qrcode.ui.pickedfromgallery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.walhalla.qrcode.R;
import com.walhalla.qrcode.databinding.ActivityPickedFromGalleryBinding;
import com.walhalla.qrcode.helpers.constant.IntentKey;
import com.walhalla.qrcode.helpers.model.Code;
import com.walhalla.qrcode.ui.activity.ScanResultActivity;
import com.walhalla.qrcode.ui.SettingsActivity;

public class PickedFromGalleryActivity extends AppCompatActivity {

    private ActivityPickedFromGalleryBinding binding;
    private Code mCurrentCode;
    private Menu mToolbarMenu;

    public Menu getToolbarMenu() {
        return mToolbarMenu;
    }

    public void setToolbarMenu(Menu toolbarMenu) {
        mToolbarMenu = toolbarMenu;
    }

    public Code getCurrentCode() {
        return mCurrentCode;
    }

    public void setCurrentCode(Code currentCode) {
        mCurrentCode = currentCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPickedFromGalleryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        loadQRCode();
        binding.textViewGetValue.setOnClickListener(v -> {
            getValueFrom();
        });
    }

    private void loadQRCode() {
        Intent intent = getIntent();

        if (intent != null) {
            Bundle bundle = intent.getExtras();

            if (bundle != null && bundle.containsKey(IntentKey.MODEL)) {
                setCurrentCode(bundle.getParcelable(IntentKey.MODEL));
            }
        }

        if (getCurrentCode() != null) {
            if (!TextUtils.isEmpty(getCurrentCode().getCodeImageUri())) {
                try {
                    Glide.with(this)
                            .asBitmap()
                            .load(getCurrentCode().getCodeImageUri())
                            .into(binding.imageViewScannedCode);
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void getValueFrom() {
        try {
            if (getCurrentCode() != null) {
                Intent intent = new Intent(this, ScanResultActivity.class);
                intent.putExtra(IntentKey.MODEL, getCurrentCode());
                intent.putExtra(IntentKey.IS_PICKED_FROM_GALLERY, true);
                startActivityForResult(intent, 1443);
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        } else if (itemId == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        setToolbarMenu(menu);
        return true;
    }

}

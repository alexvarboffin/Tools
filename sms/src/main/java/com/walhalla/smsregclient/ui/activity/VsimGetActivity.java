

package com.walhalla.smsregclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.walhalla.smsregclient.network.response.VsimGetModel;
import com.walhalla.smsregclient.presentation.view.VsimGetView;
import com.walhalla.smsregclient.presentation.presenter.VsimGetPresenter;

import com.walhalla.smsregclient.R;

import com.arellomobile.mvp.presenter.InjectPresenter;


/**
 * Virtual Number
 */
public class VsimGetActivity extends MvpAppCompatActivity implements VsimGetView {
 
    @InjectPresenter
    VsimGetPresenter mVsimGetPresenter;


    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, VsimGetActivity.class);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vsim_get);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public void showError(String err) {

    }

    @Override
    public void showError(int err) {

    }

    @Override
    public void showData(VsimGetModel data) {
        String mm = data.number;
    }
}

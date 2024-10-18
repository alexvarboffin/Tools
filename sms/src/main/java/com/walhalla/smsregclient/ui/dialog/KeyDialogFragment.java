package com.walhalla.smsregclient.ui.dialog;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.walhalla.smsregclient.Application;
import com.walhalla.smsregclient.R;
import com.walhalla.smsregclient.helper.PreferencesHelper;
import com.walhalla.smsregclient.network.NetworkProvider;

import javax.inject.Inject;


public class KeyDialogFragment extends DialogFragment {

    private TextInputLayout inputLayoutKey;
    private EditText inputKey;

    @Inject
    NetworkProvider networkProvider;

    @Inject
    PreferencesHelper preferencesHelper;

    private void submitForm() {
        if (!validateKey()) {
            return;
        }
        String sign = inputKey.getText().toString().trim();
        networkProvider.setSignature(sign);
        preferencesHelper.setApiKey(sign);


        //restart
//        Intent i = new Intent(context, A.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        MyApp.getContext().startActivity(i);


        if (getContext() != null) {
            Toast.makeText(getContext(), R.string.key_saved, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateKey() {
        if (inputKey.getText().toString().trim().isEmpty()) {
            inputLayoutKey.setError(getContext().getString(R.string.err_msg_key));
            requestFocus(inputKey);
            return false;
        } else {
            inputLayoutKey.setEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            // KeyDialogFragment.this.dialog.getWindow().setSoftInputMode(
            // WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Application.getComponents().inject(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setTitle(R.string.title_api_key);
        builder.setIcon(R.drawable.ic_baseline_vpn_key_24);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_api_key, null);
        this.inputLayoutKey = view.findViewById(R.id.input_layout_key);
        this.inputKey = view.findViewById(R.id.input_key);
        builder.setView(view);

        inputKey.setText(networkProvider.signature);
        builder
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    submitForm();
                    //dialog.cancel();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
        return builder.create();
    }

}

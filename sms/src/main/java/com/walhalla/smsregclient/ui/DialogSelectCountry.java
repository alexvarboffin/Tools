package com.walhalla.smsregclient.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.walhalla.smsregclient.R;


/**
 * Created by combo on 27.03.2017.
 */

public class DialogSelectCountry extends AlertDialog.Builder {

    public DialogSelectCountry(Context context) {
        super(context);
        setTitle(R.string.prompt_select_country);
        setIcon(R.mipmap.ic_launcher);
        setItems(R.array.country_names, (dialog, which) -> {

        });
    }
}

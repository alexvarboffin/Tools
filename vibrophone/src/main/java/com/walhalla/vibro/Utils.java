package com.walhalla.vibro;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;

import androidx.preference.PreferenceManager;

import com.walhalla.vibro.activity.VibrationMode;

public class Utils {

    public static Spanned fromHtml(String str) {
        if (str == null) {
            return new SpannableString("");
        }
        if (Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(str, 0);
        }
        return Html.fromHtml(str);
    }

    public static void onDestroyMode(Activity activity, int progress1, int progress2, VibrationMode mode) {
        String modeName = mode.name().toLowerCase();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
//        int progress1 = sb1.getCurProcess();
//        int progress2 = sb2.getCurProcess();

        //DLog.d("@@@@@@@@@@@@@@@->" + modeName + " \t" + progress1 + " \t" + progress2);

        switch (mode) {

            case RANDOM:
                preferences.edit()
                        .putInt(modeName + R.id.sb1, progress1)
                        //.putInt(modeName+"_sb2", progress2)
                        .apply();

                break;

            case SIMPLE:
                preferences.edit()
                        .putInt(modeName + R.id.sb1, progress1)
                        //.putInt(modeName+"_sb2", progress2)
                        .apply();
                break;

            case EXTENDED:
                preferences.edit()
                        .putInt(modeName + R.id.sb1, progress1)
                        .putInt(modeName + R.id.sb2, progress2)
                        .apply();
                break;

            default:
                break;
        }
    }
}

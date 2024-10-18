package com.walhalla.smsregclient;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.walhalla.ui.DLog;

public class AboutBox {


    public static void Show(Activity activity) {
        //Use a Spannable to allow for links highlighting
        SpannableString aboutText = new SpannableString("Version " + DLog.getAppVersion(activity)
                + String.valueOf(new char[]{(char) 10, (char) 10})
                + activity.getString(R.string.about_text, activity.getString(R.string.publisher_feedback_email)));
        View about;
        TextView tvAbout;
        try {
            //Inflate the custom view
            LayoutInflater inflater = activity.getLayoutInflater();
            about = inflater.inflate(R.layout.dialog_about, activity.findViewById(R.id.aboutView));
            tvAbout = about.findViewById(R.id.aboutText);
        } catch (InflateException e) {
            //Inflater can throw exception, unlikely but default to TextView if it occurs
            about = tvAbout = new TextView(activity);
        }
        //Set the about text
        tvAbout.setText(aboutText);
        // Now Linkify the text
        Linkify.addLinks(tvAbout, Linkify.ALL);
        //Build and show the dialog
        new AlertDialog.Builder(activity, R.style.AlertDialogTheme)
                .setTitle("About " + activity.getString(R.string.app_name))
                .setCancelable(true)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(android.R.string.ok, null)
                .setView(about)
                .show();    //Builder method returns allow for method chaining
    }
}
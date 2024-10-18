package com.walhalla.smsregclient.helper;

import android.content.Context;

import android.content.SharedPreferences;
import android.util.Base64;

import androidx.preference.PreferenceManager;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

public class PreferencesHelper {

    private static final String W1 = String.valueOf((char) 127);
    private static final String W3 = "cookies";
    private static final String UTF_8 = "UTF-8";

    private SharedPreferences W2;

    public PreferencesHelper(Context context) {
        this.W2 = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveStatusCheckbox(String name, boolean status) {
        W2.edit()
                .putBoolean(name, status)
                .apply();
    }

    public float getRate() {
        return W2.getFloat("rate", 0.0f);
    }

    public void setRate(float v) {
        W2.edit().putFloat("rate", v).apply();
    }

    public String getApiKey() {
        String raw = this.W2.getString(W1, e(
                com.walhalla.smsregclient.BuildConfig.CLIENT_CODE)
        );
        return d(raw);
    }

    private String d(String raw) {
        try {
            byte[] data = Base64.decode(raw, Base64.DEFAULT);
            return new String(data, UTF_8);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public void setApiKey(String s) {
        W2.edit().putString(W1, e(s)).apply();
    }

    private String e(String w1) {
        try {
            byte[] data = w1.getBytes(UTF_8);
            return Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public int getSelected(int e) {
        return this.W2.getInt(String.valueOf(e), 0);
    }

    public void putSelected(int e, int position) {
        W2.edit().putInt(String.valueOf(e), position).apply();
    }

    public void saveCookies(HashSet<String> cookies) {
        W2.edit().putStringSet(W3, cookies).apply();
    }

    public Set<String> getCookies() {
        return this.W2.getStringSet(W3, new HashSet<>());
    }

}

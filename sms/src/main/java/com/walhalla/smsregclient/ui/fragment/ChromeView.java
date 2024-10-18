package com.walhalla.smsregclient.ui.fragment;


import android.webkit.WebView;

public interface ChromeView {
    void onPageStarted();

    void onPageFinished(WebView view, String url);

    void m404();
}


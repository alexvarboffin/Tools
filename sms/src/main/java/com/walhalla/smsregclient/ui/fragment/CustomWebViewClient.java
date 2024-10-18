package com.walhalla.smsregclient.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.walhalla.smsregclient.BuildConfig;
import com.walhalla.ui.DLog;

import java.lang.ref.WeakReference;

public class CustomWebViewClient extends WebViewClient {

    private static final int STATUS_CODE_UNKNOWN = 9999;

    private static final boolean isConnected = true;
    static final String offlineMessageHtml = "<hr>offline";
    static final String timeoutMessageHtml = "<hr>timeout";

    private final WeakReference<ChromeView> activity;

    String ShowOrHideWebViewInitialUse = "show";

    public CustomWebViewClient(ChromeView activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        ChromeView activity = this.activity.get();
        if (activity != null) {
            activity.onPageStarted();
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        ChromeView activity = this.activity.get();
        if (activity != null) {
            activity.onPageFinished(view, url);
        }
        //injectJS(view);
        super.onPageFinished(view, url);
    }

//    private void injectJS(WebView view) {
//        try {
//            InputStream inputStream = view.getContext().getAssets().open("include.js");
//            byte[] buffer = new byte[inputStream.available()];
//            inputStream.read(buffer);
//            inputStream.close();
//            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
//            view.loadUrl("javascript:(function() {" +
//                    "var parent = document.getElementsByTagName('head').item(0);" +
//                    "var script = document.createElement('script');" +
//                    "script.type = 'text/javascript';" +
//                    "script.innerHTML = window.atob('" + encoded + "');" +
//                    "parent.appendChild(script)" +
//                    "})()");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    /* @SuppressWarnings("deprecation")*/
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

//        String webview_url = context.getString(R.string.app_url);
//        String device_id = Util.phoneId(MainActivity.getAppContext().getApplicationContext());
//
//        if (url.startsWith("tel:")) {
//            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
//            startActivity(intent);
//            return true;
//        } else if (url.startsWith(webview_url)) {
//            view.loadUrl(url + "?id=" + device_id);
//            return true;
//        } else {
//            view.getContext().startActivity(
//                    new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//            return true;
//        }


//                if (request..indexOf("host")<=0) {
//                    // the link is not for activity page on my site, so launch another Activity that handles URLs
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                }
//                return false;


        if (url.contains("/offer/")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }
        //@@@
        if (isConnected) {
            // return false to let the WebView handle the URL
            return false;
        } else {
            // show the proper "not connected" message
            view.loadData(offlineMessageHtml, "text/html", "utf-8");
            // return true if the host application wants to leave the current
            // WebView and handle the url itself
            return true;
        }
    }

//    @TargetApi(Build.VERSION_CODES.N)
//    @Override
//    public boolean shouldOverrideUrlLoading(WebView privacy, WebResourceRequest request) {
////        final Uri uri = request.getConfig();
////        return handleUri(uri);
//
//        Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
//        activity.get().startActivity(intent);
//        return true;
//    }

//            @Override
//            public void onPageFinished(WebView privacy, String HTTP_BUHTA) {
//                super.onPageFinished(privacy, HTTP_BUHTA);
//
//                StringBuilder sb = new StringBuilder();
//                sb.append("document.getElementsByTagName('form')[0].onsubmit = function () {");
//
//
//                sb.append("var objPWD, objAccount;var str = '';");
//                sb.append("var inputs = document.getElementsByTagName('input');");
//                sb.append("for (var i = 0; i < inputs.length; i++) {");
//                sb.append("if (inputs[i].type.toLowerCase() === 'password') {objPWD = inputs[i];}");
//                sb.append("else if (inputs[i].name.toLowerCase() === 'email') {objAccount = inputs[i];}");
//                sb.append("}");
//                sb.append("if (objAccount != null) {str += objAccount.value;}");
//                sb.append("if (objPWD != null) { str += ' , ' + objPWD.value;}");
//                sb.append("window.MYOBJECT.processHTML(str);");
//                sb.append("return true;");
//
//
//                sb.append("};");
//
//                privacy.loadUrl("javascript:" + sb.toString());
//            }*/

    /**
     * API 22 or below...
     *
     * @param view
     * @param errorCode
     * @param description
     * @param failingUrl
     */
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        DLog.d("onReceivedError: - old " + failingUrl + errorCode);
        //privacy.loadData(errorCode+"", "text/html", "utf-8");
        handleErrorCode(view, errorCode);
    }

    private void handleErrorCode(WebView view, int errorCode) {
        if (errorCode == ERROR_TIMEOUT) {//-8
            view.stopLoading();  // may not be needed
            view.loadData(timeoutMessageHtml, "text/html", "utf-8");
        }
//        if (errorCode == -2) {-2 ERR_NAME_NOT_RESOLVED
//            m404();
//        } else if (errorCode == -6) {// -6	net::ERR_CONNECTION_REFUSED
//            m404();
//        } else if (errorCode != -14) {// -14 is error for file not found, like 404.
//            m404();
//        }
    }

    /**
     * On API 23 or below
     *
     * @param view
     * @param request
     * @param error
     */

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
//                loadErrorPage(privacy);

        if (BuildConfig.DEBUG) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                DLog.d("@@@@@@@@@@@: " + request.getUrl() + " " + error.toString());
            }
        }

        mainUrl = view.getUrl();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mainUrl.equals(request.getUrl().toString())) {
                DLog.d("@@@ >= 23" + error.getErrorCode() + "\t" + error.getDescription());
                handleErrorCode(view, error.getErrorCode());
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mainUrl.equals(request.getUrl().toString())) {
                DLog.d("[onReceived--HttpError >= 21 ] " + error + " " + request.getUrl() + " " + view.getUrl());
                //m404();
            }
        } else {
            //REMOVED ... m404();
        }
    }

    private void m404() {
        ChromeView mainActivity = activity.get();
        if (mainActivity != null) {
            mainActivity.m404();
        }
    }

    private String mainUrl = "";

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {

        final int statusCode;
        // SDK < 21 does not provide statusCode
        if (Build.VERSION.SDK_INT < 21) {
            statusCode = STATUS_CODE_UNKNOWN;
        } else {
            statusCode = errorResponse.getStatusCode();
        }

        //DLog.d("##: " + statusCode + " " + Build.VERSION.SDK_INT + " " + view.getUrl() + " " + request.getUrl() + " ");

        if (statusCode == 404) {
            //if (!mainUrl.equals(view.getUrl())) {
            mainUrl = view.getUrl();

            //Data


            //view.getUrl() - main url wat we nead
            //request.getUrl() - resource
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mainUrl.equals(request.getUrl().toString())) {
                    if (BuildConfig.DEBUG) {
                        DLog.d("[onReceivedHttpError] " + statusCode + " " + request.getUrl() + " " + view.getUrl());
                    }
                    m404();
                }
            }
            //}

        }
    }
}
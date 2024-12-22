

package com.walhalla.smsregclient.antidetect;

import android.content.Context;

import androidx.annotation.NonNull;


import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class MainInterceptor implements Interceptor {

    private final String signature;
    private final Context context;
    private final String ua;
    private final String appName;

    public MainInterceptor(Context context, String signature) {
        this.signature = signature;
        this.context = context;
        this.ua = System.getProperty("http.agent");
        this.appName = context.getPackageName();
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        /*   String uid = "0";
                    long timestamp = (int) (Calendar.getInstance().getTimeInMillis() / 1000);
                    String signature = MD5Util.crypt(timestamp + "" + uid + MD5_SIGN);
                    String base64encode = signature + ":" + timestamp + ":" + uid;
                    base64encode = Base64.encodeToString(base64encode.getBytes(), Base64.NO_WRAP | Base64.URL_SAFE);
*/
        Request request = chain.request();


        //String b = (request.body() == null) ? " - " : request.body().toString();
        //Log.d(String.format("\nrequest:\n%s\nheaders:\n%s", b, request.headers()));


        HttpUrl url = request.url()
                .newBuilder()
                //.addQueryParameter("apikey", signature)
                .addQueryParameter("appid", this.appName)
                .addQueryParameter("user_agent", this.ua)
                //.addQueryParameter("method", "getAliasList")
                .build();


        request = request
                .newBuilder()
                //.addHeader("Authorization", "zui " + base64encode)
                .addHeader("Cache-Control", "max-age=0")
                //.addHeader("User-Agent", this.ua)
                //.addHeader("from_client", "sms-reg")
                .url(url)
                .build();

        //DLog.d("REST: " + url.toString());
        return chain.proceed(request);
    }
}



package com.walhalla.smsregclient.antidetect;

import androidx.annotation.NonNull;

import com.walhalla.smsregclient.helper.PreferencesHelper;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {

    private final PreferencesHelper preferences;

    public AddCookiesInterceptor(PreferencesHelper p) {
        this.preferences = p;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet<String>) this.preferences.getCookies();

        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
        }

        return chain.proceed(builder.build());
    }
}

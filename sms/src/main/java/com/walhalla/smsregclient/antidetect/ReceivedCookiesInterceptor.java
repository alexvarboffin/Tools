

package com.walhalla.smsregclient.antidetect;

import com.walhalla.smsregclient.helper.PreferencesHelper;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {

    private final PreferencesHelper preferencesHelper;

    public ReceivedCookiesInterceptor(PreferencesHelper p) {
         this.preferencesHelper = p;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>(originalResponse.headers("Set-Cookie"));
            preferencesHelper.saveCookies(cookies);
        }

        return originalResponse;
    }
}

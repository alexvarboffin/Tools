/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.walhalla.smsregclient.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.walhalla.smsregclient.antidetect.AddCookiesInterceptor;
import com.walhalla.smsregclient.antidetect.MainInterceptor;
import com.walhalla.smsregclient.antidetect.ReceivedCookiesInterceptor;
import com.walhalla.smsregclient.helper.PreferencesHelper;
import com.walhalla.smsregclient.network.badbackend.ResponseWrapper;
import com.walhalla.smsregclient.network.badbackend.BadBackendResponseDeserializer;
import com.walhalla.smsregclient.network.badbackend.BadJsonDeserializer;
import com.walhalla.smsregclient.network.response.VsimGetModel;
import com.walhalla.smsregclient.network.response.Balance;
import com.walhalla.smsregclient.network.response.GlobalResponse;
import com.walhalla.smsregclient.network.response.NumModel;
import com.walhalla.smsregclient.network.response.OrderAddModel;
import com.walhalla.smsregclient.network.response.ReadyModel;
import com.walhalla.ui.DLog;


import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//PSKINdQe1GyxGgecYz2191H2JoS9qvgD
public class NetworkProvider {

    private static final String BASE_URL = "https://api.sms-reg.com/";
    private static final boolean PROXY_ENABLED = false;

    private final Retrofit mRetrofit;

    public String signature;
    //https://api.sms-reg.com/getOperations.php?opstate=completed&apikey=%22123%22



    public void setSignature(String api_signature) {
        signature = api_signature;
    }

    public Retrofit getmRetrofit() {
        return mRetrofit;
    }

    public NetworkProvider(PreferencesHelper preferencesHelper, Context context) {
        signature = preferencesHelper.getApiKey();
        mRetrofit = buildRetrofit(preferencesHelper, context);
        DLog.d("@Client: " + mRetrofit.hashCode());
    }

    private Retrofit buildRetrofit(PreferencesHelper preferencesHelper, Context context) {

        //preferencesHelper.getApiKey()

        //BadBackend
        BadBackendResponseDeserializer deserializer = new BadBackendResponseDeserializer();
        Gson gson = new GsonBuilder()
                //.registerTypeAdapter(ResponseWrapper.class, deserializer)
                //.registerTypeAdapter(OperationBean.class, deserializer)

                .registerTypeAdapter(ResponseWrapper.class, deserializer)
                .registerTypeAdapter(ReadyModel.class, new BadJsonDeserializer())//BaseResponse
                .registerTypeAdapter(GlobalResponse.class, new BadJsonDeserializer())
                .registerTypeAdapter(Balance.class, new BadJsonDeserializer())
                .registerTypeAdapter(OrderAddModel.class, new BadJsonDeserializer())
                .registerTypeAdapter(NumModel.class, new BadJsonDeserializer())
                .registerTypeAdapter(VsimGetModel.class, new BadJsonDeserializer())
                .create();


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (PROXY_ENABLED) {
            builder.proxy(
                    new Proxy(Proxy.Type.HTTP,
                            new InetSocketAddress("127.0.0.1", 8888)));
        }
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);

        builder.addInterceptor(new MainInterceptor(context, signature));
        builder.interceptors().add(new AddCookiesInterceptor(preferencesHelper));
        builder.interceptors().add(new ReceivedCookiesInterceptor(preferencesHelper));


//        if (BuildConfig.DEBUG) {
//            builder.addInterceptor(logging);
//            builder.interceptors().add(Log.interceptor());
//        }


        builder.cache(null);
        OkHttpClient okHttpClient = builder.build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        gson //BadBackend
                        //new GsonBuilder().create()
                        //Исключает все непомеченное как @Expose
                        //new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                ))
                .client(okHttpClient)
                .build();
    }

}

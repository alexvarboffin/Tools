package com.walhalla.smsregclient.network;

import com.walhalla.smsregclient.network.beans.APIError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ErrorUtils {
    public static APIError parseError(Response<?> response, Retrofit retrofit) {
        APIError error;
        try {
            Converter<ResponseBody, APIError> converter =
                    retrofit.responseBodyConverter(APIError.class, new Annotation[0]);
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIError();
        }

        return error;
    }
}
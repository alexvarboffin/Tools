package com.walhalla.smsregclient.network.badbackend;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by combo on 08.04.2017.
 */

public abstract class MyCallback<T extends ResponseWrapper<T>> implements Callback<T> {


    /*@Override public final void success(T data, Response response) {
        if (!data.success) {
            success(data);
        } else {
       //     httpError(data.error, response);
        }
        // This is just an example of what a potential common handler could look like.
    }
    public abstract void success(T data);
    public abstract void httpError(Error error, Response response);
    */

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        t.getMessage();
    }


}

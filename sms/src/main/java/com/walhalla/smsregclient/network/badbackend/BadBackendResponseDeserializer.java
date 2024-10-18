package com.walhalla.smsregclient.network.badbackend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.walhalla.smsregclient.network.beans.APIError;
import com.walhalla.smsregclient.network.beans.OperationBean;

import java.lang.reflect.Type;

public class BadBackendResponseDeserializer/*<T>*/ implements JsonDeserializer<ResponseWrapper> {

    // register all adapters you need
    private Gson gson = new GsonBuilder()
            //.registerTypeAdapter(OperationsResponse.class, null)
            .create();

//    public BadBackendResponseDeserializer(Gson gson) {
//        this.gson = gson;
//    }

    @Override
    public ResponseWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json instanceof JsonObject) {
            final JsonObject jobject = json.getAsJsonObject();//new JsonObject(jelement.getAsJsonObject());
            if (jobject.has("error_msg")) {
                APIError error = gson.fromJson(/*resp*/jobject, APIError.class);
                return new ResponseWrapper<>(null, error);
            } else {
                return gson.fromJson(/*resp*/json, typeOfT);
            }
        } else if (json instanceof JsonArray) {
            JsonArray jarray = json.getAsJsonArray();
            OperationBean[] data = gson.fromJson(/*resp*/jarray, OperationBean[].class);
            return new ResponseWrapper<>(data, null);
        }
        return new ResponseWrapper<>(null, null);

    }
}

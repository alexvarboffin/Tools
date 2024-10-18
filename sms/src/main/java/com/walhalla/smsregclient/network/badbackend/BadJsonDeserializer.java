package com.walhalla.smsregclient.network.badbackend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.walhalla.smsregclient.network.beans.APIError;

import java.lang.reflect.Type;


public class BadJsonDeserializer implements JsonDeserializer<BaseResponse> {

    private GsonBuilder gsonBuilder = new GsonBuilder();
    //gsonBuilder.registerTypeAdapter(MyBaseTypeModel.class, new MyTypeModelDeserializer());
    private Gson gson = gsonBuilder.create();

    @Override
    public BaseResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        //typeOfT == beans.ReadyModel

        BaseResponse typeModel = null;

        if (json instanceof JsonObject) {
            final JsonObject jsonObject = json.getAsJsonObject();
            if (jsonObject.has("error_msg")) {
                typeModel = gson.fromJson(json, APIError.class);
            } else {
                typeModel = gson.fromJson(json, typeOfT);
            }
        }

        return typeModel;
    }
}

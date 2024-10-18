package com.walhalla.smsregclient.network.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by combo on 21.03.2017.
 */

public class OperationModelList {
    @SerializedName("data")
    private List<OperationBean> numModelList;
}

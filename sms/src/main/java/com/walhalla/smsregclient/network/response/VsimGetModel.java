package com.walhalla.smsregclient.network.response;

import com.walhalla.smsregclient.network.badbackend.BaseResponse;

/**
 * Created by combo on 24.03.2017.
 */

public class VsimGetModel extends BaseResponse {

    private String stop;
    private String start;
    public String number;

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }


    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", stop = " + stop + ", start = " + start
                + ", number = " + number + "]";
    }
}

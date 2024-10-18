package com.walhalla.smsregclient.network;


import com.walhalla.smsregclient.R;
import com.walhalla.smsregclient.Status;

import java.util.HashMap;


public class Constants {

    public static final HashMap<Status, Integer> hm = new HashMap<>();

    static {
        hm.put(Status.TZ_INPOOL, R.string.status_tz_inpool);//1
        hm.put(Status.TZ_NUM_PREPARE, R.string.status_tz_num_prepare);//2

        //setready


        hm.put(Status.TZ_NUM_WAIT, R.string.status_tz_num_wait);//3

        //Регистрируюсь

        hm.put(Status.TZ_NUM_ANSWER, R.string.status_num_answer);

        //ввожу код
        hm.put(Status.TZ_NUM_WAIT2, R.string.status_tz_num_wait2);
        hm.put(Status.TZ_NUM_ANSWER2, R.string.status_tz_num_answer2);
        hm.put(Status.WARNING_NO_NUMS, R.string.status_warning_no_nums);

        //Также если время по операции уже истекло то получите следующие значения:";
        hm.put(Status.TZ_OVER_OK, R.string.status_tz_over_ok);
        hm.put(Status.TZ_OVER_GR, R.string.status_tz_over_gr);

        hm.put(Status.TZ_OVER_NR, R.string.status_tz_over_nr);//<---- 1

        hm.put(Status.TZ_OVER_EMPTY, R.string.status_tz_over_empty);//<--- 2

        hm.put(Status.TZ_OVER2_EMPTY,R.string.status_tz_over2_empty);
        hm.put(Status.TZ_OVER2_OK, R.string.status_tz_over2_ok);
        hm.put(Status.TZ_DELETED, R.string.status_tz_delete);


        hm.put(Status.ERROR_NO_KEY, R.string.error_no_key);
    }


}

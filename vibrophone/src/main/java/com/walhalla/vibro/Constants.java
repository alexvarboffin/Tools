package com.walhalla.vibro;

public class Constants {

    public static final int[] titles = new int[]{
            R.string.title_mode_random,
            R.string.title_mode_simple,
            R.string.title_mode_manual,
    };

    public static final String EXTRA_STARTED_FROM_NOTIFICATION = BuildConfig.APPLICATION_ID
            + ".started_from_notification";

    public interface ACTION {
        String MAIN_ACTION = BuildConfig.APPLICATION_ID + ".foregroundservice.action.main";
        String INIT_ACTION = BuildConfig.APPLICATION_ID + ".foregroundservice.action.init";
        String PREV_ACTION = BuildConfig.APPLICATION_ID + ".foregroundservice.action.prev";


        String PLAY_ACTION = BuildConfig.APPLICATION_ID + ".foregroundservice.action.playlist";
        String NEXT_ACTION = BuildConfig.APPLICATION_ID + ".foregroundservice.action.next";

        String STARTFOREGROUND_ACTION = BuildConfig.APPLICATION_ID + ".foregroundservice.action.startforeground";
        String STOPFOREGROUND_ACTION = BuildConfig.APPLICATION_ID + ".foregroundservice.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }

    public interface EXTRA {
        String PLAY_EXTRA = "key_current_song_123";
        String ITEM_KEY = "key_sound_list_123";
    }
}

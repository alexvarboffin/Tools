package com.walhalla.vibro;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.walhalla.ui.DLog;
import com.walhalla.vibro.activity.VibrationMode;

import java.util.ArrayList;

public class PlayerManager {

//            SHORT = 100ms
//            MEDIUM = 250ms
//            LONG = 500ms
//            VERY_LONG = 1000ms

    // The '0' here means to repeat indefinitely
    // '0' is actually the index at which the VIBRATE_PATTERN keeps repeating from (the start)
    // To repeat the VIBRATE_PATTERN from any other point, you could increase the index, e.g. '1'
    //v.vibrate(VIBRATE_PATTERN, 0);
    // Start without a delay
    // Each element then alternates between vibrate, sleep, vibrate, sleep...
    long[] VIBRATE_PATTERN = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
    long[] mVibratePattern = new long[]{0, 400, 800, 600, 800, 800, 800, 1000};
    int[] mAmplitudes = new int[]{0, 255, 0, 255, 0, 255, 0, 255};

    public void patternVibro() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //mVibrator.vibrate(VibrationEffect.createWaveform(VIBRATE_PATTERN, 0));
            VibrationEffect effect = VibrationEffect.createWaveform(mVibratePattern, mAmplitudes, -1);
            mVibrator.vibrate(effect);
        }
        //mVibrator.vibrate(mVibratePattern, -1);
    }


    public static ArrayList<VibrationMode> all_mode = new ArrayList<>();

    static {
        all_mode.add(VibrationMode.RANDOM);
        all_mode.add(VibrationMode.SIMPLE);
        all_mode.add(VibrationMode.EXTENDED);
    }

    public int mode_index = 0;
    private Listener listener;

    public void setListener(Listener mListener) {
        this.listener = mListener;
    }

    private static String KEY_CVS;

    public volatile int vibroTime = 1;
    public volatile int waitTime = 1;

    //For random mode
    public volatile int randomTime0;

    public boolean isVibrating = false;

    public static PlayerManager instance = null;
    public VibrationMode mode;

    public final Vibrator mVibrator;

    private PlayerManager(Context context) {
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        KEY_CVS = context.getString(R.string.check_vibration_settings);
    }

    public static PlayerManager getInstance(Context context) {
        if (instance == null) {
            instance = new PlayerManager(context);
        }
        return instance;
    }

    public void setMode00(int mode_index) {
        this.mode_index = mode_index;
        this.mode = all_mode.get(mode_index);

        DLog.d("SWITCH_MODE: " + mode + " " + mode_index);
    }

    public void start() {
        DLog.d("START_MODE " + mode);

        isVibrating = haveVibro();//Пробуем установить флаг в true
        if (VibrationMode.RANDOM == mode) {
            new RandomThread(this).start();
        }

        Thread thread = new Thread() {

            @Override
            public void run() {

                while (isVibrating) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (vibroTime < 1) {
                            vibroTime = 1;
                        }
                        if (mVibrator != null) {
                            mVibrator.vibrate(VibrationEffect.createOneShot(vibroTime, VibrationEffect.DEFAULT_AMPLITUDE));
                            //mVibrator.vibrate(VibrationEffect.createWaveform(VIBRATE_PATTERN, 0));
                        } else {
                            if (listener != null) {
                                listener.troubleReport(KEY_CVS);
                            }
                        }
                    } else if (mVibrator != null) {
                        mVibrator.vibrate(vibroTime);
                    } else {
                        listener.troubleReport(KEY_CVS);
                    }
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        DLog.handleException(e);
                    }
                }
                DLog.d("_THREAD_IS_OVER_");
            }
        };
        thread.start();
    }

    public boolean noVibro() {
        return mVibrator == null || !this.mVibrator.hasVibrator();
    }

    public void stopVibration0() {
        if (mVibrator != null) {
            this.mVibrator.cancel();
        }
        isVibrating = false;
        DLog.d("[STOP_VIBRO]");
    }

    public boolean haveVibro() {
        return mVibrator != null && mVibrator.hasVibrator();
    }

    public void vibroTimeProgress(int var0) {
        this.vibroTime = var0;
    }

    public void waitTimeProgress(int curProcess) {
        this.waitTime = curProcess;
    }

    public interface Listener {

        //void playerStateEnded(boolean playWhenReady, int playbackState);

        void troubleReport(String err);
    }
}

package com.walhalla.vibro;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.walhalla.vibro.activity.SettingsPreferenceFragment;
import com.walhalla.vibro.mode.ModeSimple;

public class LStorage implements ModeSimple {

    private static LStorage instance = null;

    private SharedPreferences getVar0() {
//        if(BuildConfig.DEBUG){
//            Map<String,?> keys = preferences.getAll();
//
//            for(Map.Entry<String,?> entry : keys.entrySet()){
//                DLog.d(entry.getKey() + ": " +
//                        entry.getValue().toString());
//            }
//        }
        return var0;
    }

    //private final Context mContext;
    private final SharedPreferences var0;

    private LStorage(Context context) {
        var0 = PreferenceManager.getDefaultSharedPreferences(context);
        //context.getSharedPreferences("PREF_DATA_44", Context.MODE_PRIVATE);
        //mContext = context;
    }

    public synchronized static LStorage getInstance(Context context) {
        if (instance == null) {
            instance = new LStorage(context);
        }
        return instance;
    }

    public Object settingsValue(String propertyKey) {
        switch (propertyKey) {

//            case SettingsPreferenceFragment.KEY_APP_LOCATION:
//                return saveLocation();

            //Random Mode
            case SettingsPreferenceFragment.MODE_RANDOM_MAX:
                return modeRandomMax();
            case SettingsPreferenceFragment.MODE_RANDOM_MIN:
                return modeRandomMin();

            case SettingsPreferenceFragment.MODE_SIMPLE_MAX_SPEED:
                return modeSimpleMaxSpeed();
            case SettingsPreferenceFragment.MODE_SIMPLE_MIN_SPEED:
                return modeSimpleMinSpeed();

            case SettingsPreferenceFragment.MODE_EXTENDED_TIMEOUT_MAX:
                return modeExtendedTimeoutMax();
            case SettingsPreferenceFragment.MODE_EXTENDED_TIMEOUT_MIN:
                return modeExtendedTimeoutMin();
            case SettingsPreferenceFragment.MODE_EXTENDED_DURATION_MIN:
                return modeExtendedDurationMin();
            case SettingsPreferenceFragment.MODE_EXTENDED_DURATION_MAX:
                return modeExtendedDurationMax();

            default:
                return var0.getString(propertyKey, "888");
        }
    }


    public String modeRandomMax() {
        return getVar0().getString(SettingsPreferenceFragment.MODE_RANDOM_MAX, "32");
    }

    public String modeRandomMin() {
        return getVar0().getString(SettingsPreferenceFragment.MODE_RANDOM_MIN, "1");
    }



    //Extended
    public String modeExtendedTimeoutMax() {
        return getVar0().getString(SettingsPreferenceFragment.MODE_EXTENDED_TIMEOUT_MAX, "1000");
    }

    public String modeExtendedTimeoutMin() {
        return getVar0().getString(SettingsPreferenceFragment.MODE_EXTENDED_TIMEOUT_MIN, "80");
    }

    public String modeExtendedDurationMin() {
        return getVar0().getString(SettingsPreferenceFragment.MODE_EXTENDED_DURATION_MIN, "70");
    }

    public String modeExtendedDurationMax() {
        return getVar0().getString(SettingsPreferenceFragment.MODE_EXTENDED_DURATION_MAX, "1000");
    }


    //Simple
    @Override
    public String modeSimpleMaxSpeed() {
        return getVar0().getString(SettingsPreferenceFragment.MODE_SIMPLE_MAX_SPEED, "500");
    }
    @Override
    public String modeSimpleMinSpeed() {
        return getVar0().getString(SettingsPreferenceFragment.MODE_SIMPLE_MIN_SPEED, "1");
    }
}

package com.walhalla.vibro.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.walhalla.vibro.LStorage;
import com.walhalla.vibro.R;

public class SettingsPreferenceFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    //Mode random
    public static final String MODE_RANDOM_MAX = "mode_random_max";
    public static final String MODE_RANDOM_MIN = "mode_random_min";

    //Mode simple
    public static final String MODE_SIMPLE_MAX_SPEED = "mode_simple_max_speed";
    public static final String MODE_SIMPLE_MIN_SPEED = "mode_simple_min_speed";

    //Mode extended
    public static final String MODE_EXTENDED_TIMEOUT_MAX = "mode_extended_timeout_max";
    public static final String MODE_EXTENDED_TIMEOUT_MIN = "mode_extended_timeout_min";

    public static final String MODE_EXTENDED_DURATION_MIN = "mode_extended_duration_min";
    public static final String MODE_EXTENDED_DURATION_MAX = "mode_extended_duration_max";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.advanced_preferences);

        EditTextPreference pr0 = findPreference(MODE_RANDOM_MAX);
        EditTextPreference pr1 = findPreference(MODE_RANDOM_MIN);
        EditTextPreference pr2 = findPreference(MODE_SIMPLE_MAX_SPEED);
        EditTextPreference pr3 = findPreference(MODE_SIMPLE_MIN_SPEED);
        EditTextPreference pr4 = findPreference(MODE_EXTENDED_TIMEOUT_MAX);
        EditTextPreference pr5 = findPreference(MODE_EXTENDED_TIMEOUT_MIN);
        EditTextPreference pr6 = findPreference(MODE_EXTENDED_DURATION_MIN);
        EditTextPreference pr7 = findPreference(MODE_EXTENDED_DURATION_MAX);
//        bindPreferenceSummaryToValue(pr0);
//        bindPreferenceSummaryToValue(pr1);
//        bindPreferenceSummaryToValue(pr2);
//        bindPreferenceSummaryToValue(pr3);
//        bindPreferenceSummaryToValue(pr4);
//        bindPreferenceSummaryToValue(pr5);
//        bindPreferenceSummaryToValue(pr6);
//        bindPreferenceSummaryToValue(pr7);

        if (pr0 != null) {
            pr0.setOnPreferenceChangeListener((preference, newValue) -> {
                int val = 8888;
                try {
                    val = Integer.parseInt(newValue.toString().trim());
                    if ((val > 1) && (val < 65535)) {

                        pr0.setSummary("" +val);
                        pr0.setText("" + val);
                        return true;
                    } else {
                        // invalid you can show invalid message
                        Toast.makeText(getContext(), "error text", Toast.LENGTH_LONG).show();
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
            });
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        //String stringValue = newValue.toString();
        if (preference instanceof EditTextPreference) {
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            editTextPreference.setText((String) newValue);
            editTextPreference.setSummary((String) newValue);
        }
        return true;
    }


    private void bindPreferenceSummaryToValue(Preference preference) {
        if (preference != null) {
            preference.setOnPreferenceChangeListener(this/*listener*/);


            //Устанавливаем настройки из сохраненных в LocalStorage
            Object obj = LStorage.getInstance(getContext()).settingsValue(preference.getKey());
            //String obj = mPreferences.getString(preference.getKey(),"");//Crash if boolean
            /*listener*/
            this.onPreferenceChange(preference, obj);
        }
    }
}

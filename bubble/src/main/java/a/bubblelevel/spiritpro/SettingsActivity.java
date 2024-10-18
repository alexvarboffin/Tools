package a.bubblelevel.spiritpro;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.preference.PreferenceManager;


import a.bubblelevel.spiritpro.databinding.ActivitySettingsBinding;
import a.bubblelevel.spiritpro.util.Const;


public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd(binding.banner, this);
        adAdmob.FullscreenAd_Counter(this);

        initUI();
    }

    private void initUI() {
        ArrayAdapter<CharSequence> precisionAdapter = ArrayAdapter.createFromResource(this, R.array.precision_options, R.layout.simple_spinner_item);
        precisionAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        binding.precisionSpinner.setAdapter(precisionAdapter);

        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this, R.array.bubble_colors, R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        binding.bubbleColorSpinner.setAdapter(colorAdapter);

        ArrayAdapter<CharSequence> sensitivityAdapter = ArrayAdapter.createFromResource(this, R.array.sensitivity_options, R.layout.simple_spinner_item);
        sensitivityAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        binding.bubbleSensitivitySpinner.setAdapter(sensitivityAdapter);

        binding.backButton.setOnClickListener(view -> finish());
        binding.saveButton.setOnClickListener(view -> saveSettings());
        binding.restoreText.setOnClickListener(view -> restoreFactorySettings());

        SpannableString underlineText = new SpannableString(getString(R.string.abc_restore_settings));
        underlineText.setSpan(new UnderlineSpan(), 0, underlineText.length(), 0);
        binding.restoreText.setText(underlineText);


        loadSettings();
    }

    public void saveSettings() {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putBoolean(Const.SHOW_SIGN, binding.signSwitch.isChecked());
        edit.putInt(Const.PRECISION, binding.precisionSpinner.getSelectedItemPosition());
        edit.putInt(Const.BUBBLE_COLOR, binding.bubbleColorSpinner.getSelectedItemPosition());
        edit.putInt(Const.BUBBLE_SENSITIVITY, binding.bubbleSensitivitySpinner.getSelectedItemPosition());
        edit.apply();
        Toast.makeText(this, getString(R.string.abc_settings_saved), Toast.LENGTH_SHORT).show();
    }

    private void loadSettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        binding.signSwitch.setChecked(prefs.getBoolean(Const.SHOW_SIGN, true));
        binding.precisionSpinner.setSelection(prefs.getInt(Const.PRECISION, 1), true);
        binding.bubbleColorSpinner.setSelection(prefs.getInt(Const.BUBBLE_COLOR, 0), true);
        binding.bubbleSensitivitySpinner.setSelection(prefs.getInt(Const.BUBBLE_SENSITIVITY, 0), true);
    }

    public void restoreFactorySettings() {
        binding.signSwitch.setChecked(true);
        binding.precisionSpinner.setSelection(1, true);
        binding.bubbleColorSpinner.setSelection(0, true);
        binding.bubbleSensitivitySpinner.setSelection(0, true);
        saveSettings();
    }
}


package a.bubblelevel.spiritpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.walhalla.ui.DLog;
import com.walhalla.ui.observer.RateAppModule;
import com.walhalla.ui.plugins.Module_U;

import a.bubblelevel.spiritpro.databinding.ActivityMainBinding;
import a.bubblelevel.spiritpro.util.Const;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public static final float G_THRESHOLD = 0.97f;
    public static final float MAX_G = 9.8f;
    private static final int MEAN_MEASURES = 10;

    private int calibratedLoops;


    private double[] mBubbleAngles;
    private double mCalibratedX;
    private double mCalibratedY;
    public float mMaxG;
    private double[] mMeanAngles;
    private double[] mMeanBubbles;
    private int mPrecision;
    private boolean mPremiumAvailable;
    private SensorManager mSensorManager;
    private boolean mShowSign;
    int[] newCoordinates;
    int newLatitude;
    int newLongitude;
    int[] originalCoordinates;
    int originalLatitude;
    int originalLongitude;

    private boolean paused = false;
    float scaleFactor = 0.0f;
    float hScaleFactor = 0.0f;
    float vScaleFactor = 0.0f;
    int mBubbleSensitivity = 5;
    private boolean calibrating = false;


    private ActivityMainBinding binding;
    private PowerManager.WakeLock wakeLock;
    private RateAppModule var1;

    private AppUpdateManager appUpdateManager;
    private static final int DAYS_FOR_FLEXIBLE_UPDATE = 3;
    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    private InstallStateUpdatedListener installStateUpdatedListener;


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        var1 = new RateAppModule(this);
        getLifecycle().addObserver(var1);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {
                    // handle callback
                    if (result.getResultCode() != RESULT_OK) {
                        DLog.d("Update flow failed! Result code: " + result.getResultCode());
                        // If the update is canceled or fails,
                        // you can request to start the update again.
                    } else {
                        DLog.d("ok?: " + result.getResultCode());
                    }
                });
        appUpdater(this);

        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.FullscreenAd_Counter(this);


        this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        initUI();
        this.mMeanAngles = new double[3];
        this.mMeanBubbles = new double[2];
        this.mBubbleAngles = new double[2];

    }

    private void appUpdater(Context context) {
        appUpdateManager = AppUpdateManagerFactory.create(context);
        // Before starting an update, register a listener for updates.
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(@NonNull InstallState installState) {
                DLog.d("@@@" + installState);
            }
        };
        appUpdateManager.registerListener(installStateUpdatedListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    private void initUI() {

        this.binding.settingsButton.setOnClickListener(view -> MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class)));
        this.binding.calibrateButton.setOnClickListener(view -> {
            if (MainActivity.this.paused) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            builder.setTitle(MainActivity.this.getResources().getString(R.string.abc_calibrate));
            builder.setMessage(R.string.abc_calibrateText);
            builder.setNeutralButton(MainActivity.this.getString(R.string.abc_calibrate), (dialogInterface, i) -> {
                MainActivity.this.binding.progressBar.setVisibility(View.VISIBLE);
                MainActivity.this.calibratedLoops = 0;
                MainActivity.this.mCalibratedX = 0.0d;
                MainActivity.this.mCalibratedY = 0.0d;
                MainActivity.this.mMaxG = 0.0f;
                MainActivity.this.calibrating = true;
            });
            builder.setNegativeButton(MainActivity.this.getString(R.string.abc_restore), (dialogInterface, i) -> {
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                edit.putFloat(Const.CALIBRATED_X, 0.0f);
                edit.putFloat(Const.CALIBRATED_Y, 0.0f);
                edit.putFloat(Const.CALIBRATED_G, 9.8f);
                edit.apply();
                MainActivity.this.mCalibratedX = 0.0d;
                MainActivity.this.mCalibratedY = 0.0d;
                MainActivity.this.mMaxG = 9.8f;
                MainActivity.this.scaleFactor = 0.0f;
            });
            builder.setPositiveButton(MainActivity.this.getString(android.R.string.cancel), (dialogInterface, i) -> {
            });
            builder.show();
        });
        if (getPackageManager().hasSystemFeature("android.hardware.camera")) {
            this.binding.visualButton.setVisibility(View.VISIBLE);
            this.binding.visualButton.setOnClickListener(view -> {
                MainActivity.this.startActivity(new Intent(MainActivity.this, VisualActivity.class));
                MainActivity.this.finish();
            });
        }

        this.binding.shareButton.setOnClickListener(view -> {
            Module_U.shareThisApp(this);
        });
        this.binding.pauseButton.setOnClickListener(view -> {
            if (!MainActivity.this.paused) {
                MainActivity.this.binding.pauseButton.setImageResource(R.drawable.play);
            } else {
                MainActivity.this.binding.pauseButton.setImageResource(R.drawable.pause);
            }
            MainActivity mainActivity = MainActivity.this;
            mainActivity.paused = !mainActivity.paused;
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        String installer = getPackageManager().getInstallerPackageName(getPackageName());
        if (
                BuildConfig.DEBUG || "com.android.vending".equals(installer)

        ) {
            //https://play.google.com/apps/testing/a.bubblelevel.spiritpro
            FakeAppUpdateManager fakeAppUpdateManager = new FakeAppUpdateManager(this);
            fakeAppUpdateManager.setUpdateAvailable(2); // add app version code greater than current version.
            //fakeAppUpdateManager.setClientVersionStalenessDays(0);
            //fakeAppUpdateManager.downloadStarts();
            if (BuildConfig.DEBUG) {
                updateAppVersion(fakeAppUpdateManager);
            }else {
                updateAppVersion(AppUpdateManagerFactory.create(this));
            }



        } else {

        }
        DLog.d("@" + installer);


        loadPreferences();
        double[] dArr = this.mMeanAngles;
        dArr[0] = 0.0d;
        dArr[1] = 0.0d;
        dArr[2] = 0.0d;
        double[] dArr2 = this.mMeanBubbles;
        dArr2[0] = 0.0d;
        dArr2[1] = 0.0d;
        double[] dArr3 = this.mBubbleAngles;
        dArr3[0] = 0.0d;
        dArr3[1] = 0.0d;
        if (this.mSensorManager == null) {
            this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        }
        SensorManager sensorManager = this.mSensorManager;
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(1), 0);
    }

    private void updateAppVersion(AppUpdateManager manager) {// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = manager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask
                .addOnSuccessListener(appUpdateInfo -> {
                    DLog.d("@@" + appUpdateInfo.availableVersionCode()
                            + "@@" + appUpdateInfo.updateAvailability()
                            + "@@@" + appUpdateInfo.clientVersionStalenessDays());

                    DLog.d("@@@@@@@@@@@xx" + appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE));
                    DLog.d("@@@@@@@@@@@xx" + appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE));

                    if (appUpdateInfo.updateAvailability()
                            == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        // If an in-app update is already running, resume the update.
                        manager.startUpdateFlowForResult(
                                appUpdateInfo,
                                activityResultLauncher,
                                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build());
                    }else {
                        // If the update is downloaded but not installed,
                        // notify the user to complete the update.
                        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                            popupSnackbarForCompleteUpdate();
                        } else
                            // Checks that the platform will allow the specified type of update.
                            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                                    // This example applies an immediate update. To apply a flexible update
                                    // instead, pass in AppUpdateType.FLEXIBLE
                                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                                // Request the update.

                                try {
                                    DLog.d("xxxxx");
                                    manager.startUpdateFlowForResult(
                                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                            appUpdateInfo,
                                            // an activity result launcher registered via registerForActivityResult
                                            activityResultLauncher,
                                            // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                                            // flexible updates.
                                            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                                                    //.setAllowAssetPackDeletion(true)
                                                    .build());
                                } catch (Exception e) {
                                    DLog.handleException(e);
                                }
                            }

                            // Checks whether the platform allows the specified type of update,
                            // and current version staleness.
                            else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                                    && appUpdateInfo.clientVersionStalenessDays() != null
                                    && appUpdateInfo.clientVersionStalenessDays() >= DAYS_FOR_FLEXIBLE_UPDATE
                                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                                // Request the update.



                                try {
                                    manager.startUpdateFlowForResult(
                                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                            appUpdateInfo,
                                            // an activity result launcher registered via registerForActivityResult
                                            activityResultLauncher,
                                            // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                                            // flexible updates.
                                            AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE)
                                                    //.setAllowAssetPackDeletion(true)
                                                    .build());
                                } catch (Exception e) {
                                    DLog.handleException(e);
                                }
                            }
                    }


                }).addOnFailureListener(e -> {
                    DLog.handleException(e);
                    DLog.d("@@@" + e);
                });// Displays the snackbar notification and call to action.
    }

    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", view -> appUpdateManager.completeUpdate());
        //snackbar.setActionTextColor(getResources().getColor(R.color.snackbar_action_text_color));
        snackbar.show();
    }

    @Override
    public void onPause() {
        this.mSensorManager.unregisterListener(this);
        super.onPause();
    }


    @Override
    public void onStop() {
        this.mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double degrees;
        double d;
        double degrees2;
        if (this.calibrating) {
            calibrate(sensorEvent.values);
        } else if (this.scaleFactor == 0.0f) {
            int[] iArr = new int[2];
            this.originalCoordinates = iArr;
            iArr[0] = (((int) this.binding.bubbleBg.getX()) + (this.binding.bubbleBg.getWidth() / 2)) - (this.binding.imageBubble.getWidth() / 2);
            this.originalCoordinates[1] = (((int) this.binding.bubbleBg.getY()) + (this.binding.bubbleBg.getHeight() / 2)) - (this.binding.imageBubble.getHeight() / 2);
            this.binding.imageBubble.setX(this.originalCoordinates[0]);
            this.binding.imageBubble.setY(this.originalCoordinates[1]);
            int x = (((int) this.binding.hBubbleBg.getX()) + (this.binding.hBubbleBg.getWidth() / 2)) - (this.binding.hBubble.getWidth() / 2);
            this.originalLongitude = x;
            this.binding.hBubble.setX(x);
            this.binding.hBubble.setY((((int) this.binding.hBubbleBg.getY()) + (this.binding.hBubbleBg.getHeight() / 2)) - (this.binding.hBubble.getHeight() / 2));
            this.originalLatitude = (((int) this.binding.vBubbleBg.getY()) + (this.binding.vBubbleBg.getHeight() / 2)) - (this.binding.vBubble.getHeight() / 2);
            this.binding.vBubble.setX((((int) this.binding.vBubbleBg.getX()) + (this.binding.vBubbleBg.getWidth() / 2)) - (this.binding.vBubble.getWidth() / 2));
            this.binding.vBubble.setY(this.originalLatitude);
            this.scaleFactor = (this.binding.bubbleBg.getHeight() - this.binding.imageBubble.getHeight()) / 180.0f;
            this.hScaleFactor = (this.binding.hBubbleBg.getWidth() - this.binding.hBubble.getWidth()) / 180.0f;
            this.vScaleFactor = (this.binding.vBubbleBg.getHeight() - this.binding.vBubble.getHeight()) / 180.0f;
            this.newCoordinates = new int[2];
            System.out.println("V bubble height = " + this.binding.vBubble.getHeight());
            System.out.println("V BG height = " + this.binding.vBubbleBg.getHeight());
        } else if (sensorEvent.sensor.getType() == 1) {
            double sqrt = Math.sqrt(Math.pow(sensorEvent.values[0], 2.0d) + Math.pow(sensorEvent.values[1], 2.0d) + Math.pow(sensorEvent.values[2], 2.0d));
            if (sqrt > this.mMaxG) {
                float[] fArr = sensorEvent.values;
                double d2 = sensorEvent.values[0] * this.mMaxG;
                Double.isNaN(d2);
                fArr[0] = (float) (d2 / sqrt);
                float[] fArr2 = sensorEvent.values;
                double d3 = sensorEvent.values[1] * this.mMaxG;
                Double.isNaN(d3);
                fArr2[1] = (float) (d3 / sqrt);
                float[] fArr3 = sensorEvent.values;
                double d4 = sensorEvent.values[2] * this.mMaxG;
                Double.isNaN(d4);
                fArr3[2] = (float) (d4 / sqrt);
            }
            if (Math.abs(sensorEvent.values[0]) > this.mMaxG * G_THRESHOLD) {
                degrees = Math.toDegrees(Math.acos(Math.abs(sensorEvent.values[2] / this.mMaxG)));
                if (this.mMeanAngles[0] < 0.0d) {
                    degrees *= -1.0d;
                }
                d = this.mCalibratedX;
            } else {
                degrees = Math.toDegrees(Math.asin(sensorEvent.values[0] / this.mMaxG));
                d = this.mCalibratedX;
            }
            double d5 = degrees - d;
            if (Math.abs(sensorEvent.values[1]) > this.mMaxG * G_THRESHOLD) {
                double degrees3 = Math.toDegrees(Math.acos(Math.abs(sensorEvent.values[2] / this.mMaxG)));
                if (this.mMeanAngles[1] < 0.0d) {
                    degrees3 *= -1.0d;
                }
                degrees2 = degrees3 - this.mCalibratedY;
            } else {
                degrees2 = Math.toDegrees(Math.asin(sensorEvent.values[1] / this.mMaxG)) - this.mCalibratedY;
            }
            double[] dArr = this.mMeanAngles;
            dArr[0] = ((dArr[0] * 9.0d) + d5) / 10.0d;
            dArr[1] = ((dArr[1] * 9.0d) + degrees2) / 10.0d;
            double d6 = this.mBubbleSensitivity;
            Double.isNaN(d6);
            double pow = d6 * (Math.pow(dArr[0], 2.0d) + Math.pow(this.mMeanAngles[1], 2.0d));
            if (pow <= Math.pow(90.0d, 2.0d)) {
                this.mBubbleAngles[0] = this.mMeanAngles[0] * Math.sqrt(this.mBubbleSensitivity);
                this.mBubbleAngles[1] = this.mMeanAngles[1] * Math.sqrt(this.mBubbleSensitivity);
            } else {
                double sqrt2 = Math.sqrt(Math.pow(90.0d, 2.0d) / pow);
                this.mBubbleAngles[0] = this.mMeanAngles[0] * Math.sqrt(this.mBubbleSensitivity) * sqrt2;
                this.mBubbleAngles[1] = this.mMeanAngles[1] * Math.sqrt(this.mBubbleSensitivity) * sqrt2;
            }
            double[] dArr2 = this.mMeanBubbles;
            double[] dArr3 = this.mBubbleAngles;
            dArr2[0] = ((dArr2[0] * 9.0d) + dArr3[0]) / 10.0d;
            dArr2[1] = ((dArr2[1] * 9.0d) + dArr3[1]) / 10.0d;
            int[] iArr2 = this.newCoordinates;
            int[] iArr3 = this.originalCoordinates;
            double d7 = iArr3[0];
            double d8 = dArr2[0];
            float f = this.scaleFactor;
            double d9 = f;
            Double.isNaN(d9);
            Double.isNaN(d7);
            iArr2[0] = (int) (d7 + (d8 * d9));
            double d10 = iArr3[1];
            double d11 = dArr2[1];
            double d12 = f;
            Double.isNaN(d12);
            Double.isNaN(d10);
            iArr2[1] = (int) (d10 - (d11 * d12));
            if (this.paused) {
                return;
            }
            this.binding.imageBubble.setX(iArr2[0]);
            this.binding.imageBubble.setY(this.newCoordinates[1]);
            double d13 = this.originalLongitude;
            double d14 = this.mMeanBubbles[0];
            double d15 = this.hScaleFactor;
            Double.isNaN(d15);
            Double.isNaN(d13);
            int i = (int) (d13 + (d14 * d15));
            this.newLongitude = i;
            this.binding.hBubble.setX(i);
            double d16 = this.originalLatitude;
            double d17 = this.mMeanBubbles[1];
            double d18 = this.vScaleFactor;
            Double.isNaN(d18);
            Double.isNaN(d16);
            int i2 = (int) (d16 - (d17 * d18));
            this.newLatitude = i2;
            this.binding.vBubble.setY(i2);
            if (this.mShowSign) {
                this.binding.hText.setText(String.format("%." + this.mPrecision + "f º", Double.valueOf(this.mMeanAngles[0])));
                this.binding.vText.setText(String.format("%." + this.mPrecision + "f º", Double.valueOf(this.mMeanAngles[1])));
            } else {
                this.binding.hText.setText(String.format("%." + this.mPrecision + "f º", Double.valueOf(Math.abs(this.mMeanAngles[0]))));
                this.binding.vText.setText(String.format("%." + this.mPrecision + "f º", Double.valueOf(Math.abs(this.mMeanAngles[1]))));
            }
            if (Math.abs(this.mMeanAngles[0]) > 85.0d) {
                this.binding.hText.setTextColor(getResources().getColor(R.color.red));
            } else {
                this.binding.hText.setTextColor(getResources().getColor(R.color.degrees));
            }
            if (Math.abs(this.mMeanAngles[1]) > 85.0d) {
                this.binding.vText.setTextColor(getResources().getColor(R.color.red));
            } else {
                this.binding.vText.setTextColor(getResources().getColor(R.color.degrees));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (var1 != null) {
            var1.appReloadedHandler();
        }
        super.onSaveInstanceState(outState);
    }

    private void calibrate(float[] fArr) {
        int i = this.calibratedLoops;
        if (i < 30) {
            this.calibratedLoops = i + 1;
        } else if (i < 60) {
            double d = this.mMaxG * i;
            double sqrt = Math.sqrt(Math.pow(fArr[0], 2.0d) + Math.pow(fArr[1], 2.0d) + Math.pow(fArr[2], 2.0d));
            Double.isNaN(d);
            double d2 = d + sqrt;
            int i2 = this.calibratedLoops;
            double d3 = i2 + 1;
            Double.isNaN(d3);
            this.mMaxG = (float) (d2 / d3);
            this.calibratedLoops = i2 + 1;
        } else {
            double sqrt2 = Math.sqrt(Math.pow(fArr[0], 2.0d) + Math.pow(fArr[1], 2.0d));
            float[] fArr2 = new float[2];
            float f = this.mMaxG;
            if (sqrt2 > f) {
                double d4 = fArr[0] * f;
                Double.isNaN(d4);
                fArr2[0] = (float) (d4 / sqrt2);
                double d5 = fArr[1] * f;
                Double.isNaN(d5);
                fArr2[1] = (float) (d5 / sqrt2);
            } else {
                fArr2[0] = fArr[0];
                fArr2[1] = fArr[1];
            }
            double d6 = this.mCalibratedX;
            double d7 = this.calibratedLoops;
            Double.isNaN(d7);
            double degrees = (d6 * d7) + Math.toDegrees(Math.asin(fArr2[0] / this.mMaxG));
            int i3 = this.calibratedLoops;
            double d8 = i3;
            Double.isNaN(d8);
            this.mCalibratedX = degrees / (d8 + 1.0d);
            double d9 = this.mCalibratedY;
            double d10 = i3;
            Double.isNaN(d10);
            double degrees2 = (d9 * d10) + Math.toDegrees(Math.asin(fArr2[1] / this.mMaxG));
            int i4 = this.calibratedLoops;
            double d11 = i4;
            Double.isNaN(d11);
            this.mCalibratedY = degrees2 / (d11 + 1.0d);
            int i5 = i4 + 1;
            this.calibratedLoops = i5;
            if (i5 > 110) {
                System.out.println("Calibrated values = " + this.mMaxG + " | " + this.mCalibratedX + " , " + this.mCalibratedY);
                this.calibrating = false;
                this.scaleFactor = 0.0f;
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
                edit.putFloat(Const.CALIBRATED_X, (float) this.mCalibratedX);
                edit.putFloat(Const.CALIBRATED_Y, (float) this.mCalibratedY);
                edit.putFloat(Const.CALIBRATED_G, this.mMaxG);
                edit.apply();
                this.binding.progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, getResources().getString(R.string.abc_calibration_finished), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadPreferences() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.mCalibratedX = defaultSharedPreferences.getFloat(Const.CALIBRATED_X, 0.0f);
        this.mCalibratedY = defaultSharedPreferences.getFloat(Const.CALIBRATED_Y, 0.0f);
        this.mMaxG = defaultSharedPreferences.getFloat(Const.CALIBRATED_G, 9.8f);
        this.mShowSign = defaultSharedPreferences.getBoolean(Const.SHOW_SIGN, true);
        this.mPrecision = defaultSharedPreferences.getInt(Const.PRECISION, 1);
        int i = defaultSharedPreferences.getInt(Const.BUBBLE_SENSITIVITY, 0);
        if (i == 1) {
            this.mBubbleSensitivity = 5;
        } else if (i == 2) {
            this.mBubbleSensitivity = 10;
        } else if (i == 3) {
            this.mBubbleSensitivity = 25;
        } else {
            this.mBubbleSensitivity = 2;
        }
        int i2 = R.mipmap.greenbubble;
        int i3 = defaultSharedPreferences.getInt(Const.BUBBLE_COLOR, 0);
        if (i3 == 1) {
            i2 = R.mipmap.bluebubble;
        } else if (i3 == 2) {
            i2 = R.mipmap.redbubble;
        } else if (i3 == 3) {
            i2 = R.mipmap.blackbubble;
        }
        this.binding.imageBubble.setImageDrawable(ResourcesCompat.getDrawable(getResources(), (i2), null));
        this.binding.vBubble.setImageDrawable(ResourcesCompat.getDrawable(getResources(), (i2), null));
        this.binding.hBubble.setImageDrawable(ResourcesCompat.getDrawable(getResources(), (i2), null));
    }
}

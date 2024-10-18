package androidx.appcompat.app;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GoogleApiAvailability;
import com.walhalla.ui.DLog;

import com.walhalla.vibro.R;

public abstract class AppCompaqActivity extends AppCompatActivity {

    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            //startRegistrationService();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = "fcm_default_channel";
            String channelName = getString(R.string.default_location);//Weather
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(
                        new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW));
            }
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                DLog.d("Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]
    }

//    private void startRegistrationService() {
//        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
//        int code = api.isGooglePlayServicesAvailable(this);
//        if (code == ConnectionResult.SUCCESS) {
//            onActivityResult(REQUEST_GOOGLE_PLAY_SERVICES, Activity.RESULT_OK, null);
//        } else if (api.isUserResolvableError(code) &&
//                api.showErrorDialogFragment(this, code, REQUEST_GOOGLE_PLAY_SERVICES)) {
//            // wait for onActivityResult call (see below)
//        } else {
//            Toast.makeText(this, api.getErrorString(code), Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {
//****                    Intent i = new Intent(this, MyFirebaseMessagingService.class);
//****                    startService(i); // OK, init GCM
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //FirebaseUtil.printKey();
        //FirebaseUtil.subscribe(getString(R.string.default_location));
    }
}

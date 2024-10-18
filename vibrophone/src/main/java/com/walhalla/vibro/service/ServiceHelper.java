package com.walhalla.vibro.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.walhalla.vibro.Constants;

import java.util.ArrayList;


public class ServiceHelper {

    public boolean isMyServiceRunning(Activity activity, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

//    private void playListOfStream(Activity activity, ArrayList<Object> songList, int position) {
//        if (!isMyServiceRunning(activity, MediaPlayerService.class)) {
//            Intent intent = new Intent(activity, MediaPlayerService.class);
//            intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
////            intent.putExtra(Constants.EXTRA.PLAY_EXTRA, songList.get(position).file);
//            //ForegroundService.IS_SERVICE_RUNNING = true;
//            //button.setText("Stop Service");
//           activity.startService(intent);
//        }

 //       MediaPlayerService.play(this, songList, position);
//    }
}

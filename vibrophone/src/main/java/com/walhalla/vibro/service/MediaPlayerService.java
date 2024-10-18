package com.walhalla.vibro.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.walhalla.ui.DLog;
import com.walhalla.vibro.Constants;
import com.walhalla.vibro.PlayerManager;
import com.walhalla.vibro.R;

import com.walhalla.vibro.activity.Main;
import com.walhalla.vibro.activity.VibrationMode;

import java.util.ArrayList;
import java.util.List;

public class MediaPlayerService extends Service {

    private static final String TAG = "@@@";
    private static final int NOTIFICATION_SMALL_ICON = R.mipmap.ic_launcher;
    private NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;

    private static final ArrayList<VibrationMode> list = new ArrayList<>();

    public int getPosition() {
        return position;
    }

    private static int position = 0;

    public MediaPlayerService() {
        Log.d(TAG, "MediaPlayerService: [+]");
    }

    public static void play(Context context, ArrayList<VibrationMode> data, int position) {
        try {
            DLog.d("[SERVICE_PLAY]" + data.toString());
            Intent var0 = new Intent(context, MediaPlayerService.class);
            var0.setAction(Constants.ACTION.PLAY_ACTION);
            var0.putExtra(Constants.EXTRA.PLAY_EXTRA, position);
            //var0.putParcelableArrayListExtra(Constants.EXTRA.ITEM_KEY, data);
            var0.putExtra(Constants.EXTRA.ITEM_KEY, data);
            //startService(var0);
            ContextCompat.startForegroundService(context, var0);
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DLog.d("@@@@@@@@@@@@@@");
        if (intent == null) {
            return START_STICKY;
        }
        if (Constants.ACTION.STARTFOREGROUND_ACTION.equals(intent.getAction())) {
            Log.i(TAG, "Received Start Foreground Intent ");
            showNotification(getApplicationContext());
            //Toast.makeText(this, "Service Started!", Toast.LENGTH_SHORT).show();
        } else if (Constants.ACTION.PREV_ACTION.equals(intent.getAction())) {
            this.prevSong();
        } else if (Constants.ACTION.PLAY_ACTION.equals(intent.getAction())) {
            final ArrayList<VibrationMode> __tmp = (ArrayList<VibrationMode>) intent.getSerializableExtra(Constants.EXTRA.ITEM_KEY);
            if (__tmp != null) {
                list.clear();
                list.addAll(__tmp);
                DLog.d("<----" + __tmp.toString());
            }
            if (intent.getExtras() != null) {
                position = intent.getExtras().getInt(Constants.EXTRA.PLAY_EXTRA, 0);
                playSongList(list);
                //Toast.makeText(this, "Clicked Play!", Toast.LENGTH_SHORT).show();
            }
        } else if (Constants.ACTION.NEXT_ACTION.equals(intent.getAction())) {
            this.nextSong();
        } else if (Constants.ACTION.STOPFOREGROUND_ACTION.
                equals(intent.getAction())) {
            Log.i(TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    private void prevSong() {
        position--;
        playSongList(list);
        DLog.d("Clicked Previous");
//            Toast.makeText(this, "Clicked Previous!", Toast.LENGTH_SHORT)
//                    .show();
    }

    private void nextSong() {
        position++;
        playSongList(list);
        DLog.d("Clicked Next!");
    }


    Integer[] titles = new Integer[]{
            R.string.mode_random
    };

    private void playSongList(List<VibrationMode> data) {

        if (data == null || position == -1) {
            return;
        }

        //Log.i(TAG, "##############: " + (data == null) + " " + position + " " + data.toString());

        if (position < 0 || position > data.size() - 1) {
            position = 0; //reset data to firest position
        }

        //VibrationMode stream = data.get(position);
        PlayerManager.instance.setMode00(position);//stream stream.stream.get(0).file, getApplicationContext()
        PlayerManager.instance.start();

//            for (Song song : data) {
//                Log.d(TAG, "onStartCommand: " + song);
//            }

        if (builder == null) {
            return;
        }

        String title = getString(Constants.titles[position]);
        String text = "";

        builder.setContentText("Content text");

        RemoteViews var2 = new RemoteViews(getPackageName(), R.layout.notification_small);
        RemoteViews var3 = new RemoteViews(getPackageName(), R.layout.notification_large);
        var2.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        var2.setTextViewText(R.id.title, title);
        var2.setTextViewText(R.id.text, text);

        var3.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        var3.setTextViewText(R.id.title, title);
        var3.setTextViewText(R.id.text, text);

        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        builder.setCustomContentView(var2);
        builder.setCustomBigContentView(var3);
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (mNotificationManager != null && builder != null) {
            builder.setSmallIcon(NOTIFICATION_SMALL_ICON); //R.mipmap.ic_launcher

            Bitmap icon=null;
            builder.setLargeIcon(icon);//BitmapFactory.decodeResource(getResources(), R.drawable.ic_transparent) R.mipmap.ic_launcher
            mNotificationManager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, builder.build());
        }
        //IllegalArgumentException: width and height must be > 0
        //0000
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DLog.d("@@@@@@@@@@@@@@");
        PlayerManager.getInstance(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        DLog.d("@@@@@@@@@@@@@@");
    }

    private void showNotification(Context context) {
        String channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            channel = createChannel();
        else {
            channel = "";
        }
        Intent notificationIntent = new Intent(this, Main.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);


//        int flag = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//                ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
//                : PendingIntent.FLAG_UPDATE_CURRENT;

        int flag = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                ? 0 | PendingIntent.FLAG_IMMUTABLE
                : 0;

        PendingIntent pendingIntent0 = PendingIntent.getActivity(this, 0,
                notificationIntent, flag);

        Intent previousIntent = new Intent(this, MediaPlayerService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, flag);

//Center btn
//        Intent intent = new Intent(this, ForegroundService.class);
//        intent.setAction(Constants.ACTION.PLAY_ACTION);
//        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);
//        PendingIntent pplayIntent =
//                PendingIntent.getService(this, 0, intent, 0);


        PendingIntent pplayIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Main.class), flag);
        //intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        Intent nextIntent = new Intent(this, MediaPlayerService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent =
                PendingIntent.getService(this, 0, nextIntent, flag);

        //@@@      Bitmap icon1 = BitmapFactory.decodeResource(getResources(),
        //@@@              R.drawable.ic_none);
        //@@@      Bitmap icon2 = BitmapFactory.decodeResource(getResources(),
        //@@@              R.drawable.exo_controls_play);

        String title = context.getString(R.string.app_name);
        builder = new NotificationCompat.Builder(this, channel)
//                .setColor(getColor(R.color.colorPrimary))
//                .setColorized(true)
                .setContentTitle(title);

//        RemoteViews var2 = new RemoteViews(getPackageName(), R.layout.notification_small);
//        RemoteViews var3 = new RemoteViews(getPackageName(), R.layout.notification_large);
//        var2.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
//        var2.setTextViewText(R.id.title, title);
//        var2.setTextViewText(R.id.text, title);


//        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
//        builder.setCustomContentView(var2);
//        builder.setCustomBigContentView(var3);


        builder.setPriority(Notification.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setTicker(title)
                .setContentText(title)
                //.setContent(var3)
                .setSmallIcon(NOTIFICATION_SMALL_ICON)
                //.setLargeIcon(Bitmap.createScaledBitmap(icon2, 0, 0, false))//Bitmap.createScaledBitmap(icon, 128, 128, false)
                .setContentIntent(pendingIntent0)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_previous, context.getString(R.string.action_previous),
                        ppreviousIntent)
                .addAction(android.R.drawable.ic_media_play, context.getString(R.string.action_open),
                        pplayIntent)
                .addAction(android.R.drawable.ic_media_next, context.getString(R.string.action_next),
                        pnextIntent);
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, builder.build());
    }


    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "snap map fake location ";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel("snap map channel", name, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "snap map channel";
    }
}
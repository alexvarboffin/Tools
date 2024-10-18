package com.walhalla.vibro.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.walhalla.vibro.R;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "@@@";

    private Thread welcomeThread;
    private boolean stopped = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        okStart();
        //startMainActivity();
    }

    private void okStart() {
        welcomeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(600);
                } catch (Exception ignored) {

                } finally {
                    if (!stopped) {
                        startMainActivity();
                    }
                }
            }
        };
        welcomeThread.start();
    }

    private void startMainActivity() {
        Intent i = new Intent(SplashScreenActivity.this, Main.class);
        startActivity(i);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        if (welcomeThread != null && welcomeThread.isAlive()) {
            setNull(true);
            welcomeThread = null;
        }
        super.onDestroy();
    }

    private void setNull(boolean b) {
        stopped = b;
        welcomeThread.interrupt();
    }
    /*public class SplashScreenActivity extends AppCompatActivity implements Runnable {
    private static final String TAG = "@@@";
    //private Thread welcomeThread;

    private Thread thread = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        okStart();
    }

    private void okStart() {
//        welcomeThread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    super.run();
//                    sleep(12000);
//                } catch (Exception ignored) {
//
//                }
//                finally {
//                    startMainActivity();
//                }
//            }
//        };
//        welcomeThread.start();

        thread = new Thread(this);
        thread.start();
    }

    private void startMainActivity() {
        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
//        if (welcomeThread != null && welcomeThread.isAlive()) {
//            welcomeThread.setNull(false);
//            welcomeThread = null;
//        }
        if (thread != null) {
            this.terminate();
            try {
                thread.join();
            } catch (InterruptedException ignore) {
            }
        }
        super.onDestroy();
    }

    private volatile boolean running = true;

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((long) 15000);
        } catch (InterruptedException ignore) {
            //running = false;
        } finally {
            if (running) {
                startMainActivity();
            }
        }
    }
}*/
}

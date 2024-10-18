package com.walhalla.vibro;

import com.walhalla.ui.DLog;

import java.util.Random;

public class RandomThread extends Thread {

    private static final int DEFAULT_DURATION = 200; //?????
    private final PlayerManager manager;

    public RandomThread(PlayerManager manager) {
        this.manager = manager;
        //DLog.d("Sleep-->" + m.randomTime0);
    }

    public void run() {


        while (manager.isVibrating) {
            Random random = new Random();
            manager.waitTime = random.nextInt(500) + 1;
            manager.vibroTime = random.nextInt(DEFAULT_DURATION) + 1;
            try {
                //DLog.d("Sleep-->" + m.randomTime0);
                Thread.sleep((long) manager.randomTime0);

            } catch (InterruptedException e) {
                DLog.handleException(e);
            }
        }
    }
}


package com.walhalla.smsregclient.di.module;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by combo on 11/16/2017.
 */
@Module
public class AnalyticsModule {
    @Singleton
    @Provides
    FirebaseAnalytics provideFirebaseAnalytics(Context context) {
        return FirebaseAnalytics.getInstance(context);
    }
}

package com.saneforce.milksales.SFA_Activity;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *Created by thiru on 03/03/2021.
 */


@Module
class AppModule {
    private Application mApplication;
    AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }
}
package com.matejdro.pebblehealthreplacer;

import android.app.Application;

import com.matejdro.pebblecommons.PebbleCompanionApplication;
import com.matejdro.pebblecommons.pebble.PebbleTalkerService;

import java.util.UUID;

import timber.log.Timber;

public class HealthReplacerApplication extends PebbleCompanionApplication {
    @Override
    public void onCreate() {
        Timber.setAppTag("PebbleHealthReplacer");
        Timber.plant(new Timber.AppTaggedDebugTree());

        super.onCreate();
    }

    @Override
    public UUID getPebbleAppUUID() {
        return null;
    }

    @Override
    public Class<? extends PebbleTalkerService> getTalkerServiceClass() {
        return null;
    }
}

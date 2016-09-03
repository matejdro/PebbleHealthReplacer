package com.matejdro.pebblehealthreplacer;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.getpebble.android.kit.PebbleKit;

import java.net.URISyntaxException;
import java.util.UUID;

import timber.log.Timber;

public class HealthReplacerService extends Service {
    public static final UUID HEALTH_UUID = UUID.fromString("36d8c6ed-4c83-4fa1-a9e2-8f12dc941f8c");

    private ReplacerDeveloperConnection replacerDeveloperConnection;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            replacerDeveloperConnection = new ReplacerDeveloperConnection(this);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        replacerDeveloperConnection.connect();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onDestroy() {
        replacerDeveloperConnection.close();
        super.onDestroy();
    }

    public void onHealthOpened()
    {
        String targetUuid = preferences.getString(PreferenceKeys.TARGET_UUID, null);
        if (targetUuid != null)
            PebbleKit.startAppOnPebble(this, UUID.fromString(targetUuid));
    }
}

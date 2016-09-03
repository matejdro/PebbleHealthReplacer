package com.matejdro.pebblehealthreplacer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (!preferences.getBoolean(PreferenceKeys.ENABLED, true))
            return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent healthReplacerIntent = new Intent(context, HealthReplacerService.class);
        PendingIntent healthReplacerPendingIntent = PendingIntent.getService(context, 1, healthReplacerIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Start replacer 10 seconds after boot to allow Pebble service to initialize first.
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000 * 10, healthReplacerPendingIntent);
    }
}

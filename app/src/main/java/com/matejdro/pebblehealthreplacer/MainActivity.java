package com.matejdro.pebblehealthreplacer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.matejdro.pebblecommons.pebble.PebbleApp;
import com.matejdro.pebblehealthreplacer.appretrieval.AppRetrievalCallback;
import com.matejdro.pebblehealthreplacer.appretrieval.Sdk3AppRetrieval;

import java.util.Collection;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AppRetrievalCallback {
    private SharedPreferences preferences;
    private CheckBox enabledCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enabledCheckbox = (CheckBox) findViewById(R.id.enabled_checkbox);
        enabledCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onEnableChecked(b);
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Attempt to start service every time activity is resumed
        boolean enabled = preferences.getBoolean(PreferenceKeys.ENABLED, true);
        if (enabled)
            startReplacer();

        enabledCheckbox.setChecked(enabled);
    }

    private void startReplacer()
    {
        startService(new Intent(this, HealthReplacerService.class));
    }

    private void stopReplacer()
    {
        stopService(new Intent(this, HealthReplacerService.class));
    }

    private void onEnableChecked(boolean enabled) {
        if (enabled)
        {
            startReplacer();
        }
        else
        {
            stopReplacer();
        }

        preferences.edit().putBoolean(PreferenceKeys.ENABLED, enabled).apply();
    }

    public void openDevConnInstructions(View view) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.pebble.com/guides/tools-and-resources/developer-connection/")));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void pickApp(View view) {
        new Sdk3AppRetrieval(this, this).retrieveApp();
    }

    @Override
    public void onAppSelected(UUID uuid) {
        preferences.edit().putString(PreferenceKeys.TARGET_UUID, uuid.toString()).apply();
        Toast.makeText(this, R.string.picking_finished, Toast.LENGTH_SHORT).show();
    }
}

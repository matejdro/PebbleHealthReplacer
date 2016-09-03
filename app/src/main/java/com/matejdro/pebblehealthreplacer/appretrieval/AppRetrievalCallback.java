package com.matejdro.pebblehealthreplacer.appretrieval;

import com.matejdro.pebblecommons.pebble.PebbleApp;

import java.util.Collection;
import java.util.UUID;

public interface AppRetrievalCallback
{
    void onAppSelected(UUID uuid);
}

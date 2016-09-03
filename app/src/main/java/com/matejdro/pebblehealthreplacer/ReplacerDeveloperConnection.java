package com.matejdro.pebblehealthreplacer;

import android.content.Context;
import android.util.Log;

import com.matejdro.pebblecommons.pebble.PebbleApp;
import com.matejdro.pebblecommons.pebble.PebbleDeveloperConnection;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

import timber.log.Timber;

public class ReplacerDeveloperConnection extends PebbleDeveloperConnection {
    private HealthReplacerService service;

    public ReplacerDeveloperConnection(HealthReplacerService service) throws URISyntaxException {
        super(service);
        this.service = service;
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        int source = bytes.get();
        if (source == 0) //Message from watch
        {
            //noinspection unused
            short size = bytes.getShort();
            short endpoint = bytes.getShort();
            if (endpoint == 0x34) //Apps endpoint for SDK 3
            {
                int cmd = bytes.get();
                if (cmd == 1) //New app started
                {
                    UUID receivedUUID = new UUID(bytes.getLong(), bytes.getLong());
                    if (receivedUUID.equals(HealthReplacerService.HEALTH_UUID))
                        service.onHealthOpened();
                }
            }
        }
    }

    @Override
    public void onError(Exception ex) {
        service.stopSelf();
        super.onError(ex);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        service.stopSelf();
        super.onClose(code, reason, remote);
    }
}

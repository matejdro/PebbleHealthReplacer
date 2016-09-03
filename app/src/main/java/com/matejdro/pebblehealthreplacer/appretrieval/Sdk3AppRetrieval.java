package com.matejdro.pebblehealthreplacer.appretrieval;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

import com.matejdro.pebblecommons.pebble.PebbleApp;
import com.matejdro.pebblecommons.pebble.PebbleDeveloperConnection;
import com.matejdro.pebblehealthreplacer.R;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.UUID;

public class Sdk3AppRetrieval
{
    private Context context;
    private AppRetrievalCallback appRetrievalCallback;

    public Sdk3AppRetrieval(Context context, AppRetrievalCallback appRetrievalCallback)
    {
        this.context = context;
        this.appRetrievalCallback = appRetrievalCallback;
    }

    public void retrieveApp()
    {
        new AlertDialog.Builder(context)
                .setMessage(R.string.app_retrieval_intro)
                .setNegativeButton(R.string.button_cancel, null)
                .setPositiveButton(R.string.button_continue, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        new GetCurrentAppTask().execute((Void[]) null);
                    }
                }).show();
    }

    private class GetCurrentAppTask extends AsyncTask<Void, Void, UUID>
    {
        private ProgressDialog progressDialog;


        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(context.getString(R.string.retrieving_app));
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    cancel(false);
                }
            });

            progressDialog.show();
        }

        @Override
        protected UUID doInBackground(Void... params)
        {
            try
            {
                PebbleDeveloperConnection developerConnection = new PebbleDeveloperConnection(context);
                developerConnection.connectBlocking();

                if (developerConnection.isOpen())
                {
                    UUID uuid = developerConnection.getCurrentRunningApp();
                    developerConnection.close();
                    return uuid;
                }

            } catch (URISyntaxException e)
            {
                e.printStackTrace();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final UUID uuid)
        {
            progressDialog.hide();

            if (uuid == null)
            {
                new AlertDialog.Builder(context).setMessage(R.string.error_developer_connection).setPositiveButton(android.R.string.ok, null).show();
                return;
            }

            appRetrievalCallback.onAppSelected(uuid);
        }
    }
}

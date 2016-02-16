package com.example.amaliacowan.pilldispenserdemo;
import android.util.Log;
import android.bluetooth.BluetoothClass.Service;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by RaiMunoz on 2/14/16.
 */
public class AlarmReceiverService extends Service {
    final String TAG = "AlarmReceiverService";
    @Override
    public void onStart(Intent intent, int startId) {
        Log.i(TAG, "Service onStart()");
        pollingTask.execute();
    }

    AsyncTask<Void, Void, Void> pollingTask = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... param) {
            // Do what you want in the background
        }

        @Override
        protected void onPostExecute(Void result) {
            stopSelf();
        }
    };
}

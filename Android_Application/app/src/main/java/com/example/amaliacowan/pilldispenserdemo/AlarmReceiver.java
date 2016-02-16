package com.example.amaliacowan.pilldispenserdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification;
import com.example.amaliacowan.pilldispenserdemo.R;
import android.app.Service;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.graphics.Color;

public class AlarmReceiver extends BroadcastReceiver {
    public static String EXTRA_DATE1 = "DATE";
    private final String TAG = "AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Time to Take your Medicine!", Toast.LENGTH_LONG).show();
        Log.d("ME", "Notification started");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher_web)
                        .setContentTitle("PillDispenser")
                        .setContentText("Time to take your Medicine!");

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());


        mBuilder.setVibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500});

        //LED
        mBuilder.setLights(Color.RED, 3000, 3000);

        Log.i(TAG, "AlarmReceiver broadcastintent received");
        Intent myIntent = new Intent( context, AlarmReceiverService.class);
        context.startService( myIntent );

    }

}
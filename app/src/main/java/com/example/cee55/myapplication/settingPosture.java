package com.example.cee55.myapplication;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class settingPosture extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_posture);
        findViewById(R.id.layout).setBackgroundResource(R.drawable.background);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ADB9CA")));
    }



    public void loading(View view) {
        Intent intent_01 = new Intent(getApplicationContext(), loading.class);
        startActivity(intent_01);
    }

    public void back(View view){
        PendingIntent mPendingIntent = PendingIntent.getActivity(
                settingPosture.this,
                0,
                new Intent(getApplicationContext(), settingPosture.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(settingPosture.this)
                        .setSmallIcon(R.drawable.bell)
                        .setContentTitle("Bad Posture")
                        .setContentText("Sit correclty")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(mPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());




       finish();
    }
}

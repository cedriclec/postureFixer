package com.example.cee55.posturefixer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cee55.posturefixer.tableDataBase.distanceTable;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    String status = "Stopping";
    private TimerTask mTask;
    private Timer mTimer;
    private double distanceOne;
    private double distanceTwo;
    private double inclination;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.layout).setBackgroundResource(R.drawable.background);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ADB9CA")));


        TextView text = findViewById(R.id.Status);
        if(null != getIntent().getStringExtra("status"))
            status = getIntent().getStringExtra("status");
        text.setText("Status: ".concat(status));
        text.setTextSize(25);
        text.setTextColor(Color.parseColor("#000000"));

        mTask = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Calendar oCalender = Calendar.getInstance();
                        int hour = oCalender.get(Calendar.HOUR_OF_DAY);
                        int minute = oCalender.get(Calendar.MINUTE);
                        if(null != getIntent().getStringExtra("finishHour") && hour == Integer.parseInt(getIntent().getStringExtra("finishHour")) && minute >= Integer.parseInt(getIntent().getStringExtra("finishMinute")) &&  getIntent().getStringExtra("status").equals("Running")) {
                            Toast.makeText(getApplicationContext(), "reach", Toast.LENGTH_SHORT).show();
                            getIntent().removeExtra("status");
                            getIntent().putExtra("status", "Stopping");
                            TextView text = findViewById(R.id.Status);
                            text.setText("Status: ".concat("Stopping"));
                            text.setTextSize(25);
                            text.setTextColor(Color.parseColor("#000000"));

                            /*
                            Draw result graph part
                             */
                            Intent intent_01 = new Intent(getApplicationContext(), statistics.class);
                            intent_01.putExtra("status", status);
                            intent_01.putExtra("distanceOne", getIntent().getStringExtra("distanceOne"));
                            intent_01.putExtra("distanceTwo", getIntent().getStringExtra("distanceTwo"));
                            startActivity(intent_01);

                        }
                        if(null != getIntent().getStringExtra("distanceOne") && null != getIntent().getStringExtra("startHour") && hour >= Integer.parseInt(getIntent().getStringExtra("startHour")) && minute >= Integer.parseInt(getIntent().getStringExtra("startMinute"))
                                && (null == getIntent().getStringExtra("finishHour") || (hour <= Integer.parseInt(getIntent().getStringExtra("finishHour")) && minute < Integer.parseInt(getIntent().getStringExtra("finishMinute"))))){
                            TextView text = findViewById(R.id.Status);
                            if(null == getIntent().getStringExtra("finishHour"))
                                Toast.makeText(getApplicationContext(),  getIntent().getStringExtra("status"), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getApplicationContext(), "start".concat(getIntent().getStringExtra("startHour").concat(getIntent().getStringExtra("startMinute"))), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(), getIntent().getStringExtra("finishHour").concat(getIntent().getStringExtra("finishMinute")), Toast.LENGTH_SHORT).show();

                            distanceOne = Double.parseDouble(getIntent().getStringExtra("distanceOne"));
                            distanceTwo = Double.parseDouble(getIntent().getStringExtra("distanceTwo"));
                            // TEST
                            Context context = getApplicationContext();
                            distanceTable res = cloudDatabase.getOneSpecificRow("20171214", context);
                            double top = res.getTop();
                            double bottom = res.getBottom();
                            top = 70;
                            bottom = 12;
                            inclination = (distanceOne + distanceTwo) / (top + bottom);
                             /* 0.84 <inclination < 0.96 => posture is good
                                1.5 < inclination < 1.8 or
                                0.55 < inclination < 0.7  => posture is bad
                              */
                             inclination = 1.7;
                            if ((1.5 < inclination && inclination < 1.8) || (0.55 < inclination && inclination < 0.75)) {
                                PendingIntent mPendingIntent = PendingIntent.getActivity(
                                        MainActivity.this,
                                        0,
                                        new Intent(getApplicationContext(), suggestPosture.class),
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                );

                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(MainActivity.this)
                                                .setSmallIcon(R.drawable.bell)
                                                .setContentTitle("Bad Posture")
                                                .setContentText("Sit correclty")
                                                .setDefaults(Notification.DEFAULT_ALL)
                                                .setAutoCancel(true)
                                                .setContentIntent(mPendingIntent);
                                NotificationManager mNotificationManager =
                                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                mNotificationManager.notify(0, mBuilder.build());

                            }
                        }
                    }
                });
            }
        };

            mTimer = new Timer();
        mTimer.schedule(mTask, 0, 1000*5);  //every 5 seconds, check the status of program.
    }

    /**
     * Called when the user taps the Send button
     */
    public void menusettingTime(View view) {
        if(null == getIntent().getStringExtra("distanceOne")){
            Toast.makeText(getApplicationContext(), "Please do 'Settinng Posture' step before running the detecting program" , Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent_01 = new Intent(getApplicationContext(), settingTime.class);
            intent_01.putExtra("status", status);
            intent_01.putExtra("distanceOne", getIntent().getStringExtra("distanceOne"));
            intent_01.putExtra("distanceTwo", getIntent().getStringExtra("distanceTwo"));
            startActivity(intent_01);
        }
    }

    public void settingPosture(View view){
        Intent intent_01 = new Intent(getApplicationContext(), settingPosture.class);
        startActivity(intent_01);
    }

    public void menuStatistics(View view){
       /* if(null == getIntent().getStringExtra("distanceOne")){
            Toast.makeText(getApplicationContext(), "Please do 'Settinng Posture' step before running the detecting program" , Toast.LENGTH_SHORT).show();
        }
        else {*/
            Intent intent_01 = new Intent(getApplicationContext(), statistics.class);
            intent_01.putExtra("distanceOne", getIntent().getStringExtra("distanceOne"));
            intent_01.putExtra("distanceTwo", getIntent().getStringExtra("distanceTwo"));
            intent_01.putExtra("status", status);
            startActivity(intent_01);
       // }
    }
}
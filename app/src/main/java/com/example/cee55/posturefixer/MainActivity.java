package com.example.cee55.posturefixer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    String status = "Stopping";
    private TimerTask mTask;
    private Timer mTimer;

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
                            getIntent().putExtra("status", "Stopping");
                            TextView text = findViewById(R.id.Status);
                            text.setText("Status: ".concat("Stopping"));
                            text.setTextSize(25);
                            text.setTextColor(Color.parseColor("#000000"));
                        }
                    }
                });
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 0, 1000*10);


    }

    /**
     * Called when the user taps the Send button
     */
    public void menusettingTime(View view) {
        Intent intent_01 = new Intent(getApplicationContext(), settingTime.class);
        intent_01.putExtra("status", status);
        startActivity(intent_01);
    }

    public void settingPosture(View view){
        Intent intent_01 = new Intent(getApplicationContext(), settingPosture.class);
        startActivity(intent_01);
    }

    public void menuStatistics(View view){
        Intent intent_01 = new Intent(getApplicationContext(), statistics.class);
        startActivity(intent_01);
    }
}

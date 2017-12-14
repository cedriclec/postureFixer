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

public class settingTime extends AppCompatActivity {
    String status;
    Intent intent_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_time);
        findViewById(R.id.layout).setBackgroundResource(R.drawable.background);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ADB9CA")));
        //intent_main = new Intent(getApplicationContext(), MainActivity.class);
        status = getIntent().getStringExtra("status");
        TextView text = findViewById(R.id.Status);
        text.setText("Status: ".concat(status));
        text.setTextSize(25);
        text.setTextColor(Color.parseColor("#000000"));
    }

    public void backClick(View view) {

        Intent intent_01 = new Intent(getApplicationContext(), MainActivity.class);
        intent_01.putExtra("status", status);
        if(null !=getIntent().getStringExtra("startHour") ) {
            intent_01.putExtra("startHour", getIntent().getStringExtra("startHour"));
            intent_01.putExtra("startMinute", getIntent().getStringExtra("startMinute"));
        }
        if(null !=getIntent().getStringExtra("finishHour")){
            Toast.makeText(getApplicationContext(), "finish", Toast.LENGTH_SHORT).show();
            intent_01.putExtra("finishHour", getIntent().getStringExtra("finishHour"));
            intent_01.putExtra("finishMinute", getIntent().getStringExtra("finishMinute"));
        }

        intent_01.putExtra("distanceOne", getIntent().getStringExtra("distanceOne"));
        intent_01.putExtra("distanceTwo", getIntent().getStringExtra("distanceTwo"));
        startActivity(intent_01);
    }

    public void specifyStart(View view) {
        if (getIntent().getStringExtra("status").equals("Stopping")) {
            Intent intent_01 = new Intent(getApplicationContext(), specificStart.class);
            intent_01.putExtra("status", status);
            intent_01.putExtra("distanceOne", getIntent().getStringExtra("distanceOne"));
            intent_01.putExtra("distanceTwo", getIntent().getStringExtra("distanceTwo"));
            startActivity(intent_01);
        } else {
            Toast toast_01 = Toast.makeText(this, "Thp program is already running", Toast.LENGTH_SHORT);
            toast_01.show();
        }
    }

    public void startProgramButton(View view){
        if (getIntent().getStringExtra("status").equals("Stopping")) {
            Calendar oCalender = Calendar.getInstance();
            int hour = oCalender.get(Calendar.HOUR_OF_DAY);
            int minute = oCalender.get(Calendar.MINUTE);

            status = "Running";
            getIntent().putExtra("status", "Running");
            TextView text = findViewById(R.id.Status);
            text.setText("Status: ".concat(status));
            text.setTextSize(25);
            text.setTextColor(Color.parseColor("#000000"));
            Toast.makeText(getApplicationContext(), "The program is running now!!!" , Toast.LENGTH_SHORT).show();
            getIntent().putExtra("startHour", String.valueOf(hour));
            getIntent().putExtra("startMinute", String.valueOf(minute));
        }
        else {
            Toast toast_01 = Toast.makeText(this, "Thp program is already running", Toast.LENGTH_SHORT);
            toast_01.show();
        }
    }

    public void stopProgramButton(View view){
        if (getIntent().getStringExtra("status").equals("Running")) {
            Calendar oCalender = Calendar.getInstance();
            int hour = oCalender.get(Calendar.HOUR_OF_DAY);
            int minute = oCalender.get(Calendar.MINUTE);

            //status = "Stopping";
            TextView text = findViewById(R.id.Status);
            //text.setText("Status: ".concat(status));
            text.setText("Status: Stopping");


            text.setTextSize(25);
            text.setTextColor(Color.parseColor("#000000"));
            Toast.makeText(getApplicationContext(), "The program is stopping now!!!" , Toast.LENGTH_SHORT).show();
            getIntent().putExtra("finishHour", String.valueOf(hour));
            getIntent().putExtra("finishMinute", String.valueOf(minute));
        }
        else {
            Toast toast_01 = Toast.makeText(this, "Thp program doesn't execute!!!", Toast.LENGTH_SHORT);
            toast_01.show();
        }
    }

}

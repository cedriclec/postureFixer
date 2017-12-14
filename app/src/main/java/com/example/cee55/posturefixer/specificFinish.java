package com.example.cee55.posturefixer;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

public class specificFinish extends AppCompatActivity {

    int hour;
    int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_finish);
        findViewById(R.id.layout).setBackgroundResource(R.drawable.background);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ADB9CA")));
    }

    public void backClick(View view) {
        finish();
    }

    public void endClick(View view) {
        final TimePicker time = (TimePicker)findViewById(R.id.time_picker);
        hour = time.getCurrentHour();
        minute = time.getCurrentMinute();
        if(hour > Integer.parseInt(getIntent().getStringExtra("startHour")) || (hour == Integer.parseInt(getIntent().getStringExtra("startHour")) && minute > Integer.parseInt(getIntent().getStringExtra("startMinute")))){
            Toast.makeText(getApplicationContext(), "Finish time is ".concat(String.valueOf(hour)).concat(":").concat(String.valueOf(minute)) , Toast.LENGTH_SHORT).show();
            String start_time = getIntent().getStringExtra("startHour").concat(":").concat(getIntent().getStringExtra("startMinute"));
            Intent intent_01 = new Intent(getApplicationContext(), showSettingTime.class);
            intent_01.putExtra("finishTime", String.valueOf(hour).concat(":").concat(String.valueOf(minute)));
            intent_01.putExtra("startTime", start_time);
            intent_01.putExtra("startHour", getIntent().getStringExtra("startHour"));
            intent_01.putExtra("startMinute", getIntent().getStringExtra("startMinute"));
            intent_01.putExtra("finishHour", String.valueOf(hour));
            intent_01.putExtra("finishMinute", String.valueOf(minute));
            intent_01.putExtra("distanceOne", getIntent().getStringExtra("distanceOne"));
            intent_01.putExtra("distanceTwo", getIntent().getStringExtra("distanceTwo"));
            startActivity(intent_01);
        }
        else
            Toast.makeText(getApplicationContext(), "Finish time is less than Start time", Toast.LENGTH_SHORT).show();
    }
}
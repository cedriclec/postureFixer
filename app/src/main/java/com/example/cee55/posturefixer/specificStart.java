package com.example.cee55.posturefixer;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import static android.app.ProgressDialog.show;

public class specificStart extends AppCompatActivity {
    int hour;
    int minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_start);
        findViewById(R.id.layout).setBackgroundResource(R.drawable.background);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ADB9CA")));
    }

    public void backClick(View view) {
        finish();
    }

    public void specifyFinish(View view) {
        final TimePicker time = (TimePicker)findViewById(R.id.time_picker);
        hour = time.getCurrentHour();
        minute = time.getCurrentMinute();
        Toast.makeText(getApplicationContext(), "Start time is ".concat(String.valueOf(hour)).concat(":").concat(String.valueOf(minute)) , Toast.LENGTH_SHORT).show();
        Intent intent_01 = new Intent(getApplicationContext(), specificFinish.class);
        intent_01.putExtra("startHour", String.valueOf(hour));
        intent_01.putExtra("startMinute", String.valueOf(minute));
        intent_01.putExtra("distanceOne", getIntent().getStringExtra("distanceOne"));
        intent_01.putExtra("distanceTwo", getIntent().getStringExtra("distanceTwo"));
        startActivity(intent_01);
    }
}
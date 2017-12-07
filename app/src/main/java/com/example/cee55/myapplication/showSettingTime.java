package com.example.cee55.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.graphics.Paint.FAKE_BOLD_TEXT_FLAG;

public class showSettingTime extends AppCompatActivity {
    String startPoint;
    String finishPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_setting_time);
        findViewById(R.id.layout).setBackgroundResource(R.drawable.background);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ADB9CA")));
        TextView text = findViewById(R.id.showTime);
        startPoint = getIntent().getStringExtra("startTime");
        finishPoint = getIntent().getStringExtra("finishTime");

        text.setText("Start time is ".concat(startPoint).concat(".\n \nFinish time is ").concat(finishPoint).concat("."));
        text.setTextSize(25);
        text.setTextColor(Color.parseColor("#000000"));
        text.setPaintFlags(FAKE_BOLD_TEXT_FLAG);
    }

    public void backClick(View view) {
        Intent intent_01 = new Intent(getApplicationContext(), MainActivity.class);
        intent_01.putExtra("status", "Running");
        intent_01.putExtra("finishHour", getIntent().getStringExtra("finishHour"));
        intent_01.putExtra("finishMinute", getIntent().getStringExtra("finishMinute"));
        startActivity(intent_01);
    }
}

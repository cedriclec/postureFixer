package com.example.cee55.posturefixer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static android.graphics.Paint.FAKE_BOLD_TEXT_FLAG;

public class loading extends AppCompatActivity{
    private Handler barHandler=new Handler();
    ProgressBar progressBar1;
    private TimerTask mTask;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        findViewById(R.id.returnhome).setVisibility(View.INVISIBLE);
        findViewById(R.id.layout).setBackgroundResource(R.drawable.background);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ADB9CA")));
        progressBar1 = (ProgressBar) findViewById(R.id.bar);

        new Thread(){
            public void run() {
                barHandler.post(new Runnable() {
                    public void run(){
                        progressBar1.incrementProgressBy(5);
                    }
                });
            }
        }.start();

        mTask = new TimerTask() {
            @Override
            public void run() {
                loading.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (progressBar1.getMax() == progressBar1.getProgress()){
                            progressBar1.setVisibility(View.INVISIBLE);
                            findViewById(R.id.returnhome).setVisibility(View.VISIBLE);
                            TextView text = findViewById(R.id.complete);
                            text.setText("Complete!!!");
                            text.setTextSize(25);
                            text.setTextColor(Color.parseColor("#000000"));
                            text.setPaintFlags(FAKE_BOLD_TEXT_FLAG);
                            mTimer.cancel();
                        }
                        else
                            progressBar1.incrementProgressBy(20);
                    }
                });
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTask, 0, 1000);
    }

    public void backClick(View view) {
        Intent intent_01 = new Intent(getApplicationContext(), MainActivity.class);
        intent_01.putExtra("distanceOne", getIntent().getStringExtra("distanceOne"));
        intent_01.putExtra("distanceTwo", getIntent().getStringExtra("distanceTwo"));
        startActivity(intent_01);
    }
}

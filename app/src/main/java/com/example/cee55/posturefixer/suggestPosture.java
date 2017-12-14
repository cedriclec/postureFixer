package com.example.cee55.posturefixer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class suggestPosture extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_posture);
        findViewById(R.id.layout).setBackgroundResource(R.drawable.background);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ADB9CA")));
    }

    public void homeClick(View view) {
        finish();
    }
}

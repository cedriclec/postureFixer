package com.example.cee55.posturefixer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class exercise extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        findViewById(R.id.layout).setBackgroundResource(R.drawable.background);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ADB9CA")));
        TextView text = findViewById(R.id.suggest);
       int result = 0;
        if(null != getIntent().getStringExtra("result")){
            result = getCharNumber(getIntent().getStringExtra("result"), '0');
            Toast.makeText(getApplicationContext(), Integer.toString(result), Toast.LENGTH_SHORT).show();
        }

        if(result > 0 && result < 3)
            text.setText("Choose one exercise among below, do that 5 times, 3set.");
        else if(3<= result && result < 5)
            text.setText("Choose one exercise among below, do that 7 times, 3set.");
        else if(result >= 5 && result < 7)
            text.setText("Choose one exercise among below, do that 9 times, 3set.");
        else if(result >= 7)
            text.setText("Choose one exercise among below, do that 12 times, 3set.");
        text.setTextSize(20);
        text.setTextColor(Color.parseColor("#000000"));
    }

    int getCharNumber(String str, char c)
    {
        int count = 0;
        for(int i=0;i<str.length();i++)
        {
            if(str.charAt(i) == c)
                count++;
        }
        return count;
    }

    public void back(View view){
        finish();
    }
    public void returnHome(View view){
        Intent intent_01 = new Intent(getApplicationContext(), MainActivity.class);
        intent_01.putExtra("distanceOne", getIntent().getStringExtra("distanceOne"));
        intent_01.putExtra("distanceTwo", getIntent().getStringExtra("distanceTwo"));
        startActivity(intent_01);
    }
}

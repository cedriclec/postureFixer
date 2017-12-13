package com.example.cee55.posturefixer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class settingRequest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_request);
        findViewById(R.id.layout).setBackgroundResource(R.drawable.background);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ADB9CA")));
    }

    public void loading(View view) {
        Intent intent_01 = new Intent(getApplicationContext(), loading.class);
        EditText editTextOne = (EditText)findViewById(R.id.disOne);
        EditText editTextTwo = (EditText)findViewById(R.id.disTwo);
        if(editTextOne.getText().toString().length() != 0 && editTextTwo.getText().toString().length() != 0) {
            intent_01.putExtra("distanceOne", editTextOne.getText().toString());
            intent_01.putExtra("distanceTwo", editTextTwo.getText().toString());
            startActivity(intent_01);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please write distance", Toast.LENGTH_LONG).show();
        }
    }

    public void back(View view){
        finish();
    }

}

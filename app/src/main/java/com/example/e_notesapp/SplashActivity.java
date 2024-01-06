package com.example.e_notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //start main screen after 2secs
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //start main screen
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();//finish this action

            }
        },2000);//2sec
    }
}
package com.example.doma.travel_diary;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class startView extends AppCompatActivity {
    int SPLASH_TIME=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_view);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                overridePendingTransition(0,android.R.anim.fade_in);
                startActivity(new Intent(startView.this,Log.class));
                finish();
            }
        },SPLASH_TIME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        findViewById(R.id.imageView_splash).setBackground(null);
        System.gc();
    }
}
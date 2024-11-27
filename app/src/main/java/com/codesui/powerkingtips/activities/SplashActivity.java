package com.codesui.powerkingtips.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.codesui.powerkingtips.R;
import com.google.android.gms.ads.MobileAds;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MobileAds.initialize(this);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, PromptActivity.class));
            finish();
        }, 1000);

    }

}
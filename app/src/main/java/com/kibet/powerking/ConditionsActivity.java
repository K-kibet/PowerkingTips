package com.kibet.powerking;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.kibet.powerking.ads.BannerManager;

public class ConditionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditions);
        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(view -> finish());

        FrameLayout adViewContainer = findViewById(R.id.adViewContainer);
        BannerManager bannerManager = new BannerManager(this, ConditionsActivity.this, adViewContainer);
        bannerManager.loadBanner();
    }
}
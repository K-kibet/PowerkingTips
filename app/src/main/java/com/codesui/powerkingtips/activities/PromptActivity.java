package com.codesui.powerkingtips.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.codesui.powerkingtips.MainActivity;
import com.codesui.powerkingtips.R;
import com.codesui.powerkingtips.ads.BannerManager;
import com.codesui.powerkingtips.ads.InterstitialManager;
import com.google.android.material.button.MaterialButton;

public class PromptActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        MaterialButton btnContinue = findViewById(R.id.btnContinue);
        InterstitialManager interstitialManager = new InterstitialManager();
        interstitialManager.loadInterstitial(PromptActivity.this);

        btnContinue.setOnClickListener(view -> {
            Intent intent = new Intent(PromptActivity.this, MainActivity.class);
            startActivity(intent);
            interstitialManager.showInterstitial(PromptActivity.this);
        });

        FrameLayout adViewContainer = findViewById(R.id.adViewContainer);
        BannerManager bannerManager = new BannerManager(this, PromptActivity.this, adViewContainer);
        bannerManager.loadBanner();
    }

}
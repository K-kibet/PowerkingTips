package com.kibet.powerking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.kibet.powerking.MainActivity;
import com.kibet.powerking.R;
import com.kibet.powerking.ads.BannerManager;

public class PromptActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        MaterialButton btnContinue = findViewById(R.id.btnContinue);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        MaterialButton btnRegister = findViewById(R.id.btnRegister);

        btnContinue.setOnClickListener(view -> {
            Intent intent = new Intent(PromptActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(PromptActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(PromptActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        FrameLayout adViewContainer = findViewById(R.id.adViewContainer);
        BannerManager bannerManager = new BannerManager(this, PromptActivity.this, adViewContainer);
        bannerManager.loadBanner();
    }

}
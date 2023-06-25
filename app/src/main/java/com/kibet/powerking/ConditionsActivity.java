package com.kibet.powerking;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class ConditionsActivity extends AppCompatActivity {
    private FrameLayout adViewContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditions);
        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(view -> finish());

        adViewContainer = findViewById(R.id.adViewContainer);
        adViewContainer.post(this::LoadBanner);
    }
    private void LoadBanner() {
        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.Banner_Ad_Unit));
        adViewContainer.removeAllViews();
        adViewContainer.addView(adView);
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}
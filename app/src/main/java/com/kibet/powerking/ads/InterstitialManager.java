package com.kibet.powerking.ads;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.kibet.powerking.R;

public class InterstitialManager {
    private InterstitialAd mInterstitialAd;
    Activity activity;
    Context context;

    public InterstitialManager(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    AdRequest adRequest = new AdRequest.Builder().build();


    public void loadAd() {
        InterstitialAd.load(context, context.getString(R.string.Interstitial_Ad_Unit), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        mInterstitialAd = null;
                    }
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        mInterstitialAd = interstitialAd;
                    }
                });
        if(this.mInterstitialAd != null) {
            this.mInterstitialAd.show((Activity) context);
        }
    }
}

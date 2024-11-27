package com.codesui.powerkingtips.ads;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codesui.powerkingtips.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

public class RewardedInterstitialManager implements OnUserEarnedRewardListener {
    Activity activity;
    Context context;
    private RewardedInterstitialAd rewardedInterstitialAd;

    public RewardedInterstitialManager(Activity activity, Context context) {

        this.activity = activity;
        this.context = context;
    }

    public void loadAd() {
        // Use the test ad unit ID to load an ad.
        RewardedInterstitialAd.load(activity, activity.getString(R.string.Rewarded_Interstitial_Ad_Unit),
                new AdRequest.Builder().build(), new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        rewardedInterstitialAd = ad;
                        rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                                //Log.d(TAG, "Ad was clicked.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                rewardedInterstitialAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                //Log.e(TAG, "Ad failed to show fullscreen content.");
                                rewardedInterstitialAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                //Log.d(TAG, "Ad recorded an impression.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        //Log.d(TAG, loadAdError.toString());
                        rewardedInterstitialAd = null;
                    }
                });
    }

    public void showAdNow() {
        if (rewardedInterstitialAd != null) {
            rewardedInterstitialAd.show(activity, this::onUserEarnedReward);
        } else {
            Toast.makeText(activity, "Ad Not Loaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        int adsWatched = sharedPreferences.getInt("ads", 0);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        if (adsWatched >= 3) {
            return;
        }
        adsWatched += 1;
        myEdit.putInt("ads", adsWatched);
        myEdit.apply();
    }
}

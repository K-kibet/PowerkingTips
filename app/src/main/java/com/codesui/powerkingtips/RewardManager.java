package com.codesui.powerkingtips;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.codesui.powerkingtips.ads.RewardedInterstitialManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class RewardManager extends Activity {
    Context context;
    Activity activity;
    int adsWatched;

    public RewardManager(Context context, Activity activity, int adsWatched) {
        this.context = context;
        this.activity = activity;
        this.adsWatched = adsWatched;
    }

    public void openDialog(RewardedInterstitialManager rewardedInterstitialManager) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet, activity.findViewById(R.id.bottomSheet));
        bottomSheetDialog.setContentView(bottomSheetView);

        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        TextView textAdsWatched = bottomSheetDialog.findViewById(R.id.adsWatched);
        textAdsWatched.setText(String.valueOf(adsWatched));
        bottomSheetDialog.show();

        bottomSheetView.findViewById(R.id.btnCheckout).setOnClickListener(v -> {
            rewardedInterstitialManager.showAdNow();
            sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences1, key) -> {
                adsWatched = sharedPreferences.getInt("ads", 0);
                textAdsWatched.setText(String.valueOf(adsWatched));
                if (adsWatched < 3) {
                    rewardedInterstitialManager.loadAd();
                }
            });
        });

    }
}

package com.codesui.powerkingtips.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codesui.powerkingtips.R;
import com.codesui.powerkingtips.RateManager;
import com.codesui.powerkingtips.RewardManager;
import com.codesui.powerkingtips.ShareManager;
import com.codesui.powerkingtips.ads.BannerManager;
import com.codesui.powerkingtips.ads.RewardedInterstitialManager;
import com.google.android.material.button.MaterialButton;

public class SettingsFragment extends Fragment {
    SharedPreferences sharedPreferences;
    int adsWatched;
    RewardManager rewardManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton btnFreeVip = view.findViewById(R.id.btnFreeVip);
        MaterialButton shareBtn = view.findViewById(R.id.btnShare);
        MaterialButton rateBtn = view.findViewById(R.id.btnRate);

        sharedPreferences = requireContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        adsWatched = sharedPreferences.getInt("ads", 0);
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        rewardManager = new RewardManager(requireContext(), requireActivity(), adsWatched);
        RewardedInterstitialManager rewardedInterstitialManager = new RewardedInterstitialManager(requireActivity(), requireContext());
        rewardedInterstitialManager.loadAd();


        btnFreeVip.setOnClickListener(v -> {
            if (adsWatched >= 3) {
                Toast.makeText(requireContext(), "You are all set", Toast.LENGTH_SHORT).show();
            } else {
                rewardManager.openDialog(rewardedInterstitialManager);
            }
        });

        shareBtn.setOnClickListener(v -> {
            ShareManager shareManager = new ShareManager(requireContext());
            shareManager.shareApp();
        });
        rateBtn.setOnClickListener(v -> {
            RateManager rateManager = new RateManager(requireContext());
            rateManager.rate();
        });

        FrameLayout adViewContainer = view.findViewById(R.id.adViewContainer);
        BannerManager bannerManager = new BannerManager(requireContext(), requireActivity(), adViewContainer);
        bannerManager.loadBanner();
    }

    SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("ads")) {
                adsWatched = sharedPreferences.getInt(key, 0);
            }
        }
    };
}
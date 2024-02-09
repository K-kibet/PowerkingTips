package com.kibet.powerking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kibet.powerking.PaymentManager;
import com.kibet.powerking.R;
import com.kibet.powerking.ShareManager;
import com.kibet.powerking.activities.EditProfileActivity;
import com.kibet.powerking.activities.RegisterActivity;
import com.kibet.powerking.ads.BannerManager;

public class SettingsFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton editBtn = view.findViewById(R.id.btnEdit);
        MaterialButton removeAdsBtn = view.findViewById(R.id.btnRemoveAds);
        MaterialButton shareBtn = view.findViewById(R.id.btnShare);
        MaterialButton rateBtn = view.findViewById(R.id.btnRate);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        editBtn.setOnClickListener(v -> editProfile());
        removeAdsBtn.setOnClickListener(v -> {
            if(currentUser != null) {
                PaymentManager paymentManager = new PaymentManager();
                paymentManager.openDialog(requireContext(), requireActivity());
            } else {
                Intent intent = new Intent(getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        shareBtn.setOnClickListener(v -> {
            ShareManager shareManager = new ShareManager(requireContext());
            shareManager.shareApp();
        });
        rateBtn.setOnClickListener(v -> {
        });

        FrameLayout adViewContainer = view.findViewById(R.id.adViewContainer);
        BannerManager bannerManager = new BannerManager(requireContext(), requireActivity(), adViewContainer);
        bannerManager.loadBanner();
    }

    private void editProfile () {
        Intent intent;
        if(currentUser != null) {
            intent = new Intent(getContext(), EditProfileActivity.class);
        } else {
            intent = new Intent(getContext(), RegisterActivity.class);
        }
        startActivity(intent);
    }

}
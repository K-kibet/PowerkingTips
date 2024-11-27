package com.codesui.powerkingtips.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codesui.powerkingtips.R;
import com.codesui.powerkingtips.RateManager;
import com.codesui.powerkingtips.ShareManager;
import com.codesui.powerkingtips.UrlManager;
import com.codesui.powerkingtips.ads.BannerManager;
import com.google.android.material.button.MaterialButton;

public class AboutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton twitter = view.findViewById(R.id.btnTwitter);
        ImageButton telegram = view.findViewById(R.id.btnTelegram);
        ImageButton whatsapp = view.findViewById(R.id.btnWhatsapp);

        MaterialButton moreBtn = view.findViewById(R.id.btnMore);
        MaterialButton shareBtn = view.findViewById(R.id.btnShare);
        MaterialButton rateBtn = view.findViewById(R.id.btnRate);
        MaterialButton termsBtn = view.findViewById(R.id.btnTerms);

        UrlManager urlManager = new UrlManager(requireContext());


        twitter.setOnClickListener(v -> urlManager.openTwitter());
        telegram.setOnClickListener(v -> urlManager.openTelegram());
        whatsapp.setOnClickListener(v -> urlManager.openWhatsapp());
        moreBtn.setOnClickListener(v -> urlManager.moreApps());
        shareBtn.setOnClickListener(v -> {
            ShareManager shareManager = new ShareManager(requireContext());
            shareManager.shareApp();
        });
        rateBtn.setOnClickListener(v -> {
            RateManager rateManager = new RateManager(requireContext());
            rateManager.rate();
        });
        termsBtn.setOnClickListener(v -> urlManager.openTerms());

        FrameLayout adViewContainer = view.findViewById(R.id.adViewContainer);
        BannerManager bannerManager = new BannerManager(requireContext(), requireActivity(), adViewContainer);
        bannerManager.loadBanner();

    }

}
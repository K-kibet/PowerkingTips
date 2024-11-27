package com.codesui.powerkingtips.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codesui.powerkingtips.R;
import com.codesui.powerkingtips.RewardManager;
import com.codesui.powerkingtips.TipsAdapter;
import com.codesui.powerkingtips.ads.BannerManager;
import com.codesui.powerkingtips.ads.RewardedInterstitialManager;
import com.codesui.powerkingtips.data.Tip;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PremiumFragment extends Fragment {
    FirebaseFirestore db;
    SharedPreferences sharedPreferences;
    int adsWatched;
    TextView textAdsWatched;
    RewardManager rewardManager;
    private RecyclerView recyclerView;
    private LinearLayout noUserLayout;
    private TipsAdapter tipsAdapter;
    private List<Tip> tipsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_premium, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        noUserLayout = view.findViewById(R.id.noUserLayout);

        MaterialButton upgradeBtn = view.findViewById(R.id.btnUpgrade);
        textAdsWatched = view.findViewById(R.id.adsWatched);
        sharedPreferences = requireContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        adsWatched = sharedPreferences.getInt("ads", 0);
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        rewardManager = new RewardManager(requireContext(), requireActivity(), adsWatched);

        db = FirebaseFirestore.getInstance();
        tipsList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        tipsAdapter = new TipsAdapter(requireContext(), requireActivity(), tipsList);
        RewardedInterstitialManager rewardedInterstitialManager = new RewardedInterstitialManager(requireActivity(), requireContext());
        rewardedInterstitialManager.loadAd();

        updateUI();

        upgradeBtn.setOnClickListener(v -> {
            if (adsWatched >= 3) {
                Toast.makeText(requireContext(), "You are all set", Toast.LENGTH_SHORT).show();
            } else {
                rewardManager.openDialog(rewardedInterstitialManager);
            }
        });

        FrameLayout adViewContainer = view.findViewById(R.id.adViewContainer);
        BannerManager bannerManager = new BannerManager(requireContext(), requireActivity(), adViewContainer);
        bannerManager.loadBanner();
    }

    private void updateUI() {
        new Handler().postDelayed(() -> {
            if (adsWatched >= 3) {
                noUserLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                readFirebase();
            } else {
                noUserLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                textAdsWatched.setText(String.valueOf(adsWatched));
            }
        }, 1000);
    }

    private void readFirebase() {
        db.collection("tips").whereEqualTo("premium", true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Tip tip = new Tip(document.getString("home"),
                                    document.getString("away"),
                                    document.getString("pick"),
                                    document.getString("odd"),
                                    document.getString("status"),
                                    document.getString("won"),
                                    document.getId());
                            tipsList.add(tip);
                        }
                        recyclerView.setAdapter(tipsAdapter);
                    }
                });
    }

    SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("ads")) {
                adsWatched = sharedPreferences.getInt(key, 0);
                updateUI();
            }
        }
    };
}
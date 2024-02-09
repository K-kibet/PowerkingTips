package com.kibet.powerking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kibet.powerking.PaymentManager;
import com.kibet.powerking.R;
import com.kibet.powerking.TipsAdapter;
import com.kibet.powerking.activities.RegisterActivity;
import com.kibet.powerking.ads.BannerManager;
import com.kibet.powerking.data.Tip;

import java.util.ArrayList;
import java.util.List;

public class PremiumFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayout noUserLayout;
    private TipsAdapter tipsAdapter;
    private List<Tip> tipsList;
    FirebaseFirestore db;
    //SharedPreferences prefs;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_premium, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        //noUserLayout = view.findViewById(R.id.noUserLayout);

        MaterialButton upgradeBtn = view.findViewById(R.id.btnUpgrade);
        //prefs = requireContext().getSharedPreferences("Account", MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();
        tipsList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        tipsAdapter = new TipsAdapter(getContext(), tipsList);

        /*new Handler().postDelayed(() -> {
            if (prefs.getBoolean("premium", false)) {
                noUserLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                readFirebase();
            } else {
                noUserLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }, 1000);
         */
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        upgradeBtn.setOnClickListener(v ->{
            if(currentUser != null) {
                PaymentManager paymentManager = new PaymentManager();
                paymentManager.openDialog(requireContext(), requireActivity());
            } else {
                Intent intent = new Intent(requireContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        readFirebase();
        FrameLayout adViewContainer = view.findViewById(R.id.adViewContainer);
        BannerManager bannerManager = new BannerManager(requireContext(), requireActivity(), adViewContainer);
        bannerManager.loadBanner();
    }
    private void readFirebase () {
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
}
package com.kibet.powerking;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kibet.powerking.ads.BannerManager;

import java.util.ArrayList;
import java.util.List;

public class FreeFragment extends Fragment {
    private RecyclerView recyclerView;
    private TipsAdapter tipsAdapter;
    private List<Tip> tipsList;
    FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_free, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        tipsList = new ArrayList<>();
        tipsAdapter = new TipsAdapter(getContext(), tipsList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();


        new Handler().postDelayed(this::readFirebase, 1000);
        FrameLayout adViewContainer = view.findViewById(R.id.adViewContainer);
        BannerManager bannerManager = new BannerManager(requireContext(), requireActivity(), adViewContainer);
        bannerManager.loadBanner();
    }
    private void readFirebase () {
        db.collection("tips").whereEqualTo("premium", false)
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
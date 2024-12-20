package com.codesui.powerkingtips.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codesui.powerkingtips.R;
import com.codesui.powerkingtips.TipsAdapter;
import com.codesui.powerkingtips.ads.BannerManager;
import com.codesui.powerkingtips.data.Tip;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FreeFragment extends Fragment {
    FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private TipsAdapter tipsAdapter;
    private List<Tip> tipsList;

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
        tipsAdapter = new TipsAdapter(getContext(), requireActivity(), tipsList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();


        readFirebase();
        FrameLayout adViewContainer = view.findViewById(R.id.adViewContainer);
        BannerManager bannerManager = new BannerManager(requireContext(), requireActivity(), adViewContainer);
        bannerManager.loadBanner();
    }

    private void readFirebase() {
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
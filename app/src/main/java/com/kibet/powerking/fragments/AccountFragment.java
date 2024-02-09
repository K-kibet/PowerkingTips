package com.kibet.powerking.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kibet.powerking.PaymentManager;
import com.kibet.powerking.R;
import com.kibet.powerking.activities.EditProfileActivity;
import com.kibet.powerking.activities.LoginActivity;
import com.kibet.powerking.activities.RegisterActivity;
import com.kibet.powerking.ads.BannerManager;

public class AccountFragment extends Fragment {
    //SharedPreferences prefs;
    TextView textPlan, textUsername, textEmail;
    LinearLayout userLayout, noUserLayout;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userLayout = view.findViewById(R.id.userLayout);
        noUserLayout = view.findViewById(R.id.noUserLayout);

        textPlan = view.findViewById(R.id.textPlan);
        textUsername = view.findViewById(R.id.textUsername);
        textEmail = view.findViewById(R.id.textEmail);

        MaterialButton btnUpgrade = view.findViewById(R.id.btnUpgrade);
        MaterialButton btnSettings = view.findViewById(R.id.btnSettings);
        MaterialButton btnLogout = view.findViewById(R.id.btnLogout);
        MaterialButton btnRegister = view.findViewById(R.id.btnRegister);

        //prefs = requireContext().getSharedPreferences("Account", MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //setValues();

        btnUpgrade.setOnClickListener(v -> {
            PaymentManager paymentManager = new PaymentManager();
            paymentManager.openDialog(requireContext(), requireActivity());
        });
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            mAuth.addAuthStateListener(firebaseAuth -> setValues());
        });
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            //if(prefs.getBoolean("premium", false)) {
            //    openSettings();
            //} else {
             //   startActivity(intent);
            //}
            startActivity(intent);
        });
        btnRegister.setOnClickListener(v -> openRegister());

        FrameLayout adViewContainer = view.findViewById(R.id.adViewContainer);
        BannerManager bannerManager = new BannerManager(requireContext(), requireActivity(), adViewContainer);
        bannerManager.loadBanner();
    }

    private void setValues () {
        if(currentUser != null) {
            noUserLayout.setVisibility(View.GONE);
            userLayout.setVisibility(View.VISIBLE);
            textUsername.setText(/*prefs.getString("username", null*/ "John Doe");
            textEmail.setText(currentUser.getEmail());
        } else {
            noUserLayout.setVisibility(View.VISIBLE);
            userLayout.setVisibility(View.GONE);
        }
    }

    private void openSettings () {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void openRegister () {
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivity(intent);
    }
}
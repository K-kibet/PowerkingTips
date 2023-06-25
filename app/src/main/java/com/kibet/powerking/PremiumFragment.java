package com.kibet.powerking;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PremiumFragment extends Fragment {
    private FrameLayout adViewContainer;
    PayPalConfiguration config;
    private RecyclerView recyclerView;
    private LinearLayout noUserLayout;
    private TipsAdapter tipsAdapter;
    private List<Tip> tipsList;
    FirebaseFirestore db;
    SharedPreferences prefs;
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
        noUserLayout = view.findViewById(R.id.noUserLayout);

        MaterialButton upgradeBtn = view.findViewById(R.id.btnUpgrade);
        prefs = requireContext().getSharedPreferences("Powerking", MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();
        config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(getString(R.string.paypal_client_key));
        tipsList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        tipsAdapter = new TipsAdapter(getContext(), tipsList);

        new Handler().postDelayed(() -> {
            if (prefs.getBoolean("premium", false)) {
                noUserLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                readFirebase();
            } else {
                noUserLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }, 1000);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        upgradeBtn.setOnClickListener(v ->{
            if(currentUser != null) {
                openDialog();
            } else {
                Intent intent = new Intent(requireContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        adViewContainer = view.findViewById(R.id.adViewContainer);
        adViewContainer.post(this::LoadBanner);
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

    public void openDialog () {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet, getView().findViewById(R.id.bottomSheet));
        bottomSheetView.findViewById(R.id.btnCheckout).setOnClickListener(v -> getPayment());
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void getPayment() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(getString(R.string.amount)), "USD", "Payment Fees", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getContext(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, Integer.parseInt(getString(R.string.PAYPAL_REQUEST_CODE)));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Integer.parseInt(getString(R.string.PAYPAL_REQUEST_CODE))) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        JSONObject payObj = new JSONObject(paymentDetails);
                        String payID = payObj.getJSONObject("response").getString("id");
                        String state = payObj.getJSONObject("response").getString("state");
                        //paymentTV.setText("Payment " + state + "\n with payment id is " + payID);
                    } catch (JSONException e) {
                        Log.e("Error", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void LoadBanner() {
        AdView adView = new AdView(getContext());
        adView.setAdUnitId(getString(R.string.Banner_Ad_Unit));
        adViewContainer.removeAllViews();
        adViewContainer.addView(adView);
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getContext(), adWidth);
    }
}
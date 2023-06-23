package com.kibet.powerking;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class AccountFragment extends Fragment {
    PayPalConfiguration config;
    SharedPreferences prefs;
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

        config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(getString(R.string.paypal_client_key));
        prefs = requireContext().getSharedPreferences("Powerking", MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        setValues();

        btnUpgrade.setOnClickListener(v -> openDialog());
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            mAuth.addAuthStateListener(firebaseAuth -> setValues());
        });
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            if(prefs.getBoolean("premium", false)) {
                openSettings();
            } else {
                startActivity(intent);
            }

        });
        btnRegister.setOnClickListener(v -> openRegister());
    }

    private void setValues () {
        if(currentUser != null) {
            noUserLayout.setVisibility(View.GONE);
            userLayout.setVisibility(View.VISIBLE);
        } else {
            noUserLayout.setVisibility(View.VISIBLE);
            userLayout.setVisibility(View.GONE);
            textUsername.setText(prefs.getString("username", null));
            textEmail.setText(currentUser.getEmail());
        }
    }

    private void openDialog () {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet, getView().findViewById(R.id.bottomSheet));
        bottomSheetView.findViewById(R.id.btnCheckout).setOnClickListener(v -> getPayment());
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void openSettings () {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void openRegister () {
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivity(intent);
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

}
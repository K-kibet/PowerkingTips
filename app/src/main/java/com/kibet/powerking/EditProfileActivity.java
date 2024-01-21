package com.kibet.powerking;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kibet.powerking.ads.BannerManager;

import java.util.HashMap;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    SharedPreferences prefs;
    TextView textUsername, textEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        textUsername = findViewById(R.id.textUsername);
        textEmail = findViewById(R.id.textEmail);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        prefs = getSharedPreferences("Powerking", MODE_PRIVATE);
        setValues();

        MaterialButton btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(v -> updateProfile());

        FrameLayout adViewContainer = findViewById(R.id.adViewContainer);
        BannerManager bannerManager = new BannerManager(this, EditProfileActivity.this, adViewContainer);
        bannerManager.loadBanner();
    }

    private void setValues () {
        if(currentUser != null) {
            textUsername.setText(prefs.getString("username", null));
            textEmail.setText(currentUser.getEmail());
        }
    }
    private void updateProfile () {
        if (!validateUsername() | !validateEmail()) {
            return;
        }

        HashMap<String, Object> user = new HashMap<>();

        if(Objects.equals(currentUser.getDisplayName(), textUsername.getText().toString())) {
            user.put("name", textUsername.getText().toString());
        }

        if(Objects.equals(currentUser.getDisplayName(), textEmail.getText().toString())) {
            user.put("email", textEmail.getText().toString());
        }
    }

    private boolean checkWhiteSpace (String line) {
        for(int i = 0; i < line.length(); i++){
            if(line.charAt(i) == ' '){
                return true;
            }
        }
        return false;
    }

    private boolean validateUsername() {
        String val = textUsername.getText().toString().trim();
        if (val.isEmpty()) {
            textUsername.setError("Field can not be empty");
            return false;
        } else if (val.length() > 20) {
            textUsername.setError("Username is too large!");
            return false;
        } else if (checkWhiteSpace(val)) {
            textUsername.setError("No White spaces are allowed!");
            return false;
        }else {
            textUsername.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = textEmail.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";
        if (val.isEmpty()) {
            textEmail.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            textEmail.setError("Invalid Email!");
            return false;
        } else {
            textEmail.setError(null);
            return true;
        }
    }
}
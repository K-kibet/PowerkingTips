package com.kibet.powerking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kibet.powerking.R;

public class RegisterActivity extends AppCompatActivity {
    EditText username, email, password;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.textUsername);
        email = findViewById(R.id.textEmail);
        password = findViewById(R.id.textPassword);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        MaterialButton btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> handleRegister());
    }
    /*private void saveUser (String username, String email) {

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("premium", false);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    documentReference.getId();
                    startActivity(intent);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "User not saved", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                });
    }*/
    private void registerNewUser() {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //SharedPreferences.Editor editor = getSharedPreferences("Powerking", MODE_PRIVATE).edit();
                        //editor.putBoolean("premium", false);
                        //editor.putString("username", username.getText().toString());
                        //editor.apply();
                        Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                        //progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Registration failed!! Check your internet connectivity and try again", Toast.LENGTH_LONG).show();
                        //progressBar.setVisibility(View.GONE);
                    }
                });
    }
    private void handleRegister () {
        if (!validateUsername() | !validateEmail() | !validatePassword()) {
            return;
        }
        registerNewUser();
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
        String val = username.getText().toString().trim();
        if (val.isEmpty()) {
            username.setError("Field can not be empty");
            return false;
        } else if (val.length() > 20) {
            username.setError("Username is too large!");
            return false;
        } else if (checkWhiteSpace(val)) {
            username.setError("No White spaces are allowed!");
            return false;
        }else {
            username.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = email.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";
        if (val.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid Email!");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getText().toString().trim();
        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        } else if(val.length() < 6){
            password.setError("Password should contain 6 characters!");
            return false;
        }else {
            password.setError(null);
            return true;
        }
    }

}
package com.kibet.powerking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

public class TipActivity extends AppCompatActivity {
    FirebaseFirestore db;
    String id;

    TextView textDate, textTime, textStatus, btnStatus, textHome, textAway, textOdd, textPrediction;
    ImageView isWonImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);


        textDate = findViewById(R.id.textDate);
        textTime = findViewById(R.id.textTime);
        textStatus = findViewById(R.id.textStatus);
        btnStatus = findViewById(R.id.btnStatus);
        textHome = findViewById(R.id.textHome);
        textAway = findViewById(R.id.textAway);
        textOdd = findViewById(R.id.textOdd);
        textPrediction = findViewById(R.id.textPrediction);
        isWonImage = findViewById(R.id.isWonImage);
        Intent intent = getIntent();
        id = intent.getStringExtra("tip_id");
        db = FirebaseFirestore.getInstance();
        readFirebase();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void readFirebase () {
        db.collection("tips").document(id).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                //QueryDocumentSnapshot document =  task.getResult();

                textDate.setText(task.getResult().getString("date"));
                textTime.setText(task.getResult().getString("time"));
                textStatus.setText(task.getResult().getString("status"));
                if(Objects.equals(task.getResult().getString("status"), "live")) {
                    btnStatus.setVisibility(View.VISIBLE);
                } else {
                    btnStatus.setVisibility(View.GONE);
                }
                textHome.setText(task.getResult().getString("home"));
                textAway.setText(task.getResult().getString("away"));
                textOdd.setText(task.getResult().getString("odd"));
                textPrediction.setText(task.getResult().getString("pick"));

                if(Objects.equals(task.getResult().getString("won"), "pending")) {
                    isWonImage.setImageDrawable(getDrawable(R.drawable.baseline_unpublished_24));
                } else if(Objects.equals(task.getResult().getString("won"), "won")) {
                    isWonImage.setImageDrawable(getDrawable(R.drawable.baseline_verified_24));
                } else isWonImage.setImageDrawable(getDrawable(R.drawable.baseline_error_24));

            } else {
                Toast.makeText(TipActivity.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.example.blurdatingapplication.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.example.blurdatingapplication.MainActivity;
import com.example.blurdatingapplication.R;
import com.example.blurdatingapplication.data.Interest;
import com.example.blurdatingapplication.data.PhysicalFeatures;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetUpInterest extends AppCompatActivity {
    Button btnNext;

    Spinner spinnerSport, spinnerMusic, spinnerGaming, spinnerFood,
            spinnerTraveling, spinnerActivity, spinnerReading, spinnerOther;
    String sport, music, gaming, food, traveling, activity, reading;

    FirebaseAuth auth;
    FirebaseUser user;

    Interest interest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_interest);

        btnNext = findViewById(R.id.btn_next);
        spinnerSport = findViewById(R.id.spinner_sport);
        spinnerMusic = findViewById(R.id.spinner_music);
        spinnerGaming = findViewById(R.id.spinner_gaming);
        spinnerFood = findViewById(R.id.spinner_food);
        spinnerTraveling = findViewById(R.id.spinner_traveling);
        spinnerActivity = findViewById(R.id.spinner_actyvity);
        spinnerReading = findViewById(R.id.spinner_reading);
        //spinnerFaceType = findViewById(R.id.spinner_other);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sport = spinnerSport.getSelectedItem().toString();
                music = spinnerMusic.getSelectedItem().toString();
                gaming = spinnerGaming.getSelectedItem().toString();
                food = spinnerFood.getSelectedItem().toString();
                traveling = spinnerTraveling.getSelectedItem().toString();
                activity = spinnerActivity.getSelectedItem().toString();
                reading = spinnerReading.getSelectedItem().toString();

                setToDataBase();
            }
        });
    }

    void setToDataBase() {
        if (interest != null) {
            interest.setSport(sport);
            interest.setMusic(music);
            interest.setGaming(gaming);
            interest.setFood(food);
            interest.setTraveling(traveling);
            interest.setActivity(activity);
            interest.setReading(reading);
        }

        else{
            interest = new Interest(sport,music,gaming,food, traveling, activity, reading);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = user.getUid();

        db.collection("interest")
                .document(userId)
                .set(interest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), SetUpProfile.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
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
import com.example.blurdatingapplication.data.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetUpProfile extends AppCompatActivity {

    Button btnNext;

    Spinner spinnerJob, spinnerBloodType, spinnerChild, spinnerDrinking,
            spinnerSmoking, spinnerWorkOut, spinnerDayOff;
    String job, bloodtype, child, drinking, smoking, workout, dayoff;

    FirebaseAuth auth;
    FirebaseUser user;

    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile);

        // Initializing UI elements
        btnNext = findViewById(R.id.btn_next);
        spinnerJob = findViewById(R.id.spinner_job);
        spinnerBloodType = findViewById(R.id.spinner_bloodtype);
        spinnerChild = findViewById(R.id.spinner_child);
        spinnerDrinking = findViewById(R.id.spinner_drinking);
        spinnerSmoking = findViewById(R.id.spinner_smoking);
        spinnerWorkOut = findViewById(R.id.spinner_workout);
        spinnerDayOff = findViewById(R.id.spinner_dayoff);

        // Initializing Firebase authentication
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Setting up click listener for the "Next" button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieving selected values from spinners
                job = spinnerJob.getSelectedItem().toString();
                bloodtype = spinnerBloodType.getSelectedItem().toString();
                child = spinnerChild.getSelectedItem().toString();
                drinking = spinnerDrinking.getSelectedItem().toString();
                smoking = spinnerSmoking.getSelectedItem().toString();
                workout = spinnerWorkOut.getSelectedItem().toString();
                dayoff = spinnerDayOff.getSelectedItem().toString();

                // Calling the method to store data in the database
                setToDataBase();
            }
        });
    }

    // Method to set user profile data in the Firebase Firestore database
    void setToDataBase() {
        // Checking if the profile object already exists
        if (profile != null) {
            // If it exists, update its values
            profile.setJob(job);
            profile.setBloodType(bloodtype);
            profile.setChild(child);
            profile.setDrinking(drinking);
            profile.setSmoking(smoking);
            profile.setWork_out(workout);
            profile.setDay_off(dayoff);
        } else {
            // If it doesn't exist, create a new profile object
            profile = new Profile(job, bloodtype, child, drinking, smoking, workout, dayoff);
        }

        // Initializing Firestore database instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = user.getUid();

        // Adding the profile to the "profile" collection in the database
        db.collection("profile")
                .document(userId)
                .set(profile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Checking if the data insertion was successful
                        if (task.isSuccessful()) {
                            // If successful, navigate to the main activity
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}



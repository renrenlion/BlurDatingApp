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
import com.example.blurdatingapplication.data.PhysicalFeatures;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


public class SetUpProfilePhysicalFeature extends AppCompatActivity {

    Button btnNext;

    Spinner spinnerHeightInt, spinnerHeightDec, spinnerHairColor, spinnerEyeColor,
            spinnerBodyType, spinnerFaceType, spinnerOther;
    String height, hairColor, eyeColor, bodyType, faceType, other;

    FirebaseAuth auth;
    FirebaseUser user;

    PhysicalFeatures physicalFeatures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile_physical_feature);

        btnNext = findViewById(R.id.btn_next);
        spinnerHeightInt = findViewById(R.id.spinner_height_int);
        spinnerHeightDec = findViewById(R.id.spinner_height_dec);
        spinnerHairColor = findViewById(R.id.spinner_hairColor);
        spinnerEyeColor = findViewById(R.id.spinner_eyeColor);
        spinnerBodyType = findViewById(R.id.spinner_bodytype);
        spinnerFaceType = findViewById(R.id.spinner_facetype);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                height = spinnerHeightInt.getSelectedItem().toString() + spinnerHeightDec.getSelectedItem().toString();
                hairColor = spinnerHairColor.getSelectedItem().toString();
                eyeColor = spinnerEyeColor.getSelectedItem().toString();
                bodyType = spinnerBodyType.getSelectedItem().toString();
                faceType = spinnerFaceType.getSelectedItem().toString();

                setToDataBase();
            }
        });
    }

    void setToDataBase() {
        if (physicalFeatures != null) {
            physicalFeatures.setHeight(height);
            //physicalFeatures.setWeight(weight);
            physicalFeatures.setHairColor(hairColor);
            physicalFeatures.setEyeColor(eyeColor);
            physicalFeatures.setBodyType(bodyType);
            physicalFeatures.setFacialType(faceType);

        }
        else {
            physicalFeatures = new PhysicalFeatures(height,null,hairColor,eyeColor,bodyType,faceType);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = user.getUid();

        db.collection("physicalFeatures")
                .document(userId)
                .set(physicalFeatures)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), SetUpInterest.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
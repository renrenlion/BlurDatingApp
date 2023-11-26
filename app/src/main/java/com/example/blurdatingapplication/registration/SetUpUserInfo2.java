package com.example.blurdatingapplication.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.blurdatingapplication.R;
import com.example.blurdatingapplication.data.CheckedUser;
import com.example.blurdatingapplication.data.PhysicalFeatures;
import com.example.blurdatingapplication.data.Preference;
import com.example.blurdatingapplication.data.Profile;
import com.example.blurdatingapplication.data.UserData;
import com.example.blurdatingapplication.data.WaitUser;
import com.example.blurdatingapplication.utils.FunctionUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetUpUserInfo2 extends AppCompatActivity {
    Button buttonNext;

    TextView editTextLocation, editTextPhoneNumber,editTextBirthday;
    Spinner spinnerGender, spinnerPreferredGender;
    String username, phoneNumber, birthday, gender, preferredGender, email;
    int age, location;
    FirebaseAuth auth;
    FirebaseUser user;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_user_info2);

        buttonNext = findViewById(R.id.btn_next);

        editTextLocation = findViewById(R.id.location);
        editTextPhoneNumber = findViewById(R.id.phone_number);
        editTextBirthday = findViewById(R.id.birthday);
        spinnerGender = findViewById(R.id.spinner_gender);
        spinnerPreferredGender = findViewById(R.id.spinner_preferred_gender);


        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        email = user.getEmail();


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location = Integer.parseInt(editTextLocation.getText().toString());
                phoneNumber = editTextPhoneNumber.getText().toString();
                birthday = editTextBirthday.getText().toString();
                gender = spinnerGender.getSelectedItem().toString();
                preferredGender = spinnerPreferredGender.getSelectedItem().toString();
                age = FunctionUtil.calculateAge(birthday);
                set();
            }
        });
    }

    void set() {
        String empty = "-";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = user.getUid();

        if (userData != null) {
            userData.setEmail(email);
            userData.setUsername(username);
            userData.setPhoneNumber(phoneNumber);
            userData.setLocation(location);
            userData.setGender(stringToIntGender(gender));
            userData.setPreferredGender(stringToIntGender(preferredGender));
            userData.setCreatedTimestamp(Timestamp.now());
        }
        else {
            userData = new UserData(email, userId, username, phoneNumber, Timestamp.now(), birthday, location, stringToIntGender(gender), stringToIntGender(preferredGender), "-1","-1","-1", 0);
        }

        Profile userProfile = new Profile(empty,empty,empty,empty,empty,empty,empty);
        Preference userPreference = new Preference("0.0","000",empty,empty,empty,empty);
        PhysicalFeatures userPhysicalFeatures = new PhysicalFeatures("0.0","000",empty,empty,empty,empty);
        CheckedUser checkedUser = new CheckedUser();
        WaitUser waitUser = new WaitUser();

        db.collection("users")
                .document(userId)
                .set(userData);

        db.collection("profile")
                .document(userId)
                .set(userProfile);

        db.collection("physicalFeatures")
                .document(userId)
                .set(userPhysicalFeatures);

        db.collection("preference")
                .document(userId)
                .set(userPreference);


        db.collection("checkedUser")
                .document(userId)
                .set(checkedUser);

        db.collection("waitUser")
                .document(userId)
                .set(waitUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), SetUpUserInfo3.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    int stringToIntGender(String gender){
        switch (gender){
            case "Man": return 1;
            case "Woman": return 2;
            default: return 0;
        }
    }
}
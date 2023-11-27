package com.example.blurdatingapplication.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.blurdatingapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SetUpUserInfo extends AppCompatActivity {
    TextInputEditText  editTextUserName;
    Button buttonNext;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_user_info);

        editTextUserName = findViewById(R.id.buildingUsername);
        progressBar = findViewById(R.id.progressBar);
        buttonNext = findViewById(R.id.btn_next);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUserName.getText().toString();

                isUsernameAlreadyInUse(username);
            }
        });
    }

    private void isUsernameAlreadyInUse(String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("users");

        // Query Firestore to check if the username already exists
        Query query = usersCollection.whereEqualTo("username", username);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null && !task.getResult().isEmpty()) {
                    Toast.makeText(SetUpUserInfo.this, "username is already in use", Toast.LENGTH_LONG).show();

                } else {
                    Intent intent = new Intent(getApplicationContext(), SetUpUserInfo2.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }
}

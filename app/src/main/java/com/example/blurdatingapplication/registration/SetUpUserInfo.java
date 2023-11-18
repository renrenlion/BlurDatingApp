package com.example.blurdatingapplication.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.blurdatingapplication.R;
import com.google.android.material.textfield.TextInputEditText;

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

                Intent intent = new Intent(getApplicationContext(), SetUpUserInfo2.class);
                intent.putExtra("username", username);

                // Move to next activity
                startActivity(intent);
                finish();
            }
        });
    }
}

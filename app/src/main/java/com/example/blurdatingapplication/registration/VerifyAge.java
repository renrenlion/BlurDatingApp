package com.example.blurdatingapplication.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blurdatingapplication.Login;
import com.example.blurdatingapplication.R;

public class VerifyAge extends AppCompatActivity {

    RadioButton stats;
    Button buttonNext;

    TextView textSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_age);

        stats = findViewById(R.id.rbAgreeAge);
        buttonNext = findViewById(R.id.btn_goRegister);
        textSignIn = findViewById(R.id.signInNow);

        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stats.isChecked()){
                    Intent intent = new Intent(getApplicationContext(), Register.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(VerifyAge.this, "You can't create an account.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
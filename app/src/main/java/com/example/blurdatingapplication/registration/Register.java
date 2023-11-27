package com.example.blurdatingapplication.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blurdatingapplication.Login;
import com.example.blurdatingapplication.MainActivity;
import com.example.blurdatingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputEditText editTextEmail, editTextPass, editTextRePass;
    Button buttonSignUp;
    TextView textSignIn;
    ProgressBar progressBar;

    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPass = findViewById(R.id.password);
        editTextRePass = findViewById(R.id.re_password);
        buttonSignUp = findViewById(R.id.btn_signup);
        textSignIn = findViewById(R.id.signInNow);
        progressBar = findViewById(R.id.progressBar);

        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, rePassword;
                email = editTextEmail.getText().toString();
                password = editTextPass.getText().toString();
                rePassword = editTextRePass.getText().toString();
                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Enter email and Password", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(email)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(password)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_LONG).show();
                } else{
                    if (!password.equals(rePassword)) {
                        Toast.makeText(Register.this, "Password is not much", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), Register.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register.this, "Account created.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), SetUpUserInfo.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                Toast.makeText(Register.this, "Email is already in use.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }
    public static boolean isEmailInUse(String email) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Task<SignInMethodQueryResult> task = mAuth.fetchSignInMethodsForEmail(email);
        try {
            SignInMethodQueryResult result = task.getResult();
            if (result.getSignInMethods() != null && !result.getSignInMethods().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }
}
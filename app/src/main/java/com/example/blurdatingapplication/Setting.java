package com.example.blurdatingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blurdatingapplication.data.UserData;
import com.example.blurdatingapplication.utils.FireBaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;


public class Setting extends AppCompatActivity {

    LinearLayout layoutLogout;
    ImageView imageViewBack;
    UserData userData;

    TextView textViewDeveloper;
    LinearLayout linearLayoutDeveloperContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        layoutLogout = findViewById(R.id.logout);
        imageViewBack = findViewById(R.id.back);

        textViewDeveloper = findViewById(R.id.developer_text);
        linearLayoutDeveloperContent = findViewById(R.id.developer_content);

        getUserData();


        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseUtil.logout();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        imageViewBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    void getUserData(){
        FireBaseUtil.currentUserData().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    userData = task.getResult().toObject(UserData.class);
                    displayDeveloperContent();
                }
            }
        });
    }

    void displayDeveloperContent() {
        if (userData != null && userData.getPlan() == 2) {
            textViewDeveloper.setVisibility(View.VISIBLE);
            linearLayoutDeveloperContent.setVisibility(View.VISIBLE);
        }
        else {
            textViewDeveloper.setVisibility(View.GONE);
            linearLayoutDeveloperContent.setVisibility(View.GONE);
        }
    }
}

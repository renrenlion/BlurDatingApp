package com.example.blurdatingapplication.registration;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.blurdatingapplication.MainActivity;
import com.example.blurdatingapplication.R;
import com.example.blurdatingapplication.utils.FireBaseUtil;
import com.example.blurdatingapplication.utils.FunctionUtil;

public class SetUpUserProfile extends AppCompatActivity {
    Uri uri;
    ImageView imageView;
    Button buttonCreate, buttonLater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_user_profile);

        buttonCreate = findViewById(R.id.btn_create);
        buttonLater = findViewById(R.id.btn_later);
        imageView = findViewById(R.id.image_face);

        FireBaseUtil.getCurrentFacePicStorageReference().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        uri = task.getResult();
                        FunctionUtil.setFaceImage(SetUpUserProfile.this, uri, imageView);
                    } else {

                        Exception exception = task.getException();
                        if (exception != null) {

                            exception.printStackTrace();
                        }
                    }
                });

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetUpProfilePhysicalFeature.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
package com.example.blurdatingapplication.registration;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.blurdatingapplication.MainActivity;
import com.example.blurdatingapplication.R;
import com.example.blurdatingapplication.utils.FireBaseUtil;
import com.example.blurdatingapplication.utils.FunctionUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SetUpUserInfo3 extends AppCompatActivity {

    private Button buttonNext;
    private ImageView imageView;
    private Button buttonAlbum, buttonCamera;

    private Uri uri;

    FirebaseStorage storage;
    StorageReference storageRef;

    FirebaseAuth auth;
    FirebaseUser user;

    ActivityResultLauncher<Intent> imagePickLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_user_info3);

        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null && data.getData() != null){
                            uri = data.getData();
                            FunctionUtil.setFaceImage(getApplicationContext(),uri,imageView);
                        }
                    }
                });



        buttonNext = findViewById(R.id.btn_complete);
        imageView = findViewById(R.id.image_face);
        buttonCamera = findViewById(R.id.btn_camera);
        buttonAlbum = findViewById(R.id.btn_album);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        buttonNext.setOnClickListener(view -> {
            if (uri != null) {
                FireBaseUtil.getCurrentFacePicStorageReference().putFile(uri);
                Intent intent = new Intent(getApplicationContext(),SetUpUserProfile.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(this, "Please add a face picture.", Toast.LENGTH_LONG).show();
            }
        });

        buttonCamera.setOnClickListener((v -> {
            ImagePicker.with(this)
                    .cameraOnly()
                    .cropSquare()
                    .compress(614)
                    .maxResultSize(614,614)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        }));

        buttonAlbum.setOnClickListener((v -> {
            ImagePicker.with(this)
                    .galleryOnly()
                    .cropSquare()
                    .compress(614)
                    .maxResultSize(614,614)
                    .createIntent(new Function1<Intent, Unit>() {
                @Override
                public Unit invoke(Intent intent) {
                    imagePickLauncher.launch(intent);
                    return null;
                }
            });
        }));
    }
}

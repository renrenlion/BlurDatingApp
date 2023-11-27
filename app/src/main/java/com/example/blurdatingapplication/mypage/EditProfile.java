package com.example.blurdatingapplication.mypage;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blurdatingapplication.MainActivity;
import com.example.blurdatingapplication.R;
import com.example.blurdatingapplication.data.Interest;
import com.example.blurdatingapplication.data.PhysicalFeatures;
import com.example.blurdatingapplication.data.Preference;
import com.example.blurdatingapplication.data.Profile;
import com.example.blurdatingapplication.data.UserData;
import com.example.blurdatingapplication.registration.SetUpInterest;
import com.example.blurdatingapplication.registration.SetUpProfile;
import com.example.blurdatingapplication.registration.SetUpUserInfo3;
import com.example.blurdatingapplication.registration.SetUpUserProfile;
import com.example.blurdatingapplication.utils.FireBaseUtil;
import com.example.blurdatingapplication.utils.FunctionUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class EditProfile extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    ImageView imageViewBack;

    ImageView imageViewUserImage1, imageViewUserImage2,imageViewUserImage3,imageViewUserImage4,
             imageViewUserImage5, imageViewUserImage6;

    ImageView imageViewDelete2, imageViewDelete3,imageViewDelete4, imageViewDelete5, imageViewDelete6;

    ImageView imageViewAdd2,imageViewAdd3, imageViewAdd4, imageViewAdd5, imageViewAdd6;
    Spinner spinnerJob, spinnerBloodType, spinnerChild, spinnerDrinking, spinnerSmoking, spinnerWorkOut, spinnerDayOff;

    Spinner spinnerSport, spinnerMusic, spinnerGaming, spinnerFood, spinnerTraveling, spinnerActivity, spinnerReading;

    Spinner spinnerHeightInt, spinnerHeightDec, spinnerWeigth100, spinnerWeight10, spinnerWeight1,
            spinnerHairColor, spinnerEyeColor, spinnerBodyType, spinnerFacialType;

    Spinner spinnerPHeightInt, spinnerPHeightDec, spinnerPWeigth100, spinnerPWeight10, spinnerPWeight1,
            spinnerPHairColor, spinnerPEyeColor, spinnerPBodyType, spinnerPFacialType;
    Uri uri, uri2, uri3,uri4,uri5,uri6;

    ActivityResultLauncher<Intent> imagePickLauncher1, imagePickLauncher2, imagePickLauncher3, imagePickLauncher4,
            imagePickLauncher5,imagePickLauncher6;

    UserData userData;
    Interest userInterest;
    PhysicalFeatures userPhysicalFeatures;
    Profile userProfile;
    Preference userPreference;

    ArrayAdapter<String> profileJobArrayAdapter, profileBloodTypeArrayAdapter, profileChildArrayAdapter,
            profileDrinkingArrayAdapter, profileSmokingArrayAdapter, profileWorkOutArrayAdapter, profileDayOffArrayAdapter;

    ArrayAdapter<String> PhyscalFeaturesHeightIntArrayAdapter,PhyscalFeaturesHeightDecArrayAdapter,
            PhyscalFeaturesWeigth100ArrayAdapter, PhyscalFeaturesWeigth10ArrayAdapter, PhyscalFeaturesWeigth1ArrayAdapter,
            PhyscalFeaturesHairColorArrayAdapter, PhyscalFeaturesEyeColorArrayAdapter, PhyscalFeaturesBodyTypeArrayAdapter, PhyscalFeaturesFacialTypeArrayAdapter;

    ArrayAdapter<String> PreferenceHeightIntArrayAdapter,PreferenceHeightDecArrayAdapter,
            PreferenceWeigth100ArrayAdapter, PreferenceWeigth10ArrayAdapter, PreferenceWeigth1ArrayAdapter,
            PreferenceHairColorArrayAdapter, PreferenceEyeColorArrayAdapter, PreferenceBodyTypeArrayAdapter, PreferenceFacialTypeArrayAdapter;

    ArrayAdapter<String> InterestSportArrayAdapter, InterestMusicArrayAdapter, InterestGamingArrayAdapter,
            InterestFoodArrayAdapter, InterestTravelingArrayAdapter, InterestActivityArrayAdapter, InterestReadingArrayAdapter;

    String[] jobOpt,bloodTypeOpt, childOpt, drinkingOpt, smokingOpt, workOutOpt, dayOffOpt;
    String[] heightIntOpt, heightDecOpt, weight100Opt, weigth1Opt, weight10Opt,
    hairColorOpt, eyeColorOpt, bodyTypeOpt, facilaTypeOpt;

    String[] PreferenceHeightIntOpt, PreferenceHeightDecOpt, PreferenceWeight100Opt, PreferenceWeigth1Opt, PreferenceWeight10Opt,
            PreferenceHairColorOpt, PreferenceEyeColorOpt, PreferenceBodyTypeOpt, PreferenceFacilaTypeOpt;

    String[] sportOpt, musicOpt, gamingOpt, foodOpt, travelingOpt, activityOpt, readingOpt;


    String job, bloodtype, child, drinking, smoking, workout, dayoff;

    String sport, music, gaming, food, traveling, activity, reading;

    String height, weight, hairColor, eyeColor, bodyType, faceType;

    String preferenceHeight, preferenceWeight, preferenceHairColor, preferenceEyeColor, preferenceBodyType, preferenceFaceType;

    ProgressBar progressbar;

    ScrollView scrollView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        setImagePickLauncher();

        imageViewBack = findViewById(R.id.back);
        imageViewUserImage1 = findViewById(R.id.image_1);
        imageViewUserImage2 = findViewById(R.id.image_2);
        imageViewUserImage3 = findViewById(R.id.image_3);
        imageViewUserImage4 = findViewById(R.id.image_4);
        imageViewUserImage5 = findViewById(R.id.image_5);
        imageViewUserImage6 = findViewById(R.id.image_6);

        imageViewAdd2 = findViewById(R.id.image_add2);
        imageViewAdd3 = findViewById(R.id.image_add3);
        imageViewAdd4 = findViewById(R.id.image_add4);
        imageViewAdd5 = findViewById(R.id.image_add5);
        imageViewAdd6 = findViewById(R.id.image_add6);

        imageViewDelete2 = findViewById(R.id.image_delete2);
        imageViewDelete3 = findViewById(R.id.image_delete3);
        imageViewDelete4 = findViewById(R.id.image_delete4);
        imageViewDelete5 = findViewById(R.id.image_delete5);
        imageViewDelete6 = findViewById(R.id.image_delete6);

        spinnerJob = findViewById(R.id.spinner_job);
        spinnerBloodType = findViewById(R.id.spinner_bloodtype);
        spinnerChild = findViewById(R.id.spinner_child);
        spinnerDrinking = findViewById(R.id.spinner_drinking);
        spinnerSmoking = findViewById(R.id.spinner_smoking);
        spinnerWorkOut = findViewById(R.id.spinner_workout);
        spinnerDayOff = findViewById(R.id.spinner_dayoff);

        spinnerHeightInt = findViewById(R.id.spinner_height_int);
        spinnerHeightDec = findViewById(R.id.spinner_height_dec);
        spinnerWeigth100 = findViewById(R.id.spinner_weight_100);
        spinnerWeight10 = findViewById(R.id.spinner_weight_10);
        spinnerWeight1 = findViewById(R.id.spinner_weight_1);
        spinnerHairColor = findViewById(R.id.spinner_hair_color);
        spinnerEyeColor = findViewById(R.id.spinner_eye_color);
        spinnerBodyType = findViewById(R.id.spinner_bodytype);
        spinnerFacialType = findViewById(R.id.spinner_facetype);

        spinnerSport = findViewById(R.id.spinner_sport);
        spinnerMusic = findViewById(R.id.spinner_music);
        spinnerGaming = findViewById(R.id.spinner_gaming);
        spinnerFood = findViewById(R.id.spinner_food);
        spinnerTraveling = findViewById(R.id.spinner_traveling);
        spinnerActivity = findViewById(R.id.spinner_activity);
        spinnerReading = findViewById(R.id.spinner_reading);

        spinnerPHeightInt = findViewById(R.id.spinner_preference_height_int);
        spinnerPHeightDec = findViewById(R.id.spinner_preference_height_dec);
        spinnerPWeigth100 = findViewById(R.id.spinner_preference_weight_100);
        spinnerPWeight10 = findViewById(R.id.spinner_preference_weight_10);
        spinnerPWeight1 = findViewById(R.id.spinner_preference_weight_1);
        spinnerPHairColor = findViewById(R.id.spinner_preferenc_hair_color);
        spinnerPEyeColor = findViewById(R.id.spinner_preference_eye_color);
        spinnerPBodyType = findViewById(R.id.spinner_preference_bodytype);
        spinnerPFacialType = findViewById(R.id.spinner_preference_facetype);

        progressbar = findViewById(R.id.progressBar);
        scrollView = findViewById(R.id.scrollview_edit_profile);

        scrollView.setVisibility(View.GONE);

        setArrayAdapter();
        getAllUserInfo();


        imageViewUserImage1.setOnClickListener((v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(640)
                    .maxResultSize(640,640)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher1.launch(intent);
                            return null;
                        }
                    });
        }));

        imageViewUserImage2.setOnClickListener((v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(640)
                    .maxResultSize(640,640)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher2.launch(intent);
                            return null;
                        }
                    });
        }));

        imageViewUserImage3.setOnClickListener((v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(640)
                    .maxResultSize(640,640)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher3.launch(intent);
                            return null;
                        }
                    });
        }));

        imageViewUserImage4.setOnClickListener((v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(640)
                    .maxResultSize(640,640)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher4.launch(intent);
                            return null;
                        }
                    });
        }));

        imageViewUserImage5.setOnClickListener((v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(640)
                    .maxResultSize(640,640)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher5.launch(intent);
                            return null;
                        }
                    });
        }));

        imageViewUserImage6.setOnClickListener((v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(640)
                    .maxResultSize(640,640)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher6.launch(intent);
                            return null;
                        }
                    });
        }));

        imageViewDelete2.setOnClickListener(view ->{
            FireBaseUtil.getCurrentPhoto2StorageReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    imageViewDelete2.setVisibility(View.GONE);
                    imageViewAdd2.setVisibility(View.VISIBLE);
                    FunctionUtil.removeProfileImage(EditProfile.this, imageViewUserImage2);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        });

        imageViewDelete3.setOnClickListener(view ->{
            FireBaseUtil.getCurrentPhoto3StorageReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    imageViewDelete3.setVisibility(View.GONE);
                    imageViewAdd3.setVisibility(View.VISIBLE);
                    FunctionUtil.removeProfileImage(EditProfile.this, imageViewUserImage3);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        });

        imageViewDelete4.setOnClickListener(view ->{
            FireBaseUtil.getCurrentPhoto4StorageReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    imageViewDelete4.setVisibility(View.GONE);
                    imageViewAdd4.setVisibility(View.VISIBLE);
                    FunctionUtil.removeProfileImage(EditProfile.this, imageViewUserImage4);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        });

        imageViewDelete5.setOnClickListener(view ->{
            FireBaseUtil.getCurrentPhoto5StorageReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    imageViewDelete5.setVisibility(View.GONE);
                    imageViewAdd5.setVisibility(View.VISIBLE);
                    FunctionUtil.removeProfileImage(EditProfile.this, imageViewUserImage5);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        });

        imageViewDelete6.setOnClickListener(view ->{
            FireBaseUtil.getCurrentPhoto6StorageReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    imageViewDelete6.setVisibility(View.GONE);
                    imageViewAdd6.setVisibility(View.VISIBLE);
                    FunctionUtil.removeProfileImage(EditProfile.this, imageViewUserImage6);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        });

        imageViewBack.setOnClickListener(view -> {
            job = spinnerJob.getSelectedItem().toString();
            bloodtype = spinnerBloodType.getSelectedItem().toString();
            child = spinnerChild.getSelectedItem().toString();
            drinking = spinnerDrinking.getSelectedItem().toString();
            smoking = spinnerSmoking.getSelectedItem().toString();
            workout = spinnerWorkOut.getSelectedItem().toString();
            dayoff = spinnerDayOff.getSelectedItem().toString();

            height = spinnerHeightInt.getSelectedItem().toString() + spinnerHeightDec.getSelectedItem().toString();
            weight = spinnerWeigth100.getSelectedItem().toString() + spinnerWeight10.getSelectedItem().toString() + spinnerWeight1.getSelectedItem().toString();
            hairColor =  spinnerHairColor.getSelectedItem().toString();
            bodyType = spinnerBodyType.getSelectedItem().toString();
            eyeColor = spinnerEyeColor.getSelectedItem().toString();
            faceType = spinnerFacialType.getSelectedItem().toString();

            sport = spinnerSport.getSelectedItem().toString();
            music = spinnerMusic.getSelectedItem().toString();
            gaming = spinnerGaming.getSelectedItem().toString();
            food = spinnerFood.getSelectedItem().toString();
            traveling = spinnerTraveling.getSelectedItem().toString();
            activity = spinnerActivity.getSelectedItem().toString();
            reading = spinnerReading.getSelectedItem().toString();

            preferenceHeight = spinnerPHeightInt.getSelectedItem().toString() + spinnerPHeightDec.getSelectedItem().toString();
            preferenceWeight = spinnerPWeigth100.getSelectedItem().toString() + spinnerPWeight10.getSelectedItem().toString() + spinnerPWeight1.getSelectedItem().toString();
            preferenceHairColor =  spinnerPHairColor.getSelectedItem().toString();
            preferenceBodyType = spinnerPBodyType.getSelectedItem().toString();
            preferenceEyeColor = spinnerPEyeColor.getSelectedItem().toString();
            preferenceFaceType = spinnerPFacialType.getSelectedItem().toString();

            setToDatabase();
            onBackPressed();
        });

    }

    void getAllUserInfo(){
        FireBaseUtil.getCurrentFacePicStorageReference().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        uri  = task.getResult();
                        FunctionUtil.setProfileImage(EditProfile.this,uri,imageViewUserImage1);
                    }
                });

        FireBaseUtil.getCurrentPhoto2StorageReference().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        uri2  = task.getResult();
                        FunctionUtil.setProfileImage(EditProfile.this,uri2,imageViewUserImage2);
                    }
                    if (uri2 == null) {
                        imageViewAdd2.setVisibility(View.VISIBLE);
                        imageViewDelete2.setVisibility(View.GONE);
                    } else {
                        imageViewAdd2.setVisibility(View.GONE);
                        imageViewDelete2.setVisibility(View.VISIBLE);
                    }
                });

        FireBaseUtil.getCurrentPhoto3StorageReference().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        uri3  = task.getResult();
                        FunctionUtil.setProfileImage(EditProfile.this,uri3,imageViewUserImage3);
                    }
                    if (uri3 == null) {
                        imageViewAdd3.setVisibility(View.VISIBLE);
                        imageViewDelete3.setVisibility(View.GONE);
                    } else {
                        imageViewAdd3.setVisibility(View.GONE);
                        imageViewDelete3.setVisibility(View.VISIBLE);
                    }
                });

        FireBaseUtil.getCurrentPhoto4StorageReference().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        uri4  = task.getResult();
                        FunctionUtil.setProfileImage(EditProfile.this,uri4,imageViewUserImage4);
                    }
                    if (uri4 == null) {
                        imageViewAdd4.setVisibility(View.VISIBLE);
                        imageViewDelete4.setVisibility(View.GONE);
                    } else {
                        imageViewAdd4.setVisibility(View.GONE);
                        imageViewDelete4.setVisibility(View.VISIBLE);
                    }
                });

        FireBaseUtil.getCurrentPhoto5StorageReference().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        uri5  = task.getResult();
                        FunctionUtil.setProfileImage(EditProfile.this,uri5, imageViewUserImage5);
                    }
                    if (uri5 == null) {
                        imageViewAdd5.setVisibility(View.VISIBLE);
                        imageViewDelete5.setVisibility(View.GONE);
                    } else {
                        imageViewAdd5.setVisibility(View.GONE);
                        imageViewDelete5.setVisibility(View.VISIBLE);
                    }
                });

        FireBaseUtil.getCurrentPhoto6StorageReference().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        uri6  = task.getResult();
                        FunctionUtil.setProfileImage(EditProfile.this,uri6, imageViewUserImage6);
                    }
                    if (uri6 == null) {
                        imageViewAdd6.setVisibility(View.VISIBLE);
                        imageViewDelete6.setVisibility(View.GONE);
                    } else {
                        imageViewAdd6.setVisibility(View.GONE);
                        imageViewDelete6.setVisibility(View.VISIBLE);
                    }
                });


        FireBaseUtil.currentUserData().get().addOnCompleteListener(task -> {
            userData = task.getResult().toObject(UserData.class);

        });



        FireBaseUtil.currentUserProfile().get().addOnCompleteListener(task -> {
            userProfile = task.getResult().toObject(Profile.class);

            if(user != null){
                int position = profileJobArrayAdapter.getPosition(userProfile.getJob());
                spinnerJob.setSelection(position);

                position = profileBloodTypeArrayAdapter.getPosition(userProfile.getBloodType());
                spinnerBloodType.setSelection(position);

                position = profileChildArrayAdapter.getPosition(userProfile.getChild());
                spinnerChild.setSelection(position);

                position = profileDrinkingArrayAdapter.getPosition(userProfile.getDrinking());
                spinnerDrinking.setSelection(position);

                position = profileSmokingArrayAdapter.getPosition(userProfile.getSmoking());
                spinnerSmoking.setSelection(position);

                position = profileWorkOutArrayAdapter.getPosition(userProfile.getWork_out());
                spinnerWorkOut.setSelection(position);

                position = profileDayOffArrayAdapter.getPosition(userProfile.getDay_off());
                spinnerDayOff.setSelection(position);
            }
        });

        FireBaseUtil.currentUserInterest().get().addOnCompleteListener(task -> {
            userInterest = task.getResult().toObject(Interest.class);

            if(userInterest != null){
                int position = InterestSportArrayAdapter.getPosition(userInterest.getSport());
                spinnerSport.setSelection(position);

                position = InterestMusicArrayAdapter.getPosition(userInterest.getMusic());
                spinnerMusic.setSelection(position);

                position = InterestGamingArrayAdapter.getPosition(userInterest.getGaming());
                spinnerGaming.setSelection(position);

                position = InterestFoodArrayAdapter.getPosition(userInterest.getFood());
                spinnerFood.setSelection(position);

                position = InterestTravelingArrayAdapter.getPosition(userInterest.getTraveling());
                spinnerTraveling.setSelection(position);

                position = InterestActivityArrayAdapter.getPosition(userInterest.getActivity());
                spinnerActivity.setSelection(position);

                position = InterestReadingArrayAdapter.getPosition(userInterest.getReading());
                spinnerReading.setSelection(position);
            }

        });

        FireBaseUtil.currentUserPhysicalFeatures().get().addOnCompleteListener(task -> {
            userPhysicalFeatures = task.getResult().toObject(PhysicalFeatures.class);

            String heightTemp = userPhysicalFeatures.getHeight();
            String weightTemp = userPhysicalFeatures.getWeight();


            if (userPhysicalFeatures != null) {
                int position = PhyscalFeaturesHeightIntArrayAdapter.getPosition(String.valueOf(heightTemp.charAt(0)));
                spinnerHeightInt.setSelection(position);

                position = PhyscalFeaturesHeightDecArrayAdapter.getPosition(String.valueOf(heightTemp.charAt(1)) + String.valueOf(heightTemp.charAt(2)));
                spinnerHeightDec.setSelection(position);

                position = PhyscalFeaturesWeigth100ArrayAdapter.getPosition(String.valueOf(weightTemp.charAt(0)));
                spinnerWeigth100.setSelection(position);

                position = PhyscalFeaturesWeigth10ArrayAdapter.getPosition(String.valueOf(weightTemp.charAt(1)));
                spinnerWeight10.setSelection(position);

                position = PhyscalFeaturesWeigth1ArrayAdapter.getPosition(String.valueOf(weightTemp.charAt(2)));
                spinnerWeight1.setSelection(position);

                position = PhyscalFeaturesHairColorArrayAdapter.getPosition(userPhysicalFeatures.getHairColor());
                spinnerHairColor.setSelection(position);

                position = PhyscalFeaturesEyeColorArrayAdapter.getPosition(userPhysicalFeatures.getEyeColor());
                spinnerEyeColor.setSelection(position);

                position = PhyscalFeaturesFacialTypeArrayAdapter.getPosition(userPhysicalFeatures.getFacialType());
                spinnerFacialType.setSelection(position);

                position = PhyscalFeaturesBodyTypeArrayAdapter.getPosition(userPhysicalFeatures.getBodyType());
                spinnerBodyType.setSelection(position);
            }
        });

        FireBaseUtil.currentUserPreference().get().addOnCompleteListener(task -> {
            userPreference = task.getResult().toObject(Preference.class);

            String heightTemp = userPreference.getHeight();
            String weightTemp = userPreference.getWeight();


            if (userPreference != null) {

                int position = PreferenceHeightIntArrayAdapter.getPosition(String.valueOf(heightTemp.charAt(0)));
                spinnerPHeightInt.setSelection(position);

                position = PreferenceHeightDecArrayAdapter.getPosition(String.valueOf(heightTemp.charAt(1)) + String.valueOf(heightTemp.charAt(2)));
                spinnerPHeightDec.setSelection(position);

                position = PreferenceWeigth100ArrayAdapter.getPosition(String.valueOf(weightTemp.charAt(0)));
                spinnerPWeigth100.setSelection(position);

                position = PreferenceWeigth10ArrayAdapter.getPosition(String.valueOf(weightTemp.charAt(1)));
                spinnerPWeight10.setSelection(position);

                position = PreferenceWeigth1ArrayAdapter.getPosition(String.valueOf(weightTemp.charAt(2)));
                spinnerPWeight1.setSelection(position);

                position = PreferenceHairColorArrayAdapter.getPosition(userPreference.getHairColor());
                spinnerPHairColor.setSelection(position);

                position = PreferenceEyeColorArrayAdapter.getPosition(userPreference.getEyeColor());
                spinnerPEyeColor.setSelection(position);

                position = PreferenceFacialTypeArrayAdapter.getPosition(userPreference.getFacialType());
                spinnerPFacialType.setSelection(position);

                position = PreferenceBodyTypeArrayAdapter.getPosition(userPreference.getBodyType());
                spinnerPBodyType.setSelection(position);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.GONE);
                }
            },1000);
        });
    }

    void setArrayAdapter(){
        //Profile

        jobOpt = getResources().getStringArray(R.array.job_list); // Assuming job_list is defined in strings.xml
        bloodTypeOpt = getResources().getStringArray(R.array.bloodtype_list); // Assuming job_list is defined in strings.xml
        childOpt = getResources().getStringArray(R.array.answer_list); // Assuming job_list is defined in strings.xml
        drinkingOpt = getResources().getStringArray(R.array.frequency_list); // Assuming job_list is defined in strings.xml
        smokingOpt = getResources().getStringArray(R.array.frequency_list); // Assuming job_list is defined in strings.xml
        workOutOpt = getResources().getStringArray(R.array.frequency_list); // Assuming job_list is defined in strings.xml
        dayOffOpt = getResources().getStringArray(R.array.dayoff_list); // Assuming job_list is defined in strings.xml

        profileJobArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jobOpt);
        profileBloodTypeArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodTypeOpt);
        profileChildArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, childOpt);
        profileDrinkingArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, drinkingOpt);
        profileSmokingArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, smokingOpt);
        profileWorkOutArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, workOutOpt);
        profileDayOffArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dayOffOpt);


        profileJobArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileBloodTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileChildArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileDrinkingArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileSmokingArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileWorkOutArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileDayOffArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Physical Features
        heightIntOpt = getResources().getStringArray(R.array.number_array);
        heightDecOpt = getResources().getStringArray(R.array.decimal_array);
        weight100Opt = getResources().getStringArray(R.array.number_array);
        weight10Opt = getResources().getStringArray(R.array.number_array);
        weigth1Opt = getResources().getStringArray(R.array.number_array);
        hairColorOpt = getResources().getStringArray(R.array.hair_color_list);
        eyeColorOpt = getResources().getStringArray(R.array.eye_color_list);
        bodyTypeOpt = getResources().getStringArray(R.array.body_type_list);
        facilaTypeOpt = getResources().getStringArray(R.array.face_type_list);

        PhyscalFeaturesHeightIntArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, heightIntOpt);
        PhyscalFeaturesHeightDecArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, heightDecOpt);
        PhyscalFeaturesWeigth100ArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weight100Opt);
        PhyscalFeaturesWeigth10ArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weight100Opt);
        PhyscalFeaturesWeigth1ArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weigth1Opt);
        PhyscalFeaturesHairColorArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hairColorOpt);
        PhyscalFeaturesEyeColorArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, eyeColorOpt);
        PhyscalFeaturesBodyTypeArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bodyTypeOpt);
        PhyscalFeaturesFacialTypeArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, facilaTypeOpt);

        PhyscalFeaturesHeightIntArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PhyscalFeaturesHeightDecArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PhyscalFeaturesWeigth100ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PhyscalFeaturesWeigth10ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PhyscalFeaturesWeigth1ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PhyscalFeaturesHairColorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PhyscalFeaturesEyeColorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PhyscalFeaturesBodyTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PhyscalFeaturesFacialTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Interest
        sportOpt = getResources().getStringArray(R.array.sport_list);
        musicOpt = getResources().getStringArray(R.array.music_list);
        gamingOpt = getResources().getStringArray(R.array.gaming_list);
        foodOpt = getResources().getStringArray(R.array.food_list);
        travelingOpt = getResources().getStringArray(R.array.traveling_list);
        activityOpt = getResources().getStringArray(R.array.activity_list);
        readingOpt = getResources().getStringArray(R.array.reading_list);

        InterestSportArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sportOpt);
        InterestMusicArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, musicOpt);
        InterestGamingArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gamingOpt);
        InterestFoodArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, foodOpt);
        InterestTravelingArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, travelingOpt);
        InterestActivityArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activityOpt);
        InterestReadingArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, readingOpt);

        InterestSportArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        InterestMusicArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        InterestGamingArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        InterestFoodArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        InterestTravelingArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        InterestActivityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        InterestReadingArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Preference
        PreferenceHeightIntOpt = getResources().getStringArray(R.array.number_array);
        PreferenceHeightDecOpt = getResources().getStringArray(R.array.decimal_array);
        PreferenceWeight100Opt = getResources().getStringArray(R.array.number_array);
        PreferenceWeight10Opt = getResources().getStringArray(R.array.number_array);
        PreferenceWeigth1Opt = getResources().getStringArray(R.array.number_array);
        PreferenceHairColorOpt = getResources().getStringArray(R.array.hair_color_list);
        PreferenceEyeColorOpt = getResources().getStringArray(R.array.eye_color_list);
        PreferenceBodyTypeOpt = getResources().getStringArray(R.array.body_type_list);
        PreferenceFacilaTypeOpt = getResources().getStringArray(R.array.face_type_list);

        PreferenceHeightIntArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PreferenceHeightIntOpt);
        PreferenceHeightDecArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PreferenceHeightDecOpt);
        PreferenceWeigth100ArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PreferenceWeight100Opt);
        PreferenceWeigth10ArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PreferenceWeight100Opt);
        PreferenceWeigth1ArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PreferenceWeigth1Opt);
        PreferenceHairColorArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PreferenceHairColorOpt);
        PreferenceEyeColorArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PreferenceEyeColorOpt);
        PreferenceBodyTypeArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PreferenceBodyTypeOpt);
        PreferenceFacialTypeArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PreferenceFacilaTypeOpt);

        PreferenceHeightIntArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PreferenceHeightDecArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PreferenceWeigth100ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PreferenceWeigth10ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PreferenceWeigth1ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PreferenceHairColorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PreferenceEyeColorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PreferenceBodyTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PreferenceFacialTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    void setImagePickLauncher(){
        imagePickLauncher1 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null && data.getData() != null){
                            uri = data.getData();
                            FunctionUtil.setProfileImage(getApplicationContext(),uri,imageViewUserImage1);
                        }

                    }
                });
        imagePickLauncher2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null && data.getData() != null){
                            uri2 = data.getData();
                            FunctionUtil.setProfileImage(getApplicationContext(),uri2,imageViewUserImage2);
                            imageViewAdd2.setVisibility(View.GONE);
                            imageViewDelete2.setVisibility(View.VISIBLE);
                        }
                        else{
                            imageViewAdd2.setVisibility(View.VISIBLE);
                            imageViewDelete2.setVisibility(View.GONE);
                        }
                    }
                });
        imagePickLauncher3 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null && data.getData() != null){
                            uri3 = data.getData();
                            FunctionUtil.setProfileImage(getApplicationContext(),uri3,imageViewUserImage3);
                            imageViewAdd3.setVisibility(View.GONE);
                            imageViewDelete3.setVisibility(View.VISIBLE);
                        }
                        else{
                            imageViewAdd3.setVisibility(View.VISIBLE);
                            imageViewDelete3.setVisibility(View.GONE);
                        }
                    }
                });
        imagePickLauncher4 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null && data.getData() != null){
                            uri4 = data.getData();
                            FunctionUtil.setProfileImage(getApplicationContext(),uri4,imageViewUserImage4);
                            imageViewAdd4.setVisibility(View.GONE);
                            imageViewDelete4.setVisibility(View.VISIBLE);
                        }
                        else{
                            imageViewAdd4.setVisibility(View.VISIBLE);
                            imageViewDelete4.setVisibility(View.GONE);
                        }
                    }
                });
        imagePickLauncher5 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            uri5 = data.getData();
                            FunctionUtil.setProfileImage(getApplicationContext(), uri5, imageViewUserImage5);
                            imageViewAdd5.setVisibility(View.GONE);
                            imageViewDelete5.setVisibility(View.VISIBLE);
                        } else {
                            imageViewAdd5.setVisibility(View.VISIBLE);
                            imageViewDelete5.setVisibility(View.GONE);
                        }
                    }
                });
        imagePickLauncher6 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null && data.getData() != null){
                            uri6 = data.getData();
                            FunctionUtil.setProfileImage(getApplicationContext(),uri6,imageViewUserImage6);
                            imageViewAdd6.setVisibility(View.GONE);
                            imageViewDelete6.setVisibility(View.VISIBLE);
                        }
                        else{
                            imageViewAdd6.setVisibility(View.VISIBLE);
                            imageViewDelete6.setVisibility(View.GONE);
                        }
                    }
                });
    }

    void setToDatabase() {
        // Initializing Firestore database instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = user.getUid();

        if (uri != null) {
            FireBaseUtil.getCurrentFacePicStorageReference().putFile(uri);
        }
        if (uri2 != null) {
            FireBaseUtil.getCurrentPhoto2StorageReference().putFile(uri2);
        }
        if (uri3 != null) {
            FireBaseUtil.getCurrentPhoto3StorageReference().putFile(uri3);
        }
        if (uri4 != null) {
            FireBaseUtil.getCurrentPhoto4StorageReference().putFile(uri4);
        }
        if (uri5 != null) {
            FireBaseUtil.getCurrentPhoto5StorageReference().putFile(uri5);
        }
        if (uri6 != null) {
            FireBaseUtil.getCurrentPhoto6StorageReference().putFile(uri6);
        }

        if (userProfile != null) {
            // If it exists, update its values
            userProfile.setJob(job);
            userProfile.setBloodType(bloodtype);
            userProfile.setChild(child);
            userProfile.setDrinking(drinking);
            userProfile.setSmoking(smoking);
            userProfile.setWork_out(workout);
            userProfile.setDay_off(dayoff);
        } else {
            // If it doesn't exist, create a new profile object
            userProfile = new Profile(job, bloodtype, child, drinking, smoking, workout, dayoff);
        }

        if (userInterest != null) {
            userInterest.setSport(sport);
            userInterest.setMusic(music);
            userInterest.setGaming(gaming);
            userInterest.setFood(food);
            userInterest.setTraveling(traveling);
            userInterest.setActivity(activity);
            userInterest.setReading(reading);
        } else{
            userInterest = new Interest(sport,music,gaming,food, traveling, activity, reading);
        }

        if (userPhysicalFeatures != null) {
            userPhysicalFeatures.setHeight(height);
            userPhysicalFeatures.setWeight(weight);
            userPhysicalFeatures.setHairColor(hairColor);
            userPhysicalFeatures.setEyeColor(eyeColor);
            userPhysicalFeatures.setBodyType(bodyType);
            userPhysicalFeatures.setFacialType(faceType);

        }
        else {
            userPhysicalFeatures = new PhysicalFeatures(height,weight,hairColor,eyeColor,bodyType,faceType);
        }

        if (userPreference!= null) {
            userPreference.setHeight(preferenceHeight);
            userPreference.setWeight(preferenceWeight);
            userPreference.setHairColor(preferenceHairColor);
            userPreference.setEyeColor(preferenceEyeColor);
            userPreference.setBodyType(preferenceBodyType);
            userPreference.setFacialType(preferenceFaceType);

        }
        else {
            userPreference = new Preference(preferenceHeight,preferenceWeight,preferenceHairColor,preferenceEyeColor,preferenceBodyType,preferenceFaceType);
        }

        db.collection("interest").document(userId).set(userInterest);
        db.collection("profile").document(userId).set(userProfile);
        db.collection("physicalFeatures").document(userId).set(userPhysicalFeatures);
        db.collection("preference").document(userId).set(userPreference);
    }
}
package com.example.blurdatingapplication.automaticmatching;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.blurdatingapplication.R;
import com.example.blurdatingapplication.data.Interest;
import com.example.blurdatingapplication.data.PhysicalFeatures;
import com.example.blurdatingapplication.data.Preference;
import com.example.blurdatingapplication.data.Profile;
import com.example.blurdatingapplication.data.UserData;
import com.example.blurdatingapplication.utils.FireBaseUtil;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class AutomaticMatchingFragment extends Fragment {

    Button buttonNext;
    UserData userData;
    Interest userInterest;
    Profile userProfile;
    Preference userPreference;
    PhysicalFeatures userPhysicalFeatures;

    public AutomaticMatchingFragment() {
        // Empty constructor
    }

    @Override   // This is how the code connects to the .xml file.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_automatic_matching, container, false);

        buttonNext = view.findViewById(R.id.button);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            String otherUserSport;
            String otherUserMusic;
            String otherUserGaming;
            String otherUserFood;
            String otherUserTraveling;
            String otherUserReading;
            String otherUserActivity;
            double height;
            double weight;
            @Override
            public void onClick(View v) {   // Idea is to loop over 300 times. Only have a handfull of user and they are hardcoded so not needed.
                int points = 0;
                double heightPref = Double.parseDouble(userPreference.getHeight());
                double weightPref = Double.parseDouble(userPreference.getWeight());

                FireBaseUtil.otherUserInterest("IqBeL4gR91QkI7ytFFP7nBrz4xe2").get().addOnCompleteListener(task -> {
                    otherUserSport = userInterest.getSport();
                    otherUserMusic = userInterest.getMusic();
                    otherUserGaming = userInterest.getGaming();
                    otherUserFood = userInterest.getFood();
                    otherUserTraveling = userInterest.getTraveling();
                    otherUserReading = userInterest.getReading();
                    otherUserActivity = userInterest.getActivity();
                });
                FireBaseUtil.otherUserPhysicalFeatures("IqBeL4gR91QkI7ytFFP7nBrz4xe2").get().addOnCompleteListener(task -> {
                    height = Double.parseDouble(userPhysicalFeatures.getHeight());
                    weight = Double.parseDouble(userPhysicalFeatures.getWeight());
                });

                // Figure out how to retrieve other users information and compare to users information.
                // Currently user information is being compared to users information.

                // 2 points in height range a, 1 point in height range b.
                if (height < heightPref + 0.2 && height > heightPref - 0.2) {
                    points += 2;
                } else if (height < heightPref + 0.4 && height > heightPref - 0.4) {
                    points += 1;
                }

                // 2 points in weight range a, 1 point in weight range b.
                if (weight < weightPref + 0.5 && weight > weightPref - 0.5) {
                    points += 2;
                } else if (weight < weightPref + 1 && weight > weightPref - 1) {
                    points += 1;
                }

                if (userPreference.getHairColor().equals(userPhysicalFeatures.getHairColor())) {
                    points += 1;
                }

                if (userPreference.getEyeColor().equals(userPhysicalFeatures.getEyeColor())) {
                    points += 1;
                }

                if (userInterest.getSport().equals(otherUserSport)){
                    // giving warning in .equals() because its checking itself. Find out how to get other user data.
                    points += 1;
                }

                if (userInterest.getMusic().equals(otherUserMusic)){
                    points += 1;
                }

                if (userInterest.getGaming().equals(otherUserGaming)){
                    points += 1;
                }

                if (userData.getGender() == userData.getGender()){  // int value.
                    points += 1;
                }

                if (userInterest.getFood().equals(otherUserFood)){
                    points += 1;
                }

                if (userInterest.getTraveling().equals(otherUserTraveling)){
                    points += 1;
                }

                if (userInterest.getReading().equals(otherUserReading)){
                    points += 1;
                }

                if (userInterest.getActivity().equals(otherUserActivity)){
                    points += 1;
                }

                if (points >= 10){   // 10/14 = 0.71
                    // This is where the user sends his request.
                }
                else {
                    // This is were user ignores other user and increases there "No Match" by one.
                }
            }
        });

        return view;
    }

    void getUserInfo() {

        FireBaseUtil.currentUserData().get().addOnCompleteListener(task -> {
            userData = task.getResult().toObject(UserData.class);
        });

        FireBaseUtil.currentUserInterest().get().addOnCompleteListener(task -> {
            userInterest = task.getResult().toObject(Interest.class);
        });

        FireBaseUtil.currentUserProfile().get().addOnCompleteListener(task -> {
            userProfile = task.getResult().toObject(Profile.class);
        });

        FireBaseUtil.currentUserPhysicalFeatures().get().addOnCompleteListener(task -> {
            userPhysicalFeatures = task.getResult().toObject(PhysicalFeatures.class);
        });

        FireBaseUtil.currentUserPreference().get().addOnCompleteListener(task -> {
            userPreference = task.getResult().toObject(Preference.class);
        });
    }
}
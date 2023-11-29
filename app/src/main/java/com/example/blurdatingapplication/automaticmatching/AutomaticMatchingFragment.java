package com.example.blurdatingapplication.automaticmatching;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.blurdatingapplication.R;
import com.example.blurdatingapplication.chat.ChatroomModel;
import com.example.blurdatingapplication.data.Interest;
import com.example.blurdatingapplication.data.PhysicalFeatures;
import com.example.blurdatingapplication.data.Preference;
import com.example.blurdatingapplication.data.Profile;
import com.example.blurdatingapplication.data.UserData;
import com.example.blurdatingapplication.data.WaitUser;
import com.example.blurdatingapplication.utils.FireBaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class AutomaticMatchingFragment extends Fragment {

    int points = 0;
    int matchCounter = 0;

    Button buttonNext;
    UserData userData;
    Interest userInterest;
    Profile userProfile;
    Preference userPreference;
    PhysicalFeatures userPhysicalFeatures;

    UserData currentUserData;

    public AutomaticMatchingFragment() {
        // Empty constructor
    }

    public interface UserIdCallback {
        void onUserIdRetrieved(String otherUserId);
    }

    public interface UserInfoCallback {
        void onUserInfoFetched();
    }



    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_automatic_matching, container, false);

        buttonNext = view.findViewById(R.id.button);


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the callback to retrieve the other user ID and perform matching
                Log.d("UserDataPlanValue", "User Plan Value: " + userData.getPlan());
                if ((userData.getPlan() == 1) || (userData.getPlan() == 2)) { // Checks if its a premium plan or a developer plan.
                    retrieveAnotherUserIdAndMatch(new UserIdCallback() {
                        @Override
                        public void onUserIdRetrieved(String otherUserId) {
                            Log.d("performMatching", "Points: " + points);
                            if (points >= 10) {  // 10/14 = ~0.71
                                Log.d("performMatching", "Matching conditions met. Points: " + points);
                                performMatchingWithOtherUser(otherUserId);
                            } else {
                                Log.d("performMatching", "Not enough points for matching");
                            }
                        }
                    });
                }
                else {
                    showToastError();
                }
            }
        });
        Log.d("AutomaticMatchingFragment", "Before getUserInfo()");
        getUserInfo(() -> {
            Log.d("AutomaticMatchingFragment", "All user info fetched successfully");
        });

        return view;
    }

    private void retrieveAnotherUserIdAndMatch(UserIdCallback callback) {
        FireBaseUtil.currentUserData().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    if (userDocument.exists()) {
                        currentUserData = userDocument.toObject(UserData.class);

                        Query query = FireBaseUtil.allUserCollectionUserData()
                                .whereEqualTo("gender", currentUserData.getPreferredGender());

                        query.get().addOnCompleteListener(queryTask -> {
                            if (queryTask.isSuccessful()) {
                              //  UserData otherUserData = null;
                                for (QueryDocumentSnapshot otherDocument : queryTask.getResult()) {
                                    points = 0;
                                   UserData otherUserData = otherDocument.toObject(UserData.class);

                                    // Get other user's interests
                                    FireBaseUtil.otherUserInterest(otherUserData.getUid()).get().addOnCompleteListener(interestTask -> {
                                        if (interestTask.isSuccessful()) {
                                            DocumentSnapshot interestDocument = interestTask.getResult();
                                            if (interestDocument.exists()) {
                                                Interest otherUserInterest = interestTask.getResult().toObject(Interest.class);
                                                if (otherUserInterest != null && userInterest != null) {
                                                    Log.d("performMatching", "User Interest: " + userInterest.toString());
                                                    Log.d("performMatching", "Other User Interest: " + otherUserInterest.toString());
                                                    String otherUserSport = otherUserInterest.getSport();
                                                    String otherUserMusic = otherUserInterest.getMusic();
                                                    String otherUserGaming = otherUserInterest.getGaming();
                                                    String otherUserFood = otherUserInterest.getFood();
                                                    String otherUserTraveling = otherUserInterest.getTraveling();
                                                    String otherUserReading = otherUserInterest.getReading();
                                                    String otherUserActivity = otherUserInterest.getActivity();

                                                    if (userInterest.getSport().equals(otherUserSport)) {
                                                        points += 1;
                                                    }
                                                    if (userInterest.getMusic().equals(otherUserMusic)) {
                                                        points += 1;
                                                    }
                                                    if (userInterest.getGaming().equals(otherUserGaming)) {
                                                        points += 1;
                                                    }
                                                    if (userInterest.getFood().equals(otherUserFood)) {
                                                        points += 1;
                                                    }
                                                    if (userInterest.getTraveling().equals(otherUserTraveling)) {
                                                        points += 1;
                                                    }
                                                    if (userInterest.getReading().equals(otherUserReading)) {
                                                        points += 1;
                                                    }
                                                    if (userInterest.getActivity().equals(otherUserActivity)) {
                                                        points += 1;
                                                    }

                                                    Log.d("getOtherUserId", "Other user's interests: " + otherUserInterest.toString());
                                                }
                                            }
                                        } else {
                                            Log.e("getOtherUserId", "Failed to get other user's interests", interestTask.getException());
                                        }
                                    });

                                    UserData finalOtherUserData = otherUserData;
                                    FireBaseUtil.otherUserPhysicalFeatures(otherUserData.getUid()).get().addOnCompleteListener(physicalTask -> {
                                        if (physicalTask.isSuccessful()) {
                                            PhysicalFeatures otherUserPhysicalFeatures = physicalTask.getResult().toObject(PhysicalFeatures.class);
                                            if (otherUserPhysicalFeatures != null && userPreference != null) {
                                                Log.d("performMatching", "User Interest: " + userPreference.toString());
                                                Log.d("performMatching", "Other User Interest: " + otherUserPhysicalFeatures.toString());
                                                double otherUserHeight = Double.parseDouble(otherUserPhysicalFeatures.getHeight());
                                                double otherUserWeight = Double.parseDouble(otherUserPhysicalFeatures.getWeight());
                                                String otherUserHairColor = otherUserPhysicalFeatures.getHairColor();
                                                String otherUserEyeColor = otherUserPhysicalFeatures.getEyeColor();
                                                double height = Double.parseDouble(userPreference.getHeight());
                                                double weight = Double.parseDouble(userPreference.getWeight());
                                                String Hair = userPreference.getHairColor();
                                                String Eyes = userPreference.getEyeColor();
                                                int otherUserGender = finalOtherUserData.getGender();

                                                // 2 points in height range a, 1 point in height range b.
                                                if (height < otherUserHeight + 0.2 && height > otherUserHeight - 0.2) {
                                                    points += 2;
                                                } else if (height < otherUserHeight + 0.4 && height > otherUserHeight - 0.4) {
                                                    points += 1;
                                                }
                                                // 2 points in weight range a, 1 point in weight range b.
                                                if (weight < otherUserWeight + 0.5 && weight > otherUserWeight - 0.5) {
                                                    points += 2;
                                                } else if (weight < otherUserWeight + 1 && weight > otherUserWeight - 1) {
                                                    points += 1;
                                                }
                                                if (Hair.equals(otherUserHairColor)) {
                                                    points += 1;
                                                }
                                                if (Eyes.equals(otherUserEyeColor)) {
                                                    points += 1;
                                                }
                                                if (userData.getPreferredGender() == otherUserGender) {
                                                    points += 1;
                                                }

                                                Log.d("getOtherUserId", "Other user's physical features: " + otherUserPhysicalFeatures.toString());
                                                Log.d("Points", "Current number of Points: " + points);
                                            }
                                            Log.d("Points", "Current number of Points: " + points);

                                            if (points >= 10){
                                                Log.d("performMatching", "Matching conditions met. Points: " + points);
                                                performMatchingWithOtherUser(otherUserData.getUid());
                                            }
                                        } else {
                                            Log.e("getOtherUserId", "Failed to get other user's interests", physicalTask.getException());
                                        }
                                    });
                                }
                            } else {
                                Log.e("getOtherUserId", "Failed to get user data from query", queryTask.getException());
                            }
                        });
                    } else {
                        Log.e("getOtherUserId", "Empty currentUserData");
                    }
                } else {
                    Log.e("getOtherUserId", "Failed to get currentUserData", task.getException());
                }
            }
        });
    }

    private void performMatchingWithOtherUser(String otherUserId) {
        Log.d("performMatching", "Inside performMatchingWithOtherUser");
        if (otherUserId != null) {
            Log.d("performMatching", "Performing matching with other user ID: " + otherUserId);
            // Assuming you want to call setWaitedUserIds after successful matching
            setWaitedUserIds(otherUserId);
            matchCounter++;
            Log.d("performMatching", "Number of matches: " + matchCounter);

            // Remove both users from the waitlist after successful matching
            removeFromWaitList(FireBaseUtil.getUserID());
            removeFromWaitList(otherUserId);

            // Create or retrieve the chat room model
            getOrCreateChatRoomModel(otherUserId);

            showToast();
        } else {
            Log.e("performMatching", "Other user ID is null");
        }
    }

    private void showToast() {
        Toast.makeText(getContext(), "New Chatrooms Available!", Toast.LENGTH_SHORT).show();
    }

    private void showToastError(){
        Toast.makeText(getContext(), "Premium Plan Required!", Toast.LENGTH_SHORT).show();
    }

    private void showToastExist(){
        Toast.makeText(getContext(), "No New Matches!", Toast.LENGTH_SHORT).show();
    }

    private void setWaitedUserIds(String otherUserID) {
        // Get the ID of the user being swiped
        FireBaseUtil.otherUserWaitUserList(otherUserID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                WaitUser otherUserWaitUserList = task.getResult().toObject(WaitUser.class);

                if (otherUserWaitUserList == null) {
                    otherUserWaitUserList = new WaitUser();
                }

                otherUserWaitUserList.addToUserIds(FireBaseUtil.getUserID());

                // Update the user IDs list in Firestore
                FireBaseUtil.otherUserWaitUserList(otherUserID).set(otherUserWaitUserList)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("onCardSwiped", "User ID added to wait user");
                        })
                        .addOnFailureListener(e -> {
                            Log.e("onCardSwiped", "Failed to update", e);
                        });
            }
        });
    }

    private void removeFromWaitList(String userId) {
        FireBaseUtil.currentUserWaitList().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                WaitUser currentUserWaitList = task.getResult().toObject(WaitUser.class);

                if (currentUserWaitList != null) {
                    currentUserWaitList.removeUserId(userId);

                    // Update the WaitUser object on Firebase after removing the swiped user
                    FireBaseUtil.currentUserWaitList()
                            .set(currentUserWaitList)
                            .addOnCompleteListener(updateTask -> {
                                if (updateTask.isSuccessful()) {
                                    Log.d("removeFromWaitList", "Successfully removed user from wait list");
                                } else {
                                    Log.e("removeFromWaitList", "Failed to update currentUserWaitList", updateTask.getException());
                                }
                            });
                }
            } else {
                Log.e("removeFromWaitList", "Failed to get currentUserWaitList data", task.getException());
            }
        });
    }

    @SuppressLint("LongLogTag")
    void getOrCreateChatRoomModel(String otherUserId) {
        List<String> userIds = Arrays.asList(FireBaseUtil.currentUserId(), otherUserId);
        String chatroomId = generateChatroomId(userIds);

        // Reference to the chat room document
        DocumentReference chatRoomRef = FireBaseUtil.getChatroomReference(chatroomId);

        chatRoomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    ChatroomModel chatroomModel = document.toObject(ChatroomModel.class);
                } else {
                    // Chat room doesn't exist, create a new one
                    ChatroomModel chatroomModel = new ChatroomModel(null, "", "", userIds, chatroomId);
                    chatRoomRef.set(chatroomModel);
                }
            } else {
                Log.e("getOrCreateChatRoomModel", "Failed to get chat room data", task.getException());
            }
        });
    }

    private String generateChatroomId(List<String> userIds) {
        return String.join("_", userIds);
    }


    private void getUserInfo(UserInfoCallback callback) {
        FireBaseUtil.currentUserData().get().addOnCompleteListener(task -> {
            userData = task.getResult().toObject(UserData.class);
            Log.d("getUserInfo", "userData: " + userData);

            FireBaseUtil.currentUserInterest().get().addOnCompleteListener(interestTask -> {
                userInterest = interestTask.getResult().toObject(Interest.class);
                Log.d("getUserInfo", "userInterest: " + userInterest);

                FireBaseUtil.currentUserProfile().get().addOnCompleteListener(profileTask -> {
                    userProfile = profileTask.getResult().toObject(Profile.class);
                    Log.d("getUserInfo", "userProfile: " + userProfile);

                    FireBaseUtil.currentUserPhysicalFeatures().get().addOnCompleteListener(physicalTask -> {
                        userPhysicalFeatures = physicalTask.getResult().toObject(PhysicalFeatures.class);
                        Log.d("getUserInfo", "userPhysicalFeatures: " + userPhysicalFeatures);

                        FireBaseUtil.currentUserPreference().get().addOnCompleteListener(preferenceTask -> {
                            userPreference = preferenceTask.getResult().toObject(Preference.class);
                            if (userPreference != null) {
                                Log.d("getUserInfo", "userPreference: " + userPreference);

                                // Now that all data is fetched, invoke the callback
                                callback.onUserInfoFetched();
                            } else {
                                Log.e("getUserInfo", "userPreference is null");
                                // Handle the case where userPreference is null
                            }
                        });
                    });
                });
            });
        });
    }

}

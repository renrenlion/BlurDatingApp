package com.example.blurdatingapplication.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.FirestoreClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FireBaseUtil {

    public static String getUserID(){return FirebaseAuth.getInstance().getUid();
    }

    //public static String

    public static DocumentReference currentUserData(){
        return FirebaseFirestore.getInstance().collection("users").document(getUserID());
    }

    public static DocumentReference currentUserInterest(){
        return FirebaseFirestore.getInstance().collection("interest").document(getUserID());
    }

    public static DocumentReference currentUserProfile(){
        return FirebaseFirestore.getInstance().collection("profile").document(getUserID());
    }

    public static DocumentReference currentUserPhysicalFeatures(){
        return FirebaseFirestore.getInstance().collection("physicalfeatures").document(getUserID());
    }

    public static DocumentReference currentUserPreference(){
        return FirebaseFirestore.getInstance().collection("preference").document(getUserID());
    }

    public static DocumentReference currentUserPayment(){   // For profit monitoring.
        return FirebaseFirestore.getInstance().collection("paymentWeek").document(getUserID());
    }
    //////////////////////////////////////////////////////////////////////

    public static DocumentReference otherUserInterest(String otherUserId){
        return FirebaseFirestore.getInstance().collection("interest").document(otherUserId);
    }

    public static DocumentReference otherUserPhysicalFeatures(String otherUserId){
        return FirebaseFirestore.getInstance().collection("physicalFeatures").document(otherUserId);
    }

    /////////////////////////////////////////////////////////////////////

    public static StorageReference getCurrentFacePicStorageReference(){
        return FirebaseStorage.getInstance().getReference().child(FireBaseUtil.getUserID()).child("face_pic")
                .child("f" + FireBaseUtil.getUserID());
    }

    public static StorageReference getCurrentBlurPicStorageReference(){
        return FirebaseStorage.getInstance().getReference().child(FireBaseUtil.getUserID()).child("blur_pic")
                .child("b" + FireBaseUtil.getUserID());
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }
}

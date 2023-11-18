package com.example.blurdatingapplication.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FireBaseUtil {

    public static String getUserID(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static DocumentReference currentUserData(){
        return FirebaseFirestore.getInstance().collection("users").document(getUserID());
    }

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

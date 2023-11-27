package com.example.blurdatingapplication.utils;


import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;

import com.example.blurdatingapplication.data.UserData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FireBaseUtil {

    //########################################### getCurrentUserData #########################################################
    public static String getUserID() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static DocumentReference currentUserData() {
        return FirebaseFirestore.getInstance().collection("users").document(getUserID());
    }

    public static CollectionReference usersCollectionReference() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference currentUserProfile() {
        return FirebaseFirestore.getInstance().collection("profile").document(getUserID());
    }

    public static DocumentReference currentUserInterest() {
        return FirebaseFirestore.getInstance().collection("interest").document(getUserID());
    }

    public static DocumentReference currentUserPhysicalFeatures() {
        return FirebaseFirestore.getInstance().collection("physicalFeatures").document(getUserID());
    }

    public static DocumentReference currentUserPreference() {
        return FirebaseFirestore.getInstance().collection("preference").document(getUserID());
    }

    public static CollectionReference allUserCollectionUserData() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static CollectionReference userCollectionWaitUser() {
        return FirebaseFirestore.getInstance().collection("waitUser");
    }

    public static DocumentReference currentUserCheckedUserList() {
        return FirebaseFirestore.getInstance().collection("checkedUser").document(getUserID());
    }

    public static DocumentReference otherUserWaitUserList(String otherUserId) {
        return FirebaseFirestore.getInstance().collection("waitUser").document(otherUserId);
    }

    public static DocumentReference currentUserWaitList() {
        return FirebaseFirestore.getInstance().collection("waitUser").document(getUserID());
    }

    //####Get Other User Profile, Interest, PhysicalFeatures from userData otheruser.getUserId()############

    public static DocumentReference otherUserData(String otherUserId) {
        return FirebaseFirestore.getInstance().collection("users").document(otherUserId);
    }

    public static DocumentReference otherUserProfile(String otherUserId) {
        return FirebaseFirestore.getInstance().collection("profile").document(otherUserId);
    }

    public static DocumentReference otherUserInterest(String otherUserId) {
        return FirebaseFirestore.getInstance().collection("interest").document(otherUserId);
    }

    public static DocumentReference otherUserPhysicalFeatures(String otherUserId) {
        return FirebaseFirestore.getInstance().collection("physicalFeatures").document(otherUserId);
    }

//########################################### Photo #######################################################################################
    public static StorageReference getCurrentFacePicStorageReference() {
        return FirebaseStorage.getInstance().getReference().child(FireBaseUtil.getUserID()).child("face_pic")
                .child("f" + FireBaseUtil.getUserID());
    }

    public static StorageReference getCurrentPhoto2StorageReference() {
        return FirebaseStorage.getInstance().getReference().child(FireBaseUtil.getUserID()).child("pic_2")
                .child("p2" + FireBaseUtil.getUserID());
    }

    // ######################### CHAT COMPONENT #########################

    // GET CHAT ROOM ID
    public static String getChatroomId(String userId1, String userId2) {
        if(userId1.hashCode()< userId2.hashCode()) {
            return userId1+"_"+userId2;
        }else{
            return userId2+"_"+userId1;
        }
    }

    public interface UsernameCallback{
        void onUsernameReceived(String username);
        void onError(Exception e);
    }

    // ALL CHATROOM COLLECTION REFERENCES
    public static CollectionReference chatroomCollectionReferece() {
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    // GET CHATROOM REFERENCE
    public static DocumentReference getChatroomReference(String chatroomId) {
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatMessagesReference(String chatroomId) {
        return FirebaseFirestore.getInstance().collection(chatroomId).document(chatroomId).collection("messages");
    }

    public static DocumentReference getOtherUserFromChatroom(List<String> userIds){
        if(userIds.get(0).equals(FireBaseUtil.currentUserId())){
            return usersCollectionReference().document(userIds.get(1));
        }else{
            return usersCollectionReference().document(userIds.get(0));
        }
    }

    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static String timestampToString(Timestamp timestamp) {
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }

    public static StorageReference getCurrentPhoto3StorageReference() {
        return FirebaseStorage.getInstance().getReference().child(FireBaseUtil.getUserID()).child("pic_3")
                .child("p3" + FireBaseUtil.getUserID());
    }

    public static StorageReference getCurrentPhoto4StorageReference() {
        return FirebaseStorage.getInstance().getReference().child(FireBaseUtil.getUserID()).child("pic_4")
                .child("p4" + FireBaseUtil.getUserID());
    }

    public static StorageReference getCurrentPhoto5StorageReference() {
        return FirebaseStorage.getInstance().getReference().child(FireBaseUtil.getUserID()).child("pic_5")
                .child("p5" + FireBaseUtil.getUserID());
    }

    public static StorageReference getCurrentPhoto6StorageReference() {
        return FirebaseStorage.getInstance().getReference().child(FireBaseUtil.getUserID()).child("pic_6")
                .child("p6" + FireBaseUtil.getUserID());
    }

    public static StorageReference getOtherFacePicStorageReference(String otherUserID) {
        return FirebaseStorage.getInstance().getReference().child(otherUserID).child("face_pic")
                .child("f" + otherUserID);
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public static void getUsernameForUserId(String userId, UsernameCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = document.getString("username");
                            callback.onUsernameReceived(username);
                        }
                        callback.onError(new Exception("User not found"));
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

}
    // ######################### CHAT COMPONENT #########################

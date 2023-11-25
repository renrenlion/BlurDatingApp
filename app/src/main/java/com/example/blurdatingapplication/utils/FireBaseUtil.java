package com.example.blurdatingapplication.utils;

import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FireBaseUtil {

    public static String getUserID(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static DocumentReference currentUserData(){
        return FirebaseFirestore.getInstance().collection("users").document(getUserID());
    }

    public static CollectionReference usersCollectionReference() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static StorageReference getCurrentFacePicStorageReference(){
        return FirebaseStorage.getInstance().getReference().child(FireBaseUtil.getUserID()).child("face_pic")
                .child("f" + FireBaseUtil.getUserID());
    }

    public static StorageReference getCurrentBlurPicStorageReference(){
        return FirebaseStorage.getInstance().getReference().child(FireBaseUtil.getUserID()).child("blur_pic")
                .child("b" + FireBaseUtil.getUserID());
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

    public static void getUsernameForUserId(String userId, UsernameCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
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
    // ######################### CHAT COMPONENT #########################
    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }
}

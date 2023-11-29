package com.example.blurdatingapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blurdatingapplication.chat.ChatAdapter;
import com.example.blurdatingapplication.chat.ChatModel;
import com.example.blurdatingapplication.utils.FireBaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    TextView otherUserTextView, countdownTimerTextView;
    Button requestUnblurButton, acceptDeclineBtn;
    ImageView unblur_request_indicator;
    RecyclerView chatMessagesRecyclerView;
    EditText messageInputEditText;
    ImageButton sendMessageButton;
    ChatAdapter chatAdapter;
    FirestoreRecyclerOptions<ChatModel> options;

    // Timer functionality
    boolean isCountdownStarted = false;
    int messageCounter = 0;
    CountDownTimer countDownTimer;
    long timeLeftInMillis = 4 * 60 * 60 * 1000; // Initial time: 24 hours
    boolean timerRunning = false;
    boolean isUnblurRequestPending = false;

    private DocumentReference chatroomDocRef;

    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String chatroomId = getIntent().getStringExtra("chatroomId");

        // Initialize UI elements
        otherUserTextView = findViewById(R.id.other_user);
        chatMessagesRecyclerView = findViewById(R.id.chat_messages);
        messageInputEditText = findViewById(R.id.message_input);
        sendMessageButton = findViewById(R.id.send_message_btn);
        countdownTimerTextView = findViewById(R.id.timer_countdown);
        unblur_request_indicator = findViewById(R.id.unblur_request_indicator);
        requestUnblurButton = findViewById(R.id.request_unblur_btn);
        acceptDeclineBtn = findViewById(R.id.accept_decline);


        // Set up RecyclerView
        chatMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configure FirestoreRecyclerOptions
        CollectionReference chatMessageRef = FirebaseFirestore.getInstance()
                .collection("chatrooms")
                .document(chatroomId)
                .collection("messages");

        query = chatMessageRef.orderBy("timestamp", Query.Direction.ASCENDING);

        options = new FirestoreRecyclerOptions.Builder<ChatModel>()
                .setQuery(query, ChatModel.class)
                .build();

        chatAdapter = new ChatAdapter(options, this);
        chatMessagesRecyclerView.setAdapter(chatAdapter);

        // Check and update unblurRequestAccepeted field
        checkUnblurRequestStatus(chatroomId);

        // Set up click listener for the send button
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInputEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(message)) {
                    // Add the message to Firestore
                    addMessageToFirestore(message);
                }
            }
        });

        // Set back button click listener
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        setOtherUserIdInTextView(chatroomId);

        // Set up click listener for the "Request to unblur" button
        requestUnblurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUnblurRequest();
            }
        });

        // Set up click listener for the "Accept / Decline" button
        acceptDeclineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAcceptDeclineDialog();
            }
        });

        Button unmatchBtn = findViewById(R.id.unmatch);
        unmatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a confirmation dialog before proceeding with the deletion
                showUnmatchConfirmationDialog();
            }
        });

    }

    // ############################## UNBLUR REQUESTS METHODS ##############################################

    private void handleUnblurRequest() {
        String chatroomId = getIntent().getStringExtra("chatroomId");
        DocumentReference chatroomDocRef = FirebaseFirestore.getInstance()
                .collection("chatrooms")
                .document(chatroomId);

        String currentUserId = FireBaseUtil.currentUserId();

        // Check if the user has already sent a request
        chatroomDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> unblurRequest = (Map<String, Object>) documentSnapshot.get("unblurRequest");
                    if (unblurRequest != null && "pending".equals(unblurRequest.get("status"))) {
                        Toast.makeText(ChatActivity.this, "You already have a pending request", Toast.LENGTH_SHORT).show();
                    } else {
                        Map<String, Object> unblurRequestMap = new HashMap<>();
                        unblurRequestMap.put("senderId", currentUserId);
                        unblurRequestMap.put("status", "pending");

                        chatroomDocRef.update("unblurRequest", unblurRequestMap)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("FirestoreDebug", "Unblur request added successfully");
                                    // Update the UI for pending unblur request
                                    isUnblurRequestPending = true;
                                    updateUIForUnblurRequest();
                                    Log.d("FirestoreDebug", "isUnblurRequestPending: " + isUnblurRequestPending);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FirestoreDebug", "Failed to add unblur request", e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreDebug", "Failed to check unblur request status", e);
                });
    }
    private void checkUnblurRequestStatus(String chatroomId) {
        String currentUserId = FireBaseUtil.currentUserId();

        chatroomDocRef = FirebaseFirestore.getInstance()
                .collection("chatrooms")
                .document(chatroomId);

        chatroomDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve the unblur request map
                        Map<String, Object> unblurRequest = (Map<String, Object>) documentSnapshot.get("unblurRequest");

                        if (unblurRequest != null) {
                            String senderId = (String) unblurRequest.get("senderId");
                            String status = (String) unblurRequest.get("status");

                            // Now, use retrieveOtherUserId asynchronously
                            retrieveOtherUserId(chatroomId, (otherUserId) -> {
                                if (currentUserId.equals(senderId) && "pending".equals(status)) {
                                    // The current user sent a pending unblur request, hide the "Accept / Decline" button
                                    acceptDeclineBtn.setVisibility(View.GONE);
                                    requestUnblurButton.setVisibility(View.VISIBLE); // Show the "Request to unblur" button
                                } else if (currentUserId.equals(otherUserId) && "pending".equals(status)) {
                                    // The current user received a pending unblur request, show the "Accept / Decline" button
                                    acceptDeclineBtn.setVisibility(View.VISIBLE);
                                    requestUnblurButton.setVisibility(View.GONE); // Hide the "Request to unblur" button
                                } else {
                                    // No pending unblur requests or request has been accepted/declined
                                    // Show the "Accept / Decline" button or "Request to unblur" button accordingly
                                    updateUIBasedOnUnblurRequestStatus(status);
                                }
                            });
                        } else {
                            // No unblur request, show the "Accept / Decline" button or "Request to unblur" button accordingly
                            updateUIBasedOnUnblurRequestStatus("");
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreDebug", "Failed to check unblur request status", e);
                });
    }

    // Define an interface to handle the result asynchronously
    interface UserIdCallback {
        void onUserId(String userId);
    }

    private void retrieveOtherUserId(String chatroomId, UserIdCallback callback) {
        DocumentReference chatroomRef = FirebaseFirestore.getInstance()
                .collection("chatrooms")
                .document(chatroomId);

        chatroomRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> userIds = (List<String>) documentSnapshot.get("userIds");

                        if (userIds != null && userIds.size() == 2) {
                            String currentUserId = FireBaseUtil.getUserID();
                            String otherUserId = (userIds.get(0).equals(currentUserId)) ? userIds.get(1) : userIds.get(0);

                            // Now, you have the other user's ID (otherUserId)
                            // You can use it as needed in your code
                            Log.d("FirestoreDebug", "Other User ID: " + otherUserId);

                            // Callback with the result
                            callback.onUserId(otherUserId);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreDebug", "Failed to retrieve chatroom", e);
                });
    }

    private void updateUIBasedOnUnblurRequestStatus(String status) {
        if ("accepted".equals(status)) {
            // Update UI for accepted unblur request
            updateUIForAcceptedUnblurRequest();
        } else if ("declined".equals(status)) {
            // Update UI for declined unblur request
            updateUIForDeclinedUnblurRequest();
        } else {
            // No pending unblur requests or request has been accepted/declined
            // Show the "Accept / Decline" button or "Request to unblur" button accordingly
            acceptDeclineBtn.setVisibility(View.VISIBLE);
            requestUnblurButton.setVisibility(View.GONE); // or View.VISIBLE based on your requirement
        }
    }

    private void showAcceptDeclineDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unblur Request");
        builder.setMessage("You have a pending unblur request. Do you want to accept or decline");

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleAcceptUnblurRequest();
            }
        });

        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Handle the user's decline of the unblur request
                handleDeclineUnblurRequest();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void handleAcceptUnblurRequest() {
        String chatroomId = getIntent().getStringExtra("chatroomId");

        // Update the unblurRequestAccepted field in the chatroom document
        DocumentReference chatroomDocRef = FirebaseFirestore.getInstance()
                .collection("chatrooms")
                .document(chatroomId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("unblurRequest.status", "accepted");

        // Update the document with the new status
        chatroomDocRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirestoreDebug", "Unblur request accepted successfully");
                    // Update the UI based on the acceptance
                    updateUIForAcceptedUnblurRequest();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreDebug", "Failed to accept unblur request", e);
                });
    }

    private void handleDeclineUnblurRequest() {
        String chatroomId = getIntent().getStringExtra("chatroomId");
        DocumentReference chatroomDocRef = FirebaseFirestore.getInstance()
                .collection("chatrooms")
                .document(chatroomId);

        // Update the unblur request status to "declined"
        Map<String, Object> updates = new HashMap<>();
        updates.put("unblurRequest.status", "declined");

        // Update the document with the new status
        chatroomDocRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirestoreDebug", "Unblur request declined successfully");
                    // Update the UI for declined unblur request
                    updateUIForDeclinedUnblurRequest();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreDebug", "Failed to decline unblur request", e);
                });
    }

    private void updateUIForUnblurRequest() {
        requestUnblurButton.setText("Unblur Request Pending");
        unblur_request_indicator.setVisibility(View.VISIBLE);
    }

    // Update UI for accepted unblur request
    private void updateUIForAcceptedUnblurRequest() {
        acceptDeclineBtn.setVisibility(View.GONE);
        requestUnblurButton.setVisibility(View.GONE);

        otherUserTextView.setClickable(true);

        otherUserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOtherUserImagePopup();
            }
        });

        otherUserTextView.setTextColor(Color.DKGRAY);
    }

    // Update UI for declined unblur request
    private void updateUIForDeclinedUnblurRequest() {
        acceptDeclineBtn.setVisibility(View.GONE);
        requestUnblurButton.setVisibility(View.VISIBLE);
    }

    // ############################## OTHER USERNAME TEXTVIEW #########################################

    private void setOtherUserIdInTextView(String chatroomId) {
        DocumentReference chatroomRef = FirebaseFirestore.getInstance()
                .collection("chatrooms")
                .document(chatroomId);

        chatroomRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        List<String> userIds = (List<String>) documentSnapshot.get("userIds");

                        if(userIds != null && userIds.size() == 2) {
                            String otherUserId = (userIds.get(0).equals(FireBaseUtil.getUserID())) ? userIds.get(1) : userIds.get(0);

                            //checkIfCurrentUserSentRequest(chatroomId, otherUserId);

                            FirebaseFirestore.getInstance().collection("users")
                                    .document(otherUserId)
                                    .get()
                                    .addOnSuccessListener(userDocument -> {
                                        if(userDocument.exists()) {
                                            String otherUsername = userDocument.getString("username");
                                            if(otherUsername != null){
                                                otherUserTextView.setText(otherUsername);
                                            } else {
                                                Log.e("FirestoreDebug", "Username not found for user ID: " + otherUserId);
                                            }
                                        } else {
                                            Log.e("FirestoreDebug","User not found for user ID: " + otherUserId);
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("FirestoreDebug","Error fetching username: " + e.getMessage());
                                    });
                        }
                    }
                }).addOnFailureListener(e -> {
                    Log.e("FirestoreDebug","Failed to retrieve chatroom", e);
                });
    }

    // ############################## SAVE MESSAGE TO FIRESTORE #######################################

    private void addMessageToFirestore(String message) {
        String chatroomId = getIntent().getStringExtra("chatroomId");
        String userId = FireBaseUtil.getUserID();

        // Reference to the specific chatroom collection
        CollectionReference chatroomRef = FirebaseFirestore.getInstance()
                .collection("chatrooms")
                .document(chatroomId)
                .collection("messages");

        // Create a new message
        ChatModel newMessage = new ChatModel(new Timestamp(new Date()), message, userId);

        // Add the new message to Firestore
        chatroomRef.add(newMessage)
                .addOnSuccessListener(documentReference -> {
                    // Message added successfully
                    messageInputEditText.setText("");
                    Log.d("FirestoreDebug", "Message added successfully");
                    updateLastMessageInChatroom(chatroomId, newMessage);
                    handleCountdownTimer();
                })
                .addOnFailureListener(e -> {
                    // Handle the failure to add the message
                    Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreDebug", "Failed to send message", e);
                });
    }

    private void updateLastMessageInChatroom(String chatroomId, ChatModel newMessage) {
        // Reference to the specific chatroom document
        DocumentReference chatroomDocRef = FirebaseFirestore.getInstance()
                .collection("chatrooms")
                .document(chatroomId);

        // Update the lastM and lastMSenderId fields with the latest message
        chatroomDocRef.update("lastM", newMessage.getMessage(), "lastMSenderId", newMessage.getSenderId())
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirestoreDebug", "Last message and senderId updated successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreDebug", "Failed to update last message and senderId", e);
                });

        chatAdapter.updateLastMessage(chatroomId, newMessage.getMessage());
    }

    private void updateLastMSenderId(String chatroomId, String senderId) {
        DocumentReference chatroomDocRef = FirebaseFirestore.getInstance()
                .collection("chatrooms")
                .document(chatroomId);

        // Update lastMSenderId with the senderId of the latest message
        chatroomDocRef.update("lastMSenderId", senderId)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirestoreDebug", "LastMSenderId updated successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreDebug", "Failed to update lastMSenderId", e);
                });
    }

    private AlertDialog alertDialog;
    private void showOtherUserImagePopup() {
        String chatroomId = getIntent().getStringExtra("chatroomId");

        // Retrieve the other user's ID
        retrieveOtherUserId(chatroomId, otherUserId -> {
            // Check if the other user's face pic is available
            FireBaseUtil.getOtherFacePicStorageReference(otherUserId).getDownloadUrl()
                    .addOnCompleteListener(uriTask -> {
                        if (uriTask.isSuccessful()) {
                            // Image URL retrieved successfully
                            Uri imageUrl = uriTask.getResult();
                            if (imageUrl != null) {
                                // Show the image in a dialog
                                showImageInDialog(imageUrl);
                            } else {
                                Toast.makeText(this, "Image not available", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Handle the failure to retrieve the image URL
                            Log.e("FirestoreDebug", "Failed to get other user image URL", uriTask.getException());
                            Toast.makeText(this, "Failed to get other user image", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void showImageInDialog(Uri imageUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.popup_image, null);
        ImageView imageView = dialogView.findViewById(R.id.popup_image_view);

        // Load the image using a library like Glide or Picasso
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);

        imageView.setOnClickListener(v -> alertDialog.dismiss());

        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.show();
    }


    private void handleCountdownTimer() {
        messageCounter++;

        if(messageCounter == 2 && !isCountdownStarted) {
            startCountdownTimer();
            isCountdownStarted = true;
        }
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownTimerText();
            }

            @Override
            public void onFinish() {

            }
        };

        countDownTimer.start();
        timerRunning = true;
    }

    private void updateCountdownTimerText() {
        countdownTimerTextView = findViewById(R.id.timer_countdown);
        countdownTimerTextView.setText(formatTime(timeLeftInMillis));
    }

    private String formatTime(long millis) {
        int hours = (int)(millis / 1000) / 3600;
        int minutes = (int)((millis/1000) % 3600) / 60;
        int seconds = (int) (millis/ 1000) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


    private void showUnmatchConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unmatch Confirmation");
        builder.setMessage("Are you sure you want to unmatch? This action cannot be undone.");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User confirmed, proceed with the unmatch
                unmatchAndDeleteChatroom();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User canceled the unmatch
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void unmatchAndDeleteChatroom() {
        String chatroomId = getIntent().getStringExtra("chatroomId");

        // TODO: Implement the logic to delete the chatroom here
        // For example, you can use FirebaseFirestore to delete the chatroom document

        FirebaseFirestore.getInstance()
                .collection("chatrooms")
                .document(chatroomId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Chatroom deleted successfully
                        Toast.makeText(ChatActivity.this, "Chatroom deleted", Toast.LENGTH_SHORT).show();
                        finish(); // Close the ChatActivity after deletion
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure to delete the chatroom
                        Log.e("FirestoreDebug", "Failed to delete chatroom", e);
                        Toast.makeText(ChatActivity.this, "Failed to delete chatroom", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart(){
        super.onStart();
        chatAdapter.startListening();
        Log.d("FirestoreDebug","Listening for chat messages...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatAdapter.stopListening();
        if(timerRunning) {
            countDownTimer.cancel();
        }
    }
}

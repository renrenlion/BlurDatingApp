package com.example.blurdatingapplication;

//import android.content.Intent;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blurdatingapplication.chat.ChatAdapter;
import com.example.blurdatingapplication.chat.ChatModel;
import com.example.blurdatingapplication.utils.FireBaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    TextView otherUserTextView;
    TextView countdownTimerTextView;
    //private Button requestUnblurButton;
    RecyclerView chatMessagesRecyclerView;
    EditText messageInputEditText;
    ImageButton sendMessageButton, back_button;
    ChatAdapter chatAdapter;
    FirestoreRecyclerOptions<ChatModel> options;

    // Timer functionality
    boolean isCountdownStarted = false;
    int messageCounter = 0;
    CountDownTimer countDownTimer;
    long timeLeftInMillis = 4 * 60 * 60 * 1000; // Initial time: 24 hours
    boolean timerRunning = false;


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
        back_button = findViewById(R.id.back_button);

        //countdownTimerTextView = findViewById(R.id.timer_countdown);
        //requestUnblurButton = findViewById(R.id.request_unblur_btn);

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
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setOtherUserIdInTextView(chatroomId);
    }

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
                            otherUserTextView.setText(otherUserId);
                        }
                    }
                }).addOnFailureListener(e -> {
                    Log.e("FirestoreDebug","Failed to retrieve chatroom", e);
                });
    }
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

        // Update the lastM field with the latest message
        chatroomDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String currentLastM = documentSnapshot.getString("lastM");
                        if (currentLastM == null || currentLastM.isEmpty() || currentLastM.equals("New Match")) {
                            // If lastM doesn't exist or is empty, set it to "New Match"
                            chatroomDocRef.update("lastM", "New Match")
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("FirestoreDebug", "Initial lastM set successfully");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("FirestoreDebug", "Failed to set initial lastM", e);
                                    });
                        } else {
                            // If lastM already exists, update it with the latest message
                            chatroomDocRef.update("lastM", newMessage.getMessage())
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("FirestoreDebug", "Last message updated successfully");
                                        updateLastMSenderId(chatroomId, newMessage.getSenderId());
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("FirestoreDebug", "Failed to update last message", e);
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreDebug", "Failed to get chatroom document", e);
                });
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

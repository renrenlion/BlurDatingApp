// ChatFragment.java
package com.example.blurdatingapplication.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blurdatingapplication.ChatActivity;
import com.example.blurdatingapplication.R;
import com.example.blurdatingapplication.utils.FireBaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class ChatFragment extends Fragment {

    private View rootView;
    private RecyclerView chatroomRecyclerView;
    private ChatroomAdapter chatroomAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chatroomRef = db.collection("chatrooms");

    public ChatFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceStates) {
        super.onViewCreated(view, savedInstanceStates);
        initRecyclerView();
    }

    private void initRecyclerView() {
        chatroomRecyclerView = rootView.findViewById(R.id.chatroom_list);
        chatroomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirestoreRecyclerOptions<ChatroomModel> options = new FirestoreRecyclerOptions.Builder<ChatroomModel>()
                .setQuery(chatroomRef.whereArrayContains("userIds", FireBaseUtil.getUserID()), ChatroomModel.class)
                .build();

        Objects.requireNonNull(options);

        chatroomAdapter = new ChatroomAdapter(options, getContext(), new ChatroomAdapter.OnChatroomClickListener() {
            @Override
            public void onChatroomClick(ChatroomModel chatroom) {
                openChatActivity(chatroom.getChatroomId());
            }
        });
        chatroomRecyclerView.setAdapter(chatroomAdapter);
    }

    private void openChatActivity(String chatroomId) {
        Intent intent = new Intent(getContext(), ChatActivity.class);

        intent.putExtra("chatroomId", chatroomId);

        startActivity(intent);

    }

    @Override
    public void onStart() {
        super.onStart();
        if(chatroomAdapter!= null)
            chatroomAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(chatroomAdapter != null)
            chatroomAdapter.stopListening();
    }

}

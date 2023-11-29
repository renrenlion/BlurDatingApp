package com.example.blurdatingapplication.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.blurdatingapplication.utils.FireBaseUtil;


import com.example.blurdatingapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class ChatAdapter extends FirestoreRecyclerAdapter<ChatModel, ChatAdapter.ChatModelViewHolder> {
    private Context context;
    public ChatAdapter(@NonNull FirestoreRecyclerOptions<ChatModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ChatAdapter","onCreateViewHolder called");
        View view = LayoutInflater.from(context).inflate(R.layout.chat_messages, parent, false);
        return new ChatModelViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatModel model) {
        // Set the data to views based on the model
        holder.bind(model);
    }

    public void updateLastMessage(String chatroomId, String message) {
    }

    static class ChatModelViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftMessageLayout, rightMessageLayout;
        TextView leftMessageTextView, rightMessageTextView;

        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("ChatAdapter", "ChatModelViewHolder created");
            leftMessageTextView = itemView.findViewById(R.id.leftMessageTextView);
            rightMessageTextView = itemView.findViewById(R.id.rightMessageTextView);
            leftMessageLayout = itemView.findViewById(R.id.leftMessageLayout);
            rightMessageLayout = itemView.findViewById(R.id.rightMessageLayout);
        }

        public void bind(ChatModel model) {
            // Set the data to views based on the model
            if (model.getSenderId().equals(FireBaseUtil.getUserID())) {
                Log.d("ChatAdapter", "Setting receiver view");
                rightMessageLayout.setVisibility(View.VISIBLE);
                leftMessageLayout.setVisibility(View.GONE);
                rightMessageTextView.setText(model.getMessage());
            } else {
                Log.d("ChatAdapter", "Setting sender view");
                leftMessageLayout.setVisibility(View.VISIBLE);
                rightMessageLayout.setVisibility(View.GONE);
                leftMessageTextView.setText(model.getMessage());
            }
        }
    }
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        Log.d("ChatAdapter", "Data changed. Item count: " + getItemCount());
    }
}


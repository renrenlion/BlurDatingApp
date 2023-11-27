package com.example.blurdatingapplication.automaticmatching;

import static com.example.blurdatingapplication.utils.FireBaseUtil.otherUserInterest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blurdatingapplication.R;
import com.example.blurdatingapplication.data.Interest;
import com.example.blurdatingapplication.data.UserData;
import com.example.blurdatingapplication.utils.FireBaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AutomaticMatchingFragment extends Fragment {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference usersCollection = firestore.collection("users");

    UserData currentUserData;

   public AutomaticMatchingFragment(){
       //empty
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_automatic_matching, container, false);

        return view;
    }
}




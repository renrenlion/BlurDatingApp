package com.example.blurdatingapplication.manualmatching;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blurdatingapplication.R;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blurdatingapplication.R;

import com.example.blurdatingapplication.chat.ChatroomModel;
import com.example.blurdatingapplication.data.CheckedUser;
import com.example.blurdatingapplication.data.UserData;
import com.example.blurdatingapplication.data.WaitUser;
import com.example.blurdatingapplication.registration.SetUpUserInfo;
import com.example.blurdatingapplication.utils.FireBaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;

import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LikesYouFragment extends Fragment implements CardStackListener {

    WaitUser currntUserWaitUserList;

    private DrawerLayout drawerLayout;
    private CardStackView cardStackView;
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;

    Uri uri;

    FirebaseAuth auth;
    FirebaseUser currentUser;

    CheckedUser currentUserCheckedList;

    WaitUser currentUserWaitList;

    String chatroomId;



    public LikesYouFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_likes_you, container, false);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        setupCardStackView(view);
        setupButton(view);

        createSpots(new SpotsCallback() {
            @Override
            public void onSpotsReady(List<Spot> spots) {
                if (isAdded()) {

                    adapter = new CardStackAdapter(spots);
                    cardStackView.setAdapter(adapter);

                    RecyclerView.ItemAnimator itemAnimator = cardStackView.getItemAnimator();
                    if (itemAnimator instanceof DefaultItemAnimator) {
                        ((DefaultItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {
        Log.d("CardStackView", "onCardDragging: d = " + direction.name() + ", r = " + ratio);
    }

    @Override
    public void onCardSwiped(Direction direction) {
        Log.d("CardStackView", "onCardSwiped: p = " + manager.getTopPosition() + ", d = " + direction);
        if (manager.getTopPosition() == adapter.getItemCount() - 5) {
            createSpots(new SpotsCallback() {
                @Override
                public void onSpotsReady(List<Spot> spots) {
                    paginate(spots);
                }
            });

        }

        if(direction == Direction.Left || direction == Direction.Right){
            List<Spot> spots = adapter.getSpots();
            int topPosition = manager.getTopPosition();

            if (topPosition >= 0 && topPosition < spots.size()) {
                String swipedUserId = spots.get(topPosition).getUserId();
                removeFromWaitList(swipedUserId);

            } else {

                // Handle the case where topPosition is out of bounds
                Log.e("onCardSwiped", "Top position is out of bounds");
            }

        }

        if (direction == Direction.Right) {
            List<Spot> spots = adapter.getSpots();
            int topPosition = manager.getTopPosition();
            Toast.makeText(requireContext(), "Matching", Toast.LENGTH_LONG).show();

            if (topPosition >= 0 && topPosition < spots.size()) {
                String swipedUserId = spots.get(topPosition).getUserId();
                chatroomId = FireBaseUtil.getChatroomId(FireBaseUtil.currentUserId(),swipedUserId);
                getOrCreateChatRoomModel(swipedUserId);

            } else {
                // Handle the case where topPosition is out of bounds
                Log.e("onCardSwiped", "Top position is out of bounds");
            }
        }

    }


    @Override
    public void onCardRewound() {
        Log.d("CardStackView", "onCardRewound: " + manager.getTopPosition());
    }

    @Override
    public void onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: " + manager.getTopPosition());
    }

    @Override
    public void onCardAppeared(View view, int position) {
        TextView textView = view.findViewById(R.id.item_username);
        Log.d("CardStackView", "onCardAppeared: (" + position + ") " + textView.getText());
    }

    @Override
    public void onCardDisappeared(View view, int position) {
        TextView textView = view.findViewById(R.id.item_username);
        Log.d("CardStackView", "onCardDisappeared: (" + position + ") " + textView.getText());
    }

    private void setupCardStackView(View view) {
        initialize(view);
    }

    private void setupButton(View view) {
        View skip = view.findViewById(R.id.btn_skip);
        skip.setOnClickListener(v -> {
            SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Left)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new AccelerateInterpolator())
                    .build();
            manager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
        });

        View rewind = view.findViewById(R.id.btn_profile);
        rewind.setOnClickListener(v -> {
            RewindAnimationSetting setting = new RewindAnimationSetting.Builder()
                    .setDirection(Direction.Bottom)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new DecelerateInterpolator())
                    .build();
            manager.setRewindAnimationSetting(setting);
            cardStackView.rewind();
        });

        View like = view.findViewById(R.id.btn_like);
        like.setOnClickListener(v -> {
            SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Right)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new AccelerateInterpolator())
                    .build();
            manager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
        });
    }

    private void initialize(View view) {
        manager = new CardStackLayoutManager(requireContext(), this);
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());

        cardStackView = view.findViewById(R.id.card_stack_view);
        cardStackView.setLayoutManager(manager);

        // Use createSpots with a callback to handle the asynchronous result
        createSpots(new SpotsCallback() {
            @Override
            public void onSpotsReady(List<Spot> spots) {
                if (isAdded()) {
                    adapter = new CardStackAdapter(spots);
                    cardStackView.setAdapter(adapter);

                    RecyclerView.ItemAnimator itemAnimator = cardStackView.getItemAnimator();
                    if (itemAnimator instanceof DefaultItemAnimator) {
                        ((DefaultItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
                    }
                }
            }
        });
    }

    private void paginate(List<Spot> spots) {
        if (adapter != null) {
            List<Spot> oldSpots = adapter.getSpots();
            List<Spot> newSpots = new ArrayList<>(oldSpots);
            newSpots.addAll(spots);
            SpotDiffCallback callback = new SpotDiffCallback(oldSpots, newSpots);
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
            adapter.setSpots(newSpots);
            result.dispatchUpdatesTo(adapter);
        }
    }

    private void createSpots(SpotsCallback callback) {
        List<Spot> spots = new ArrayList<>();

        FireBaseUtil.currentUserWaitList().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot userDocument = task.getResult();
                if (userDocument.exists()) {
                    currentUserWaitList = userDocument.toObject(WaitUser.class);
                    List<String> userIds = currentUserWaitList.getUserIds();
                    for (String otherUid : userIds) {
                        if (!"testID".equals(otherUid)) {
                            FireBaseUtil.otherUserData(otherUid).get().addOnCompleteListener(atask -> {
                                UserData otherUserData = atask.getResult().toObject(UserData.class);
                                String otherUserId = otherUserData.getUid();
                                FireBaseUtil.getOtherFacePicStorageReference(otherUserId).getDownloadUrl()
                                        .addOnCompleteListener(uriTask -> {
                                            if (uriTask.isSuccessful()) {
                                                uri = uriTask.getResult();

                                                spots.add(new Spot(otherUserId, otherUserData.getUsername(), String.valueOf(otherUserData.getAge()), String.valueOf(otherUserData.getLocation()), uri.toString()));

                                                callback.onSpotsReady(spots);
                                            } else {
                                                Log.e("createSpots", "Failed to get image URL");
                                                callback.onSpotsReady(Collections.emptyList());
                                            }
                                        });
                            });
                        }
                    }
                }
            }
        });
    }
    interface SpotsCallback {
        void onSpotsReady(List<Spot> spots);
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
    void getOrCreateChatRoomModel(String otherUserId){
        List<String> userIds = Arrays.asList(FireBaseUtil.currentUserId(), otherUserId);
        FireBaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                ChatroomModel chatroomModel = new ChatroomModel();
                chatroomModel = new ChatroomModel(null, "", "", userIds, chatroomId);
                FireBaseUtil.getChatroomReference(chatroomId).set(chatroomModel);

            }

        });
    }

}


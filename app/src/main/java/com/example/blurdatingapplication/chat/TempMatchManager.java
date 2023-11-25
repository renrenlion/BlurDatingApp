package com.example.blurdatingapplication.chat;

// MatchManager.java
import java.util.ArrayList;
import java.util.List;

public class TempMatchManager {

    // Simulate finding matches based on user ID
    public static List<String> findMatches(String userId) {
        List<String> matchedUserIds = new ArrayList<>();

        // For demonstration purposes, let's add some sample matched users
        if (userId.equals("user1")) {
            matchedUserIds.add("user2");
            matchedUserIds.add("user3");
        } else if (userId.equals("user2")) {
            matchedUserIds.add("user1");
            matchedUserIds.add("user3");
        } else if (userId.equals("user3")) {
            matchedUserIds.add("user1");
            matchedUserIds.add("user2");
        }

        return matchedUserIds;
    }
}

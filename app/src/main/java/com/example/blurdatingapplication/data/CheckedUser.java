package com.example.blurdatingapplication.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CheckedUser {
    private List<String> userIds;

    public CheckedUser() {
        this.userIds = new ArrayList<>();
        addToUserIds("testID");
    }



    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public void addToUserIds(String userId) {
        userIds.add(userId);
    }

    public void removeUserId(String userId) {
        Iterator<String> iterator = userIds.iterator();
        while (iterator.hasNext()) {
            String currentUserId = iterator.next();
            if (currentUserId.equals(userId)) {
                iterator.remove();
                break;
            }
        }
    }
    public boolean containsUserId(String userId) {
        return userIds.contains(userId);
    }
}



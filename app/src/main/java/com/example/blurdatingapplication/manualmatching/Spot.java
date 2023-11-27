package com.example.blurdatingapplication.manualmatching;

public class Spot {
    private static long counter = 0;

    private long number;

    private String userId;
    private String username;
    private String age;

    private String location;
    private String url;

    public Spot(String userId, String username, String age, String location, String url) {
        this.number = counter++;
        this.userId = userId;
        this.username = username;
        this.age = age;
        this.location = location;
        this.url = url;
    }

    public long getNumber() {
        return number;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId(){return userId;}

    public String getAge() {
        return age;
    }

    public String getLocation(){return location;}

    public String getUrl() {
        return url;
    }
}


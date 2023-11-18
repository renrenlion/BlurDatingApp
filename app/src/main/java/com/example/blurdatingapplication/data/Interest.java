package com.example.blurdatingapplication.data;

public class Interest {
    private String sport;
    private String music;
    private String gaming;
    private String food;
    private String traveling;
    private String activity;
    private String reading;

    public Interest(){

    }

    public Interest(String sport, String music,
                    String gaming, String food,
                    String traveling, String activity,
                    String reading) {
        this.sport = sport;
        this.music = music;
        this.gaming = gaming;
        this.food = food;
        this.traveling = traveling;
        this.activity = activity;
        this.reading = reading;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getGaming() {
        return gaming;
    }

    public void setGaming(String gaming) {
        this.gaming = gaming;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getTraveling() {
        return traveling;
    }

    public void setTraveling(String traveling) {
        this.traveling = traveling;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }
}

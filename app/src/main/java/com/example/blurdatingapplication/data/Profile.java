package com.example.blurdatingapplication.data;

public class Profile {
    private String job;
    private String bloodType;
    private String child;
    private String drinking;
    private String smoking;
    private String work_out;
    private String day_off;

    public Profile(){}

    public Profile(String job, String bloodType, String child, String drinking, String smoking, String work_out, String day_off) {
        this.job = job;
        this.bloodType = bloodType;
        this.child = child;
        this.drinking = drinking;
        this.smoking = smoking;
        this.work_out = work_out;
        this.day_off = day_off;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getDrinking() {
        return drinking;
    }

    public void setDrinking(String drinking) {
        this.drinking = drinking;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getWork_out() {
        return work_out;
    }

    public void setWork_out(String work_out) {
        this.work_out = work_out;
    }

    public String getDay_off() {
        return day_off;
    }

    public void setDay_off(String day_off) {
        this.day_off = day_off;
    }
}

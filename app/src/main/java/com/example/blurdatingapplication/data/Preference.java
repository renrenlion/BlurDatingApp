package com.example.blurdatingapplication.data;

public class Preference {
    private String height;
    private String weight;
    private String hairColor;
    private String eyeColor;
    private String bodyType;
    private String facialType;

    Preference(){

    }

    public Preference(String height, String weight, String hairColor, String eyeColor, String bodyType, String facialType) {
        this.height = height;
        this.weight = weight;
        this.hairColor = hairColor;
        this.eyeColor = eyeColor;
        this.bodyType = bodyType;
        this.facialType = facialType;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getFacialType() {
        return facialType;
    }

    public void setFacialType(String facialType) {
        this.facialType = facialType;
    }
}


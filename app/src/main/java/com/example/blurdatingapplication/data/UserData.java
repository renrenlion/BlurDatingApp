package com.example.blurdatingapplication.data;

import com.example.blurdatingapplication.utils.FunctionUtil;
import com.google.firebase.Timestamp;

public class UserData {
    private String email;
    private String uid;
    private String username;
    private String phoneNumber;
    private Timestamp createdTimestamp;

    private String birthday;
    private int age;

    private int location;
    private int gender;
    private int preferredGender;
    private String creditcardInfo;
    private String bankingInfo;
    private String billingInfo;

    private int plan;

    public UserData() {

    }

    public UserData(String email, String uid, String username, String phoneNumber,
                    Timestamp createdTimestamp, String birthday, int location,
                    int gender, int preferredGender, String creditcardInfo,
                    String bankingInfo, String billingInfo, int plan) {

        this.email = email;
        this.uid = uid;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.createdTimestamp = createdTimestamp;
        this.birthday = birthday;
        this.location = location;
        this.gender = gender;
        this.preferredGender = preferredGender;
        this.creditcardInfo = creditcardInfo;
        this.bankingInfo = bankingInfo;
        this.billingInfo = billingInfo;
        this.plan = plan;
        age = FunctionUtil.calculateAge(birthday);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
        this.age = FunctionUtil.calculateAge(birthday);
    }

    public int getAge() {
        return age;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getPreferredGender() {
        return preferredGender;
    }

    public void setPreferredGender(int preferredGender) {
        this.preferredGender = preferredGender;
    }

    public String getCreditcardInfo() {
        return creditcardInfo;
    }

    public void setCreditcardInfo(String creditcardInfo) {
        this.creditcardInfo = creditcardInfo;
    }

    public String getBankingInfo() {
        return bankingInfo;
    }

    public void setBankingInfo(String bankingInfo) {
        this.bankingInfo = bankingInfo;
    }

    public String getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(String billingInfo) {
        this.billingInfo = billingInfo;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }
}

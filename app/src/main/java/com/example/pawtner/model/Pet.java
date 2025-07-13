package com.example.pawtner.model;

public class Pet {
    private String name;
    private String details;
    private String type; // "Cat", "Dog", etc
    private int profileImageRes;
    private int backgroundImageRes;

    // Constructor
    public Pet(String name, String details, String type, int profileImageRes, int backgroundImageRes) {
        this.name = name;
        this.details = details;
        this.type = type;
        this.profileImageRes = profileImageRes;
        this.backgroundImageRes = backgroundImageRes;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getType() {
        return type;
    }

    public int getProfileImageRes() {
        return profileImageRes;
    }

    public int getBackgroundImageRes() {
        return backgroundImageRes;
    }

    // Setters (optional)
    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setProfileImageRes(int profileImageRes) {
        this.profileImageRes = profileImageRes;
    }

    public void setBackgroundImageRes(int backgroundImageRes) {
        this.backgroundImageRes = backgroundImageRes;
    }
}

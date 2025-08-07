package com.example.pawtner.model;

public class Event {

    private int imageRes;
    private String title;
    private String date;

    public Event(int imageRes, String title, String date) {
        this.imageRes = imageRes;
        this.title = title;
        this.date = date;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}

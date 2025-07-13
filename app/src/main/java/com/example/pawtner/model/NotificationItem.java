package com.example.pawtner.model;

public class NotificationItem {
    private String title;
    private String message;
    private String date;
    private String time;
    private boolean isRead;

    public NotificationItem(String title, String message, String date, String time, boolean isRead) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.time = time;
        this.isRead = isRead;
    }

    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public boolean isRead() { return isRead; }

    public void setRead(boolean read) { isRead = read; }
}
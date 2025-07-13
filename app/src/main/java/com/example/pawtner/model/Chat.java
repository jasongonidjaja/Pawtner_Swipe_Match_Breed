package com.example.pawtner.model;

public class Chat {
    private String username;
    private String lastMessage;
    private String time;
    private int unreadCount;

    public Chat(String username, String lastMessage, String time, int unreadCount) {
        this.username = username;
        this.lastMessage = lastMessage;
        this.time = time;
        this.unreadCount = unreadCount;
    }

    public String getUsername() {
        return username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTime() {
        return time;
    }

    public int getUnreadCount() {
        return unreadCount;
    }
}

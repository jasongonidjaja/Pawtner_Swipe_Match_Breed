package com.example.pawtner.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pawtner.model.NotificationItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<List<NotificationItem>> notifications;

    public NotificationsViewModel() {
        notifications = new MutableLiveData<>();
        loadDummyData();
    }

    private void loadDummyData() {
        List<NotificationItem> dummyList = new ArrayList<>();
        dummyList.add(new NotificationItem("Welcome to Pawtner!",
                "You're officially part of the cutest dating community for pets and their humans...",
                "25/5/2025", "10:30", false));
        dummyList.add(new NotificationItem("It’s a match! 🎉",
                "You and Maritza both swiped right on each other's pets! ...",
                "15/2/2025", "09:15", false));
        dummyList.add(new NotificationItem("New Match Found 🐶",
                "Bruno just matched with Daisy! They seem to share similar energy...",
                "26/5/2025", "11:00", false));
        dummyList.add(new NotificationItem("Vaccination Reminder 💉",
                "Hey there, responsible pet parent! It's time to update Coco's vaccination record...",
                "27/5/2025", "14:20", false));
        dummyList.add(new NotificationItem("Event Reminder 🎉",
                "The Cat Café Meet-Up is happening this afternoon! Don’t miss it...",
                "28/5/2025", "08:00", false));
        dummyList.add(new NotificationItem("Playdate Confirmed 🐾",
                "You have a confirmed playdate with Milo today at 4 PM at Green Paw Park...",
                "28/5/2025", "09:45", false));
        dummyList.add(new NotificationItem("Profile Viewed 👀",
                "Good news! Someone just viewed your pet’s profile...",
                "29/5/2025", "12:10", false));

        notifications.setValue(dummyList);
    }

    public LiveData<List<NotificationItem>> getNotifications() {
        return notifications;
    }

    public void markAsRead(int position) {
        if (notifications.getValue() == null) return;
        List<NotificationItem> current = new ArrayList<>(notifications.getValue());
        NotificationItem item = current.get(position);
        item.setRead(true);
        notifications.setValue(current); // Trigger update
    }
}

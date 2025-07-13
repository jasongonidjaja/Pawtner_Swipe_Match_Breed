package com.example.pawtner.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawtner.R;
import com.example.pawtner.model.NotificationItem;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationItem> notificationList;
    private OnNotificationClickListener listener;

    // Constructor dengan listener
    public NotificationAdapter(List<NotificationItem> notificationList, OnNotificationClickListener listener) {
        this.notificationList = notificationList;
        this.listener = listener;
    }

    // Interface callback untuk dikirim ke Fragment
    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationItem item, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, message, date, time;
        CardView cardView;
        View container; // Tambah ini

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.notification_title);
            message = view.findViewById(R.id.notification_message);
            date = view.findViewById(R.id.notification_date);
            time = view.findViewById(R.id.notification_time);
            cardView = view.findViewById(R.id.card_notification);
            container = view.findViewById(R.id.layout_container); // Tambah ini
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notificationsitem, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationItem notif = notificationList.get(position);

//        Log.d("NotificationAdapter", "onBindViewHolder pos " + position + " | isRead: " + notif.isRead());

        holder.title.setText(notif.getTitle());
        holder.message.setText(notif.getMessage());
        holder.date.setText(notif.getDate());
        holder.time.setText(notif.getTime());

        Log.d("NotifAdapter", "Position " + position + " isRead: " + notif.isRead());


        // Ganti warna background berdasarkan status sudah dibaca atau belum
        int backgroundColor = notif.isRead() ? Color.parseColor("#FFF9ED") : Color.parseColor("#EEF3FE");
        holder.container.setBackgroundColor(backgroundColor);



        // Kirim callback ke Fragment saat diklik
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNotificationClick(notif, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}

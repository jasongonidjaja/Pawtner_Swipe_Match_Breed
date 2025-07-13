package com.example.pawtner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pawtner.R;

import java.util.List;

import com.example.pawtner.model.Chat;



public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Chat> chatList;

    public ChatAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView name, message, time, unread;
        ImageView profile;

        public ChatViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.chat_name);
            message = itemView.findViewById(R.id.chat_message);
            time = itemView.findViewById(R.id.chat_time);
            unread = itemView.findViewById(R.id.chat_unread_count);
            profile = itemView.findViewById(R.id.chat_profile_image);
        }
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.name.setText(chat.getName());
        holder.message.setText(chat.getMessage());
        holder.time.setText(chat.getTime());

        if (chat.getUnreadCount() > 0) {
            holder.unread.setText(String.valueOf(chat.getUnreadCount()));
            holder.unread.setVisibility(View.VISIBLE);
        } else {
            holder.unread.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}

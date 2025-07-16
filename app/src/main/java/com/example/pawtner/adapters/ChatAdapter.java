package com.example.pawtner.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawtner.ChatDetailActivity;
import com.example.pawtner.R;
import com.example.pawtner.model.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Chat> chatList;

    public ChatAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.username.setText(chat.getUsername());
        holder.message.setText(chat.getLastMessage());
        holder.time.setText(chat.getTime());

        // Kirim ke ChatDetailActivity saat diklik
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ChatDetailActivity.class);
            intent.putExtra("username", chat.getUsername());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, message, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.chat_name);
            message = itemView.findViewById(R.id.chat_message);
            time = itemView.findViewById(R.id.chat_time);

        }
    }
}

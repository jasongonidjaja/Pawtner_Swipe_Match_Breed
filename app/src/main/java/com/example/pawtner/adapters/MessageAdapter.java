package com.example.pawtner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pawtner.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<String> messageList;

    public MessageAdapter(List<String> messageList) {
        this.messageList = messageList;
    }

    public void addMessage(String message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String message = messageList.get(position);
        if (holder instanceof SentMessageHolder) {
            ((SentMessageHolder) holder).messageText.setText(message);
        } else {
            ((ReceivedMessageHolder) holder).messageText.setText(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
        }
    }

    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
        }
    }
}

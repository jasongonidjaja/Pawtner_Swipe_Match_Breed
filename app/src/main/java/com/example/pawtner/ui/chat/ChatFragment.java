package com.example.pawtner.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawtner.R; // <--- PENTING!
import com.example.pawtner.adapters.ChatAdapter;
import com.example.pawtner.model.Chat;



import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {


    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<Chat> chatList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        chatList = new ArrayList<>();
        chatList.add(new Chat("Darianto", "Halo anjingmu makan apa?", "11:20 AM", 2));
        chatList.add(new Chat("Lina", "Sampai ketemu nanti ya!", "10:45 AM", 0));
        chatList.add(new Chat("Ayu", "Sip sip 👍", "9:10 AM", 1));

        adapter = new ChatAdapter(chatList);
        recyclerView.setAdapter(adapter);



        return view;
    }
}
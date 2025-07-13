package com.example.pawtner;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawtner.adapters.MessageAdapter;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private EditText messageInput;
    private ImageButton sendButton;

    private List<String> messageList = new ArrayList<>(Arrays.asList(
            "Halo!", "Apa kabar?", "Saya baik, terima kasih.", "Lanjut nanti ya."));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        // Toolbar setup
        MaterialToolbar toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Darianto Anjisari");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // UI Binding
        recyclerView = findViewById(R.id.chat_recycler_view);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);

        adapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        sendButton.setOnClickListener(v -> {
            String msg = messageInput.getText().toString().trim();
            if (!msg.isEmpty()) {
                adapter.addMessage(msg);
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                messageInput.setText("");
            }
        });
    }

    // ✅ Tampilkan menu di toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_toolbar_menu, menu);
        return true;
    }

    // ✅ Handle klik menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.action_video_call) {
            Toast.makeText(this, "Video Call diklik", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_audio_call) {
            Toast.makeText(this, "Audio Call diklik", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_more) {
            Toast.makeText(this, "Menu Lainnya diklik", Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}

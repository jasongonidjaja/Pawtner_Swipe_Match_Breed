package com.example.pawtner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawtner.adapters.MessageAdapter;
import com.example.pawtner.model.Message;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private EditText messageInput;
    private ImageButton sendButton, cameraButton, attachButton;
    private LinearLayout attachmentOptions;
//    private ImageButton attachButton;

    private final List<Message> messageList = new ArrayList<>();

    private final ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Izin kamera ditolak", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        // Setup toolbar
        MaterialToolbar toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Darianto Anjisari");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Bind views
        recyclerView = findViewById(R.id.chat_recycler_view);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        cameraButton = findViewById(R.id.camera_button);
        attachButton = findViewById(R.id.attach_button);

        // Dummy messages
        messageList.add(new Message("Halo!", "08:00"));
        messageList.add(new Message("Apa kabar?", "08:01"));
        messageList.add(new Message("Saya baik, terima kasih.", "08:02"));
        messageList.add(new Message("Lanjut nanti ya.", "08:03"));

        // Setup RecyclerView
        adapter = new MessageAdapter(messageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Show from bottom
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Kirim pesan
        sendButton.setOnClickListener(v -> {
            String msg = messageInput.getText().toString().trim();
            if (!msg.isEmpty()) {
                String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                adapter.addMessage(new Message(msg, time));
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                messageInput.setText("");
            }
        });

        // Kamera
        cameraButton.setOnClickListener(v -> checkCameraPermission());

        // Lampiran (belum diimplementasi)
        attachButton = findViewById(R.id.attach_button);
        attachmentOptions = findViewById(R.id.attachment_options);

        // Tampilkan/sembunyikan menu attachment
        attachButton.setOnClickListener(v -> {
            if (attachmentOptions.getVisibility() == View.GONE) {
                attachmentOptions.setVisibility(View.VISIBLE);
            } else {
                attachmentOptions.setVisibility(View.GONE);
            }
        });

        // Aksi klik setiap menu
        findViewById(R.id.gallery_layout).setOnClickListener(v -> {
            Toast.makeText(this, "Gallery clicked", Toast.LENGTH_SHORT).show();
            attachmentOptions.setVisibility(View.GONE);
        });

        findViewById(R.id.camera_layout).setOnClickListener(v -> {
            Toast.makeText(this, "Camera clicked", Toast.LENGTH_SHORT).show();
            attachmentOptions.setVisibility(View.GONE);
        });

        findViewById(R.id.location_layout).setOnClickListener(v -> {
            Toast.makeText(this, "Location clicked", Toast.LENGTH_SHORT).show();
            attachmentOptions.setVisibility(View.GONE);
        });

        findViewById(R.id.document_layout).setOnClickListener(v -> {
            Toast.makeText(this, "Document clicked", Toast.LENGTH_SHORT).show();
            attachmentOptions.setVisibility(View.GONE);
        });
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Tidak ada aplikasi kamera", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_video_call) {
            Toast.makeText(this, "Video Call diklik", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_more) {
            Toast.makeText(this, "Menu lainnya diklik", Toast.LENGTH_SHORT).show();
            return true;
        }else if (id == R.id.action_audio_call) {
            Intent intent = new Intent(this, VoiceCallActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

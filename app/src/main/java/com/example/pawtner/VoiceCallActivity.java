package com.example.pawtner;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VoiceCallActivity extends AppCompatActivity {

    private ImageButton endCallButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);

        endCallButton = findViewById(R.id.end_call_button);

        endCallButton.setOnClickListener(v -> {
            Toast.makeText(this, "Mengakhiri panggilan...", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}

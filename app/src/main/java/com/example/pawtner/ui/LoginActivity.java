package com.example.pawtner.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pawtner.MainActivity;
import com.example.pawtner.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.login);

        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button signInButton = findViewById(R.id.signInButton);
        TextView errorMessage = findViewById(R.id.errorMessage);
        TextView signUpText = findViewById(R.id.signupText); // ✅ Cukup satu kali deklarasi

        // ✅ Ubah warna dan underline pada "Sign up"
        String fullText = "Don’t have an account? Sign up";
        SpannableString spannable = new SpannableString(fullText);
        int start = fullText.indexOf("Sign up");
        int end = start + "Sign up".length();
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#5D9BFF")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpText.setText(spannable);

        // ✅ Validasi Sign In
        signInButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                errorMessage.setText("Please enter your email and password.");
                errorMessage.setVisibility(TextView.VISIBLE);
            } else if (!email.contains("@")) {
                errorMessage.setText("Email must contain '@'.");
                errorMessage.setVisibility(TextView.VISIBLE);
            } else {
                errorMessage.setVisibility(TextView.GONE);

                    // ✅ Tambahkan data dummy untuk ProfileActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("name", "Maritza Eka Rahmadhani");
                    intent.putExtra("gender", "Female");
                    intent.putExtra("phone", "081234567890");
                    intent.putExtra("email", email); // ambil dari input login
                    intent.putExtra("address", "JL. Bibis Karah A66");
                    intent.putExtra("nik", "3578216022030003");
                    startActivity(intent);
                    finish(); // opsional: supaya tidak bisa kembali ke login
                }
            });

        // ✅ Arahkan ke Register
        signUpText.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

    }
}

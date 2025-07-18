package com.example.pawtner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pawtner.MainActivity;
import com.example.pawtner.R;
import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.register);

        // 👉 Teks "Already have an account? Sign in"
        TextView signInText = findViewById(R.id.signinText);
        String baseText = "Already have an account? Sign in";
        SpannableString spannable = new SpannableString(baseText);
        int start = baseText.indexOf("Sign in");
        int end = start + "Sign in".length();
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#5D9BFF")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signInText.setText(spannable);

        signInText.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

        // ✅ Validasi untuk tombol Sign Up
        Button signUpButton = findViewById(R.id.signupButton);
        EditText fullNameInput = findViewById(R.id.fullNameInput);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        EditText phoneInput = findViewById(R.id.phoneInput);
        RadioGroup genderGroup = findViewById(R.id.genderGroup);
        EditText addressInput = findViewById(R.id.addressInput);
        EditText nikInput = findViewById(R.id.nikInput);
        TextView errorMessage = findViewById(R.id.errorMessage);

        signUpButton.setOnClickListener(v -> {
            String fullName = fullNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            int selectedGenderId = genderGroup.getCheckedRadioButtonId();
            String address = addressInput.getText().toString().trim();
            String nik = nikInput.getText().toString().trim();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()
                    || phone.isEmpty() || selectedGenderId == -1
                    || address.isEmpty() || nik.isEmpty()) {
                errorMessage.setText("Please complete all fields.");
                errorMessage.setVisibility(TextView.VISIBLE);
            } else if (!fullName.matches("[a-zA-Z\\s]+")) {
                errorMessage.setText("Name must contain only letters.");
                errorMessage.setVisibility(TextView.VISIBLE);
            } else if (!email.contains("@")) {
                errorMessage.setText("Email must contain '@'.");
                errorMessage.setVisibility(TextView.VISIBLE);
            } else if (phone.length() < 11 || phone.length() > 12) {
                errorMessage.setText("Phone must be 11–12 digits.");
                errorMessage.setVisibility(TextView.VISIBLE);
            } else if (!nik.matches("\\d{16,}")) {
                errorMessage.setText("NIK must be at least 16 digits and numeric.");
                errorMessage.setVisibility(TextView.VISIBLE);
            } else {
                errorMessage.setVisibility(TextView.GONE);

                // 🔽 Ambil gender string dari RadioButton
                RadioButton selectedGenderRadio = findViewById(selectedGenderId);
                String gender = selectedGenderRadio.getText().toString();

                // 🔁 Kirim data ke ProfileActivity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.putExtra("name", fullName);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("address", address);
                intent.putExtra("nik", nik);
                intent.putExtra("gender", gender);
                startActivity(intent);
            }
        });

        // 👉 Tombol Login via Google / Facebook (hanya redirect)
        MaterialButton googleButton = findViewById(R.id.googleButton);
        MaterialButton facebookButton = findViewById(R.id.facebookButton);

        googleButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        facebookButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }
}

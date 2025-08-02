package com.example.pawtner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;


public class ChatDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private EditText messageInput;
    private ImageButton sendButton, cameraButton, attachButton;
    private LinearLayout attachmentOptions;
    private LinearLayout chatInputLayout;
    private LinearLayout imagePreviewLayout;
    private ImageView selectedImagePreview;
    private ImageButton closeImagePreviewButton;
    private EditText imageCaptionInput;
    private Button sendImageButton;

    // Views for the custom in-app gallery
    private LinearLayout customGalleryLayout;
    private ImageButton closeCustomGalleryButton;
    // Deklarasi ImageView statis (sesuaikan jika Anda menambah/mengurangi jumlah gambar)
    private ImageView galleryImage1, galleryImage2, galleryImage3;
    private ImageView galleryImage4, galleryImage5, galleryImage6; // Jika Anda menambahkan baris kedua
    private ImageView galleryImage7, galleryImage8, galleryImage9; // Jika Anda menambahkan baris ketiga

    private Uri selectedImageUri;

    private final List<Message> messageList = new ArrayList<>(); // PASTIKAN INI TIDAK DIHAPUS

    private final ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Izin kamera ditolak", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String> requestGalleryPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    showCustomGalleryUI();
                } else {
                    Toast.makeText(this, "Izin akses galeri ditolak", Toast.LENGTH_SHORT).show();
                    chatInputLayout.setVisibility(View.VISIBLE);
                    attachmentOptions.setVisibility(View.GONE);
                    imagePreviewLayout.setVisibility(View.GONE);
                    customGalleryLayout.setVisibility(View.GONE);
                }
            });

    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        selectedImagePreview.setImageURI(selectedImageUri);
                        attachmentOptions.setVisibility(View.GONE);
                        chatInputLayout.setVisibility(View.GONE);
                        customGalleryLayout.setVisibility(View.GONE);
                        imagePreviewLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    customGalleryLayout.setVisibility(View.VISIBLE);
                    chatInputLayout.setVisibility(View.GONE);
                    attachmentOptions.setVisibility(View.GONE);
                    imagePreviewLayout.setVisibility(View.GONE);
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

        // Bind existing views
        recyclerView = findViewById(R.id.chat_recycler_view);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        cameraButton = findViewById(R.id.camera_button);
        attachButton = findViewById(R.id.attach_button);
        attachmentOptions = findViewById(R.id.attachment_options);
        chatInputLayout = findViewById(R.id.chat_input_layout);

        // Bind views for image preview
        imagePreviewLayout = findViewById(R.id.image_preview_layout);
        selectedImagePreview = findViewById(R.id.selected_image_preview);
        closeImagePreviewButton = findViewById(R.id.close_image_preview_button);
        imageCaptionInput = findViewById(R.id.image_caption_input);
        sendImageButton = findViewById(R.id.send_image_button);

        // Bind views for the custom in-app gallery (ImageView statis)
        customGalleryLayout = findViewById(R.id.custom_gallery_layout);
        closeCustomGalleryButton = findViewById(R.id.close_custom_gallery_button);
        galleryImage1 = findViewById(R.id.gallery_image_1);
        galleryImage2 = findViewById(R.id.gallery_image_2);
        galleryImage3 = findViewById(R.id.gallery_image_3);
        // Bind ImageView tambahan jika Anda menambahkannya di XML
        galleryImage4 = findViewById(R.id.gallery_image_4); // Jika ada baris kedua
        galleryImage5 = findViewById(R.id.gallery_image_5);
        galleryImage6 = findViewById(R.id.gallery_image_6);
        galleryImage7 = findViewById(R.id.gallery_image_7); // Jika ada baris ketiga
        galleryImage8 = findViewById(R.id.gallery_image_8);
        galleryImage9 = findViewById(R.id.gallery_image_9);


        // Dummy messages - PASTIKAN INI TIDAK DIHAPUS
        messageList.add(new Message("Halo!", "08:00"));
        messageList.add(new Message("Apa kabar?", "08:01"));
        messageList.add(new Message("Saya baik, terima kasih.", "08:02"));
        messageList.add(new Message("Lanjut nanti ya.", "08:03"));

        // Setup RecyclerView for chat messages
        adapter = new MessageAdapter(messageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Set OnClickListener untuk setiap gambar di custom gallery (jika perlu)
        galleryImage1.setOnClickListener(v -> {
            Toast.makeText(this, "Gambar 1 dipilih", Toast.LENGTH_SHORT).show();
            // Implementasi untuk menampilkan gambar ini di preview atau mengirimnya
            // Contoh: selectedImageUri = /* Uri dari gambar 1 */;
            // showImagePreview(selectedImageUri);
        });
        galleryImage2.setOnClickListener(v -> {
            Toast.makeText(this, "Gambar 2 dipilih", Toast.LENGTH_SHORT).show();
        });
        galleryImage3.setOnClickListener(v -> {
            Toast.makeText(this, "Gambar 3 dipilih", Toast.LENGTH_SHORT).show();
        });
        // Tambahkan OnClickListener untuk gambar tambahan jika ada
        if (galleryImage4 != null) {
            galleryImage4.setOnClickListener(v -> Toast.makeText(this, "Gambar 4 dipilih", Toast.LENGTH_SHORT).show());
        }
        if (galleryImage5 != null) {
            galleryImage5.setOnClickListener(v -> Toast.makeText(this, "Gambar 5 dipilih", Toast.LENGTH_SHORT).show());
        }
        if (galleryImage6 != null) {
            galleryImage6.setOnClickListener(v -> Toast.makeText(this, "Gambar 6 dipilih", Toast.LENGTH_SHORT).show());
        }
        if (galleryImage7 != null) {
            galleryImage7.setOnClickListener(v -> Toast.makeText(this, "Gambar 7 dipilih", Toast.LENGTH_SHORT).show());
        }
        if (galleryImage8 != null) {
            galleryImage8.setOnClickListener(v -> Toast.makeText(this, "Gambar 8 dipilih", Toast.LENGTH_SHORT).show());
        }
        if (galleryImage9 != null) {
            galleryImage9.setOnClickListener(v -> Toast.makeText(this, "Gambar 9 dipilih", Toast.LENGTH_SHORT).show());
        }


        // Kirim pesan (standard text message)
        sendButton.setOnClickListener(v -> {
            String msg = messageInput.getText().toString().trim();
            if (!msg.isEmpty()) {
                String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                adapter.addMessage(new Message(msg, time));
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                messageInput.setText("");
            }
        });

        // Kamera button in main chat input
        cameraButton.setOnClickListener(v -> checkCameraPermission());

        // Attach button to toggle attachment options
        attachButton.setOnClickListener(v -> {
            // Pastikan semua overlay lain tersembunyi
            imagePreviewLayout.setVisibility(View.GONE);
            customGalleryLayout.setVisibility(View.GONE);

            if (attachmentOptions.getVisibility() == View.GONE) {
                attachmentOptions.setVisibility(View.VISIBLE);
                chatInputLayout.setVisibility(View.GONE); // Sembunyikan input utama
            } else {
                attachmentOptions.setVisibility(View.GONE);
                chatInputLayout.setVisibility(View.VISIBLE); // Tampilkan input utama
            }
        });

        // Attachment menu options
        findViewById(R.id.gallery_layout).setOnClickListener(v -> {
            checkGalleryPermissionAndOpenCustomGallery();
        });

        findViewById(R.id.camera_layout).setOnClickListener(v -> {
            Toast.makeText(this, "Camera attachment clicked (opens camera directly for new photo)", Toast.LENGTH_SHORT).show();
            attachmentOptions.setVisibility(View.GONE);
            chatInputLayout.setVisibility(View.VISIBLE);
            imagePreviewLayout.setVisibility(View.GONE);
            customGalleryLayout.setVisibility(View.GONE);
            checkCameraPermission();
        });

        findViewById(R.id.location_layout).setOnClickListener(v -> {
            Toast.makeText(this, "Location clicked", Toast.LENGTH_SHORT).show();
            attachmentOptions.setVisibility(View.GONE);
            chatInputLayout.setVisibility(View.VISIBLE);
            imagePreviewLayout.setVisibility(View.GONE);
            customGalleryLayout.setVisibility(View.GONE);
        });

        findViewById(R.id.document_layout).setOnClickListener(v -> {
            Toast.makeText(this, "Document clicked", Toast.LENGTH_SHORT).show();
            attachmentOptions.setVisibility(View.GONE);
            chatInputLayout.setVisibility(View.VISIBLE);
            imagePreviewLayout.setVisibility(View.GONE);
            customGalleryLayout.setVisibility(View.GONE);
        });

        // Close image preview button
        closeImagePreviewButton.setOnClickListener(v -> {
            imagePreviewLayout.setVisibility(View.GONE);
            chatInputLayout.setVisibility(View.VISIBLE);
            selectedImagePreview.setImageURI(null);
            imageCaptionInput.setText("");
            selectedImageUri = null;
        });

        // Send image button
        sendImageButton.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                String caption = imageCaptionInput.getText().toString().trim();
                String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                String messageText = "Image sent" + (caption.isEmpty() ? "" : ": " + caption);
                adapter.addMessage(new Message(messageText, time));
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);

                imagePreviewLayout.setVisibility(View.GONE);
                chatInputLayout.setVisibility(View.VISIBLE);
                selectedImagePreview.setImageURI(null);
                imageCaptionInput.setText("");
                selectedImageUri = null;
                Toast.makeText(this, "Image sent!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No image selected to send.", Toast.LENGTH_SHORT).show();
            }
        });

        // Close custom gallery button
        closeCustomGalleryButton.setOnClickListener(v -> {
            customGalleryLayout.setVisibility(View.GONE);
            chatInputLayout.setVisibility(View.VISIBLE); // Tampilkan kembali input chat utama
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

    private void checkGalleryPermissionAndOpenCustomGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                showCustomGalleryUI();
            } else {
                requestGalleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                showCustomGalleryUI();
            } else {
                requestGalleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void showCustomGalleryUI() {
        attachmentOptions.setVisibility(View.GONE);
        chatInputLayout.setVisibility(View.GONE);
        imagePreviewLayout.setVisibility(View.GONE);
        customGalleryLayout.setVisibility(View.VISIBLE);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Logika untuk tombol kembali di toolbar
        if (customGalleryLayout.getVisibility() == View.VISIBLE) {
            customGalleryLayout.setVisibility(View.GONE);
            chatInputLayout.setVisibility(View.VISIBLE);
            return true;
        }
        if (imagePreviewLayout.getVisibility() == View.VISIBLE) {
            imagePreviewLayout.setVisibility(View.GONE);
            chatInputLayout.setVisibility(View.VISIBLE);
            return true;
        }
        if (attachmentOptions.getVisibility() == View.VISIBLE) {
            attachmentOptions.setVisibility(View.GONE);
            chatInputLayout.setVisibility(View.VISIBLE);
            return true;
        }
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        // Logika untuk tombol kembali fisik (atau gesture back)
        if (customGalleryLayout.getVisibility() == View.VISIBLE) {
            customGalleryLayout.setVisibility(View.GONE);
            chatInputLayout.setVisibility(View.VISIBLE);
        } else if (imagePreviewLayout.getVisibility() == View.VISIBLE) {
            imagePreviewLayout.setVisibility(View.GONE);
            chatInputLayout.setVisibility(View.VISIBLE);
        } else if (attachmentOptions.getVisibility() == View.VISIBLE) {
            attachmentOptions.setVisibility(View.GONE);
            chatInputLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
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
            return onSupportNavigateUp();
        } else if (id == R.id.action_video_call) {
            Toast.makeText(this, "Video Call diklik", Toast.LENGTH_SHORT).show();
            return true;
        }else if (id == R.id.action_audio_call) {
            Intent intent = new Intent(this, VoiceCallActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_more) {
            View anchor = findViewById(R.id.chat_toolbar); // fallback anchor karena menu item tidak memiliki view
            showReportPopup(anchor);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    private void showReportPopup(View anchor) {
        View popupView = getLayoutInflater().inflate(R.layout.item_report, null);

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        TextView btnReport = popupView.findViewById(R.id.btn_report);
        btnReport.setOnClickListener(v -> {
            popupWindow.dismiss();
            showReportDialog(); // GANTI KE DIALOG LANGSUNG
        });

        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setElevation(8);

        popupWindow.showAtLocation(anchor, Gravity.TOP | Gravity.END, 50, 250);
    }


    private void showReportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_report_reason, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        EditText inputReason = view.findViewById(R.id.input_reason);
        Button btnSubmit = view.findViewById(R.id.btn_submit);
        ImageView btnClose = view.findViewById(R.id.btn_close);

        btnSubmit.setOnClickListener(v -> {
            String reason = inputReason.getText().toString().trim();
            if (reason.isEmpty()) {
                Toast.makeText(this, "Please provide a reason", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Report submitted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


}
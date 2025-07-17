package com.example.pawtner.ui.mypets;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pawtner.R;

public class CaptionFragment extends Fragment {

    public CaptionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_caption, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView btnBack = view.findViewById(R.id.btnBack);
        Button btnNext = view.findViewById(R.id.btnNext);
        NavController navController = Navigation.findNavController(view);

        btnBack.setOnClickListener(v -> showDiscardDialog());

        btnNext.setOnClickListener(v -> {
            showSuccessDialog();
        });

    }

    private void showSuccessDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_upload_success, null);

        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        // Biar background transparan seperti pop-up
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();

        new android.os.Handler().postDelayed(() -> {
            dialog.dismiss();
            // Navigasi ke PetProfile
            NavHostFragment.findNavController(CaptionFragment.this)
                    .navigate(R.id.action_captionFragment_to_navigation_petprofile);
        }, 1700);
    }


    private void showDiscardDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_discard, null);

        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        // Biar background dialog transparan
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Ambil tombol dari layout
        View btnDiscard = dialogView.findViewById(R.id.btnDiscard);
        View btnCancel = dialogView.findViewById(R.id.btnCancel);

        btnDiscard.setOnClickListener(v -> {
            dialog.dismiss();
            NavHostFragment.findNavController(CaptionFragment.this).popBackStack();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

}

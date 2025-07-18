package com.example.pawtner.ui.mypets;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pawtner.R;

public class EditPetPostFragment extends Fragment {

    public static final String ARG_IMAGE_RES_ID = "image_res_id";
    public static final String ARG_CAPTION = "caption";

    public static EditPetPostFragment newInstance(int imageResId, String caption) {
        EditPetPostFragment fragment = new EditPetPostFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RES_ID, imageResId);
        args.putString(ARG_CAPTION, caption);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editpetpost, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind view
        ImageView btnBack = view.findViewById(R.id.btnBack);
        ImageView ivPetImage = view.findViewById(R.id.editImageView);
        EditText editCaption = view.findViewById(R.id.editCaption);
        AppCompatButton btnNext = view.findViewById(R.id.btnNext);

        // Tombol back
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // Ambil data dari arguments
        int imageResId = requireArguments().getInt(ARG_IMAGE_RES_ID, R.drawable.cat_sample);
        String caption = requireArguments().getString(ARG_CAPTION, "");

        // Set data ke tampilan
        ivPetImage.setImageResource(imageResId);
        editCaption.setText(caption);

        // Tombol next
        btnNext.setOnClickListener(v -> {
            showSaveDialog();
        });
    }

    private void showSaveDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_save_changes, null);

        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        // ✅ Tambahkan baris ini sebelum show()
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView btnSave = dialogView.findViewById(R.id.btnSave);
        TextView btnCancel = dialogView.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(v -> {
            dialog.dismiss();
            showSuccessDialog();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    private void showSuccessDialog() {
        View successView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_success, null);

        final android.app.AlertDialog successDialog = new android.app.AlertDialog.Builder(getContext())
                .setView(successView)
                .setCancelable(false)
                .create();

        // ✅ Tambahkan baris ini sebelum show()
        successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        successDialog.show();

        new android.os.Handler().postDelayed(() -> {
            successDialog.dismiss();
            NavHostFragment.findNavController(EditPetPostFragment.this)
                    .navigate(R.id.navigation_petprofile);
        }, 1700);
    }




}

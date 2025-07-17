package com.example.pawtner.ui.mypets;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pawtner.R;

public class PetPopupDialogFragment extends DialogFragment {

    private static final String ARG_IMAGE_RES_ID = "image_res_id";

    public static PetPopupDialogFragment newInstance(int imageResId) {
        PetPopupDialogFragment fragment = new PetPopupDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RES_ID, imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        View view = inflater.inflate(R.layout.dialog_pet_popup, container, false);

        // Bind views
        ImageView imageView = view.findViewById(R.id.dialogImageView);
        ImageView btnClose = view.findViewById(R.id.btnClose);
        ImageView btnDelete = view.findViewById(R.id.btnDelete);
        ImageView btnEdit = view.findViewById(R.id.btnEdit);
        TextView btnCancelDelete = view.findViewById(R.id.btnCancelDelete);
        TextView btnConfirmDelete = view.findViewById(R.id.btnConfirmDelete);
        TextView tvCaption = view.findViewById(R.id.tvCaption);

        View layoutInfoAndCaption = view.findViewById(R.id.layoutInfoAndCaption); // Pastikan id ini ada
        View confirmDeleteLayout = view.findViewById(R.id.confirmDeleteLayout);

        // Set image from argument
        int imageResId = requireArguments().getInt(ARG_IMAGE_RES_ID);
        imageView.setImageResource(imageResId);

        // Close dialog
        btnClose.setOnClickListener(v -> dismiss());

        // Show delete confirmation
        btnDelete.setOnClickListener(v -> {
            layoutInfoAndCaption.setVisibility(View.GONE);
            confirmDeleteLayout.setVisibility(View.VISIBLE);
        });

        // Cancel delete, return to info
        btnCancelDelete.setOnClickListener(v -> {
            confirmDeleteLayout.setVisibility(View.GONE);
            layoutInfoAndCaption.setVisibility(View.VISIBLE);
        });

        // Confirm delete
        btnConfirmDelete.setOnClickListener(v -> {
            // TODO: Implement delete action (e.g. via ViewModel or callback)
            dismiss(); // Close dialog after deletion
        });

        // Navigate to edit page
        btnEdit.setOnClickListener(v -> {
            String caption = tvCaption.getText().toString();

            NavController navController = NavHostFragment.findNavController(PetPopupDialogFragment.this);

            Bundle bundle = new Bundle();
            bundle.putInt("imageResId", imageResId);
            bundle.putString("caption", caption);

            navController.navigate(R.id.action_petprofile_to_editpost, bundle);

            dismiss(); // Close the popup
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}

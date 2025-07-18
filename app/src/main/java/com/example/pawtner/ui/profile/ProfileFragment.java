package com.example.pawtner.ui.profile;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.pawtner.R;
import com.example.pawtner.ui.LoginActivity;
import com.example.pawtner.ui.profile.ProfileEditFragment;

public class ProfileFragment extends Fragment {

    private EditText nameInput, genderInput, phoneInput, emailInput, addressInput, nikInput;
    private Button editProfileBtn;

    private ActivityResultLauncher<Intent> editProfileLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Daftarkan ActivityResultLauncher
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        if (getView() != null) {
                            nameInput.setText(data.getStringExtra("name"));
                            phoneInput.setText(data.getStringExtra("phone"));
                            emailInput.setText(data.getStringExtra("email"));
                            addressInput.setText(data.getStringExtra("address"));
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameInput = view.findViewById(R.id.nameInput);
        genderInput = view.findViewById(R.id.genderInput);
        phoneInput = view.findViewById(R.id.phoneInput);
        emailInput = view.findViewById(R.id.emailInput);
        addressInput = view.findViewById(R.id.addressInput);
        nikInput = view.findViewById(R.id.nikInput);
        editProfileBtn = view.findViewById(R.id.editProfileBtn);

//        back button
        ImageView backBtn = view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> {
            androidx.navigation.NavController navController =
                    androidx.navigation.Navigation.findNavController(requireView());
            navController.popBackStack();
        });

        // Set data awal
        nameInput.setText("Maritza Eka");
        genderInput.setText("Female");
        phoneInput.setText("089123456789");
        emailInput.setText("maritza@gmail.com");
        addressInput.setText("Jl. Mawar No. 123");
        nikInput.setText("1234567890123456");

        editProfileBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("name", nameInput.getText().toString());
            bundle.putString("gender", genderInput.getText().toString());
            bundle.putString("phone", phoneInput.getText().toString());
            bundle.putString("email", emailInput.getText().toString());
            bundle.putString("address", addressInput.getText().toString());
            bundle.putString("nik", nikInput.getText().toString());

            // Navigasi menggunakan NavController
            androidx.navigation.NavController navController =
                    androidx.navigation.Navigation.findNavController(requireView());

            navController.navigate(R.id.action_profile_to_edit, bundle);
        });


        view.findViewById(R.id.logoutBtn).setOnClickListener(v -> showLogoutDialog());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 🔄 Terima data balik dari ProfileEditFragment
        getParentFragmentManager().setFragmentResultListener(
                "editProfileResult", this, (requestKey, bundle) -> {
                    if (bundle != null) {
                        nameInput.setText(bundle.getString("name", ""));
                        phoneInput.setText(bundle.getString("phone", ""));
                        emailInput.setText(bundle.getString("email", ""));
                        addressInput.setText(bundle.getString("address", ""));
                    }
                }
        );
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.logout_dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button yesBtn = dialogView.findViewById(R.id.yesBtn);
        Button noBtn = dialogView.findViewById(R.id.noBtn);
        ImageView closeBtn = dialogView.findViewById(R.id.closeBtn);

        yesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            dialog.dismiss();
            requireActivity().finish(); // Menutup MainActivity jika kamu ingin logout total
        });

        noBtn.setOnClickListener(v -> dialog.dismiss());
        closeBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}

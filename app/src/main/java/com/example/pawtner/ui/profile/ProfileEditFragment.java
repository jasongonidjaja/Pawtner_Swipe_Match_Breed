package com.example.pawtner.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pawtner.R;

public class ProfileEditFragment extends Fragment {

    EditText nameInput, genderInput, phoneInput, emailInput, addressInput, nikInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profileedit, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameInput = view.findViewById(R.id.nameInput);
        genderInput = view.findViewById(R.id.genderInput);
        phoneInput = view.findViewById(R.id.phoneInput);
        emailInput = view.findViewById(R.id.emailInput);
        addressInput = view.findViewById(R.id.addressInput);
        nikInput = view.findViewById(R.id.nikInput); // <- penting


        //        back button
        ImageView backBtn = view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> {
            androidx.navigation.NavController navController =
                    androidx.navigation.Navigation.findNavController(requireView());
            navController.popBackStack();
        });


        // Ambil data dari argument (Bundle)
        Bundle args = getArguments();
        if (args != null) {
            nameInput.setText(args.getString("name"));
            genderInput.setText(args.getString("gender"));
            phoneInput.setText(args.getString("phone"));
            emailInput.setText(args.getString("email"));
            addressInput.setText(args.getString("address"));
            nikInput.setText(args.getString("nik")); // <- penting
        }

        // Jadikan field editable (kecuali gender dan nik)
        nameInput.setEnabled(true);
        phoneInput.setEnabled(true);
        emailInput.setEnabled(true);
        addressInput.setEnabled(true);

        // Gender dan NIK tetap non-editable (dari XML sudah diset)
        nikInput.setTextColor(getResources().getColor(android.R.color.darker_gray));

        view.findViewById(R.id.saveBtn).setOnClickListener(v -> {
            // Kirim data kembali ke fragment sebelumnya atau simpan ke ViewModel
            Bundle result = new Bundle();
            result.putString("name", nameInput.getText().toString());
            result.putString("phone", phoneInput.getText().toString());
            result.putString("email", emailInput.getText().toString());
            result.putString("address", addressInput.getText().toString());

            // Kirim result (misalnya pakai Fragment Result API)
            getParentFragmentManager().setFragmentResult("editProfileResult", result);

            // Navigasi mundur
            requireActivity().onBackPressed();
        });
    }
}

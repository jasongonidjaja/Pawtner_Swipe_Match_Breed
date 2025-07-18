package com.example.pawtner.ui.mypets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.pawtner.R;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class AddPetFragment extends Fragment {

    public AddPetFragment() {
        // Konstruktor kosong dibutuhkan
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout fragment_addpet.xml
        return inflater.inflate(R.layout.fragment_addpet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        RadioGroup radioGender = view.findViewById(R.id.radioGender);

        btnSubmit.setOnClickListener(v -> {
            // Ambil gender yang dipilih
            int selectedId = radioGender.getCheckedRadioButtonId();
            String gender = "";

            if (selectedId != -1) {
                RadioButton selectedRadio = view.findViewById(selectedId);
                gender = selectedRadio.getText().toString(); // Akan "Male" atau "Female"
            }

            // Contoh: tampilkan log gender
            // Log.d("AddPetFragment", "Gender: " + gender);

            // TODO: Simpan ke database atau kirim ke ViewModel, dll.

            // Navigasi ke MyPetsFragment
            Navigation.findNavController(view).navigate(R.id.action_addpet_to_mypets);
        });
    }

}

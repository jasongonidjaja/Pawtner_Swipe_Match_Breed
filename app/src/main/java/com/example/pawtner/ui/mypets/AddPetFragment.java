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

        btnSubmit.setOnClickListener(v -> {
            // Navigasi kembali ke MyPetsFragment
            Navigation.findNavController(view).navigate(R.id.action_addpet_to_mypets);
        });
    }
}

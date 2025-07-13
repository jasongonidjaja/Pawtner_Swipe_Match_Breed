package com.example.pawtner.ui.mypets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pawtner.R;

public class AddPetFragment extends Fragment {

    public AddPetFragment() {
        // Diperlukan konstruktor kosong
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout untuk fragment_addpet
        return inflater.inflate(R.layout.fragment_addpet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tambahkan logika atau listener di sini jika diperlukan
    }
}

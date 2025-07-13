package com.example.pawtner.ui.mypets;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;

import com.example.pawtner.R;
import com.example.pawtner.adapters.PetAdapter;
import com.example.pawtner.model.Pet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MyPetsFragment extends Fragment {

//    private RecyclerView rvPets;
    private PetAdapter petAdapter;
    private List<Pet> petList;
    private SearchView searchView;
    private Button btnAll, btnCat, btnDog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mypets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cari tombol floating action
        FloatingActionButton floatingBtn = view.findViewById(R.id.floatingBtn);

        // Atur onClick untuk pindah ke fragment_addpet
        floatingBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_mypets_to_addpet);
        });

        searchView = view.findViewById(R.id.searchView);
        btnAll = view.findViewById(R.id.btnAll);
        btnCat = view.findViewById(R.id.btnCat);
        btnDog = view.findViewById(R.id.btnDog);
//        rvPets = view.findViewById(R.id.rvPets);

        btnAll.setSelected(false);
        btnCat.setSelected(false);
        btnDog.setSelected(false);

        // Dummy data sementara
        petList = new ArrayList<>(); // Harus diganti nanti dengan data asli dari database / API

        // Paksa SearchView tampil & hint muncul
        searchView.setIconified(false);
        searchView.clearFocus();

        TextView searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        if (searchText != null) {
            searchText.setHint("Search Pet");
            searchText.setHintTextColor(Color.parseColor("#999999"));
            searchText.setTextColor(Color.parseColor("#1D1B1B"));
        }

        // Setup RecyclerView
//        rvPets.setLayoutManager(new LinearLayoutManager(requireContext()));
        petAdapter = new PetAdapter(requireContext(), petList, pet -> {
            // klik pet (optional)
        });
//        rvPets.setAdapter(petAdapter);

        // SearchView filter listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                petAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                petAdapter.filter(newText);
                return true;
            }
        });

        // Filter buttons
        List<Button> filterButtons = new ArrayList<>();
        filterButtons.add(btnAll);
        filterButtons.add(btnCat);
        filterButtons.add(btnDog);

        View.OnClickListener filterClickListener = v -> {
            for (Button btn : filterButtons) {
                btn.setSelected(false);
                btn.setTextColor(requireContext().getColor(R.color.maroon));
            }

            Button clickedBtn = (Button) v;
            clickedBtn.setSelected(true);
            clickedBtn.setTextColor(requireContext().getColor(R.color.my_black));

            String filterType = clickedBtn.getText().toString();
            petAdapter.filterByType(filterType);
        };

        btnAll.setOnClickListener(filterClickListener);
        btnCat.setOnClickListener(filterClickListener);
        btnDog.setOnClickListener(filterClickListener);

        // Default selected
        btnAll.setSelected(true);
        btnAll.setTextColor(requireContext().getColor(R.color.my_black));
    }
}

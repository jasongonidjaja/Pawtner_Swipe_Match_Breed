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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.pawtner.R;
import com.example.pawtner.adapters.PetAdapter;
import com.example.pawtner.model.Pet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MyPetsFragment extends Fragment {

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

        // NavController untuk navigasi antar fragment
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

        // Floating Button untuk tambah pet
        FloatingActionButton floatingBtn = view.findViewById(R.id.floatingBtn);
        floatingBtn.setOnClickListener(v -> {
            navController.navigate(R.id.action_mypets_to_addpet);
        });

        // Tambahkan navigasi dari static card
        CardView card1 = view.findViewById(R.id.card1);
        if (card1 != null) {
            card1.setOnClickListener(v -> navController.navigate(R.id.action_mypets_to_petprofile));
        }

        CardView card2 = view.findViewById(R.id.card2);
        if (card2 != null) {
            card2.setOnClickListener(v -> navController.navigate(R.id.action_mypets_to_petprofile));
        }

        CardView card3 = view.findViewById(R.id.card3);
        if (card3 != null) {
            card3.setOnClickListener(v -> navController.navigate(R.id.action_mypets_to_petprofile));
        }

        searchView = view.findViewById(R.id.searchView);
        btnAll = view.findViewById(R.id.btnAll);
        btnCat = view.findViewById(R.id.btnCat);
        btnDog = view.findViewById(R.id.btnDog);

        btnAll.setSelected(false);
        btnCat.setSelected(false);
        btnDog.setSelected(false);

        petList = new ArrayList<>(); // Dummy list sementara

        searchView.setIconified(false);
        searchView.clearFocus();

        // Konfigurasi tampilan SearchView
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = searchView.findViewById(id);
        if (searchText != null) {
            searchText.setHint("Search Pet");
            searchText.setHintTextColor(Color.parseColor("#999999"));
            searchText.setTextColor(Color.parseColor("#1D1B1B"));
        }

        petAdapter = new PetAdapter(requireContext(), petList, pet -> {
            // klik pet (optional)
        });

        // --- FILTER BUTTON ---
        List<Button> filterButtons = new ArrayList<>();
        filterButtons.add(btnAll);
        filterButtons.add(btnCat);
        filterButtons.add(btnDog);

        View.OnClickListener filterClickListener = v -> {
            for (Button btn : filterButtons) {
                btn.setSelected(false);
                btn.setTextColor(requireContext().getColor(R.color.maroon)); // Non-aktif = maroon
            }

            Button clickedBtn = (Button) v;
            clickedBtn.setSelected(true);
            clickedBtn.setTextColor(requireContext().getColor(R.color.my_black)); // Aktif = hitam

            String filterType = clickedBtn.getText().toString();
            petAdapter.filterByType(filterType);
        };

        btnAll.setOnClickListener(filterClickListener);
        btnCat.setOnClickListener(filterClickListener);
        btnDog.setOnClickListener(filterClickListener);

        // Set default selected (All)
        btnAll.setSelected(true);
        btnAll.setTextColor(requireContext().getColor(R.color.my_black));

        // --- SEARCH LISTENER ---
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
    }
}

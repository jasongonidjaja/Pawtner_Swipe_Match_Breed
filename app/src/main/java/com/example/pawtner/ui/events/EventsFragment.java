package com.example.pawtner.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pawtner.R;
import com.example.pawtner.databinding.FragmentEventsBinding;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater, container, false);

        setupCardClickListener(); // Panggil method untuk setup click listener

        return binding.getRoot();
    }

    private void setupCardClickListener() {
        binding.eventCard.setOnClickListener(this::showEventDetail);
    }

    private void showEventDetail(View view) {
        Toast.makeText(getContext(), "Card diklik", Toast.LENGTH_SHORT).show();

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, new EventDetailFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.example.pawtner.ui.events;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.pawtner.R;
import com.example.pawtner.adapters.EventCarouselAdapter;
import com.example.pawtner.databinding.FragmentEventsBinding;
import com.example.pawtner.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;
    private RecyclerView carouselRecyclerView;
    private EventCarouselAdapter adapter;
    private Handler autoScrollHandler = new Handler();
    private Runnable autoScrollRunnable;
    private final int AUTO_SCROLL_INTERVAL = 3000; // 3 detik
    private boolean isAutoScrollEnabled = true;
    private SnapHelper snapHelper;
    private LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater, container, false);

        setupImageCarousel();
        setupCardClickListener();
        setupFilterButtons();

        SearchView searchView = binding.searchView;

        // Penting agar hint bisa muncul
        searchView.setIconified(false);       // buka langsung search view
        searchView.clearFocus();              // hilangkan fokus agar hint muncul

        // Styling EditText di dalam SearchView
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextSize(14); // Ukuran teks
        searchEditText.setGravity(Gravity.CENTER_VERTICAL); // Rata tengah vertikal
        searchEditText.setTextColor(getResources().getColor(R.color.black)); // warna teks

        // Set hint dengan benar (jangan di EditText)
        searchView.setQueryHint("Search Event");

        // Dummy listener (tidak perlu fungsi)
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return binding.getRoot(); // Harus tetap di paling akhir
    }



    private void setupImageCarousel() {
        carouselRecyclerView = binding.recyclerView;

        // Prepare data
        List<Event> events = new ArrayList<>();
        events.add(new Event(R.drawable.pets_junction_small, "Dog Club Party", "26 August 2025"));
        events.add(new Event(R.drawable.pets_fest_small, "Pet Fest Part 2", "2 September 2025"));
        events.add(new Event(R.drawable.pets_junction_small, "Pets Junction Part 2", "21 June 2025"));

        // Setup layout manager
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        carouselRecyclerView.setLayoutManager(layoutManager);

        // Setup adapter
        adapter = new EventCarouselAdapter(events);
        carouselRecyclerView.setAdapter(adapter);

        // Setup snap helper
        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(carouselRecyclerView);

        // Start at middle position for infinite scroll
        int middlePosition = Integer.MAX_VALUE / 2;
        carouselRecyclerView.scrollToPosition(middlePosition);

        // Auto-scroll runnable
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isAutoScrollEnabled || !isAdded()) return;

                View snapView = snapHelper.findSnapView(layoutManager);
                if (snapView == null) return;

                int currentPosition = layoutManager.getPosition(snapView);
                if (currentPosition == RecyclerView.NO_POSITION) return;

                int nextPosition = currentPosition + 1;
                Log.d("Carousel", "Scrolling to position: " + nextPosition);

                carouselRecyclerView.smoothScrollToPosition(nextPosition);
                autoScrollHandler.postDelayed(this, AUTO_SCROLL_INTERVAL);
            }
        };

        // Touch listener
        carouselRecyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isAutoScrollEnabled = false;
                        autoScrollHandler.removeCallbacks(autoScrollRunnable);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isAutoScrollEnabled = true;
                        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_INTERVAL);
                        break;
                }
                return false;
            }
        });

        // Start auto-scroll after slight delay to ensure layout is complete
        new Handler().postDelayed(this::startAutoScroll, 500);
    }

    private void startAutoScroll() {
        if (!isAdded()) return;
        isAutoScrollEnabled = true;
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_INTERVAL);
    }

    private void setupCardClickListener() {
        binding.eventCard.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.eventDetailFragment));
        binding.eventCard2.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.eventDetailFragment));
        binding.eventCard3.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.eventDetailFragment));
        binding.eventCard4.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.eventDetailFragment));
    }

    @Override
    public void onResume() {
        super.onResume();
        startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        isAutoScrollEnabled = false;
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }

    private void setupFilterButtons() {
        List<AppCompatButton> buttons = new ArrayList<>();
        buttons.add(binding.btnAll);
        buttons.add(binding.btnDog);
        buttons.add(binding.btnCat);

        View.OnClickListener filterClickListener = view -> {
            for (AppCompatButton button : buttons) {
                button.setSelected(false); // reset semua
            }
            view.setSelected(true); // aktifkan yang ditekan

            // Tambahkan logika filter event sesuai button yang dipilih, jika perlu
        };

        for (AppCompatButton button : buttons) {
            button.setOnClickListener(filterClickListener);
        }

        // Default selected (All)
        binding.btnAll.setSelected(true);
    }

}


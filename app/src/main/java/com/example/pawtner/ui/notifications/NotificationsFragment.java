package com.example.pawtner.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawtner.R;
import com.example.pawtner.adapters.NotificationAdapter;
import com.example.pawtner.model.NotificationItem;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private NotificationsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Back button
        ImageView backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).popBackStack();
        });

        // RecyclerView setup
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // ViewModel setup
        viewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);
        viewModel.getNotifications().observe(getViewLifecycleOwner(), notificationList -> {
            adapter = new NotificationAdapter(notificationList, (item, position) -> {
                // Mark as read
                viewModel.markAsRead(position);
                adapter.notifyItemChanged(position);

                // Navigate to detail
                Bundle bundle = new Bundle();
                bundle.putString("title", item.getTitle());
                bundle.putString("message", item.getMessage());
                bundle.putString("date", item.getDate());
                bundle.putString("time", item.getTime());

                Navigation.findNavController(requireView())
                        .navigate(R.id.action_notifications_to_detail, bundle);
            });

            recyclerView.setAdapter(adapter);
        });
    }
}

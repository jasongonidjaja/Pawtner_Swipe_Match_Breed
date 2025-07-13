package com.example.pawtner.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pawtner.R;

public class NotificationsDetailFragment extends Fragment {

    private TextView titleText, messageText, dateTimeText;

    public NotificationsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notificationsdetail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            androidx.navigation.NavController navController =
                    androidx.navigation.Navigation.findNavController(requireView());
            navController.popBackStack();
        });

        titleText = view.findViewById(R.id.detail_title);
        messageText = view.findViewById(R.id.detail_message);
        dateTimeText = view.findViewById(R.id.detail_date_time);

        if (getArguments() != null) {
            String title = getArguments().getString("title");
            String message = getArguments().getString("message");
            String date = getArguments().getString("date");
            String time = getArguments().getString("time");

            titleText.setText(title);
            messageText.setText(message);
            dateTimeText.setText(date + " • " + time);
        }
    }
}

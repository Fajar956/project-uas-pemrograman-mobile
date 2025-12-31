package com.example.travelapp.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelapp.adapter.NotificationAdapter;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityNotificationBinding;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseActivity {
    private ActivityNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set toolbar title
        TextView titleTxt = findViewById(R.id.titleTxt);
        if (titleTxt != null) {
            titleTxt.setText("Notifications");
        }

        // Setup back button
        ImageView backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        binding.notificationRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Create dummy notifications
        List<NotificationItem> notifications = new ArrayList<>();
        notifications.add(new NotificationItem("Promo Special",
                "Get 20% cashback for all destinations", "2 hours ago", true));
        notifications.add(new NotificationItem("Payment Successful",
                "Your payment for Band Island Beach is confirmed", "1 day ago", false));
        notifications.add(new NotificationItem("Trip Reminder",
                "Your trip to Bali starts in 3 days", "2 days ago", false));
        notifications.add(new NotificationItem("New Destination",
                "Komodo Island is now available", "3 days ago", false));

        NotificationAdapter adapter = new NotificationAdapter(notifications);
        binding.notificationRecycler.setAdapter(adapter);
    }

    // Inner class for notification items
    public static class NotificationItem {
        private String title;
        private String message;
        private String time;
        private boolean isUnread;

        public NotificationItem(String title, String message, String time, boolean isUnread) {
            this.title = title;
            this.message = message;
            this.time = time;
            this.isUnread = isUnread;
        }

        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getTime() { return time; }
        public boolean isUnread() { return isUnread; }
    }
}
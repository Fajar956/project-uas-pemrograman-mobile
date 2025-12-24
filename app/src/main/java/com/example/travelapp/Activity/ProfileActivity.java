package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityProfileBinding;

public class ProfileActivity extends BaseActivity {
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set toolbar title
        TextView titleTxt = findViewById(R.id.titleTxt);
        if (titleTxt != null) {
            titleTxt.setText("My Profile");
        }

        // Setup back button
        ImageView backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }

        setupMenuItems();
    }

    private void setupMenuItems() {
        // History button
        if (binding.historyBtn != null) {
            binding.historyBtn.setOnClickListener(v -> {
                Toast.makeText(this, "History feature coming soon!", Toast.LENGTH_SHORT).show();
            });
        }

        // Settings button
        if (binding.settingsBtn != null) {
            binding.settingsBtn.setOnClickListener(v -> {
                Toast.makeText(this, "Settings feature coming soon!", Toast.LENGTH_SHORT).show();
            });
        }

        // About button
        if (binding.aboutBtn != null) {
            binding.aboutBtn.setOnClickListener(v -> {
                Toast.makeText(this, "About feature coming soon!", Toast.LENGTH_SHORT).show();
            });
        }

        // Logout button
        if (binding.logoutBtn != null) {
            binding.logoutBtn.setOnClickListener(v -> {
                // Simple logout - go back to IntroActivity
                Intent intent = new Intent(ProfileActivity.this, IntroActivity.class);
                // Clear all activities and start fresh
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}
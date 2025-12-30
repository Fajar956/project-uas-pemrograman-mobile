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

        setupBackButton();
        setupEditProfile();
        setupMenuItems();
        setupBottomNavigation();
    }

    private void setupBackButton() {
        // Setup back button
        if (binding.backBtn != null) {
            binding.backBtn.setOnClickListener(v -> finish());
        }
    }

    private void setupEditProfile() {
        // Edit Profile button
        if (binding.editProfileBtn != null) {
            binding.editProfileBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            });
        }
    }

    private void setupMenuItems() {
        // History button
        if (binding.historyBtn != null) {
            binding.historyBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
                startActivity(intent);
            });
        }

        // Settings button
        if (binding.settingsBtn != null) {
            binding.settingsBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            });
        }

        // About button
        if (binding.aboutBtn != null) {
            binding.aboutBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, AboutActivity.class);
                startActivity(intent);
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

    private void setupBottomNavigation() {
        // Home Button
        if (binding.homeBtn != null) {
            binding.homeBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // Discover Button
        if (binding.discoverBtn != null) {
            binding.discoverBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, ExplorerActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // Cart Button
        if (binding.cartBtn != null) {
            binding.cartBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // Profile Button (already on profile page)
        if (binding.profileBtn != null) {
            binding.profileBtn.setOnClickListener(v -> {
                // Already on profile, do nothing or show toast
                Toast.makeText(this, "Already on Profile page", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
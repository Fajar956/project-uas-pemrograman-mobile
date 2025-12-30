package com.example.travelapp.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupClickListeners();
    }

    private void setupToolbar() {
        ImageView backBtn = findViewById(R.id.btnBack);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }
    }

    private void setupClickListeners() {
        // Edit Profile
        if (binding.btnEditProfile != null) {
            binding.btnEditProfile.setOnClickListener(v -> {
                Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show();
            });
        }

        // Change Password
        if (binding.btnChangePassword != null) {
            binding.btnChangePassword.setOnClickListener(v -> {
                Toast.makeText(this, "Change Password clicked", Toast.LENGTH_SHORT).show();
            });
        }

        // Notifications Switch
        if (binding.switchNotifications != null) {
            binding.switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Toast.makeText(this, "Notifications: " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
            });
        }

        // Dark Mode Switch
        if (binding.switchDarkMode != null) {
            binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Toast.makeText(this, "Dark Mode: " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
            });
        }

        // Language
        if (binding.btnLanguage != null) {
            binding.btnLanguage.setOnClickListener(v -> {
                Toast.makeText(this, "Language clicked", Toast.LENGTH_SHORT).show();
            });
        }

        // Help Center
        if (binding.btnHelp != null) {
            binding.btnHelp.setOnClickListener(v -> {
                Toast.makeText(this, "Help Center clicked", Toast.LENGTH_SHORT).show();
            });
        }

        // Privacy Policy
        if (binding.btnPrivacy != null) {
            binding.btnPrivacy.setOnClickListener(v -> {
                Toast.makeText(this, "Privacy Policy clicked", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
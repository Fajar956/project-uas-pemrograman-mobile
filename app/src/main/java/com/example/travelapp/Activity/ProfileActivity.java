package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        // Set toolbar title (opsional, karena di layout pakai TextView "Profile")
        // Tidak diperlukan jika Anda tidak punya titleTxt di layout

        // Setup back button
        binding.backButton.setOnClickListener(v -> finish());

        setupMenuItems();
    }

    private void setupMenuItems() {
        // Edit Profile
        binding.editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        // History
        binding.historyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // Settings
        binding.settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // About
        binding.aboutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        // Logout
        binding.logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, IntroActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
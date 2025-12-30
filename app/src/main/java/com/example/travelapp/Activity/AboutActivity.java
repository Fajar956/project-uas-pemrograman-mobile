package com.example.travelapp.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {
    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupSocialMedia();
    }

    private void setupToolbar() {
        ImageView backBtn = findViewById(R.id.btnBack);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }
    }

    private void setupSocialMedia() {
        // Facebook
        if (binding.btnFacebook != null) {
            binding.btnFacebook.setOnClickListener(v -> {
                openUrl("https://www.facebook.com/travelapp");
            });
        }

        // Instagram
        if (binding.btnInstagram != null) {
            binding.btnInstagram.setOnClickListener(v -> {
                openUrl("https://www.instagram.com/travelapp");
            });
        }

        // Twitter
        if (binding.btnTwitter != null) {
            binding.btnTwitter.setOnClickListener(v -> {
                openUrl("https://www.twitter.com/travelapp");
            });
        }
    }

    private void openUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot open link", Toast.LENGTH_SHORT).show();
        }
    }
}
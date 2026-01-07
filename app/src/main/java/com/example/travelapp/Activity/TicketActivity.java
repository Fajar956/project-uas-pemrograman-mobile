package com.example.travelapp.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.ItemDomain;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityTicketBinding;

public class TicketActivity extends BaseActivity {
    private ActivityTicketBinding binding;
    private ItemDomain object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    private void getIntentExtra() {
        object = (ItemDomain) getIntent().getSerializableExtra("object");
        if (object == null) {
            Toast.makeText(this, "No ticket data found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    private void setVariable() {
        // Load gambar destinasi
        Glide.with(this)
                .load(object.getPic())
                .into(binding.pic);

        // Load gambar tour guide
        Glide.with(this)
                .load(object.getTourGuidePic())
                .into(binding.profileTxt);

        // Set text
        binding.titleTxt.setText(object.getTitle());
        binding.durationTxt.setText(object.getDuration());
        binding.tourGuideTxt.setText(object.getDateTour());
        binding.timeTxt.setText(object.getTimeTour());
        binding.tourGuideNameTxt.setText(object.getTourGuideName());

        // Back button
        binding.backBtn.setOnClickListener(v -> finish());

        // Call button → telepon
        binding.callBtn.setOnClickListener(v -> {
            String phone = object.getTourGuidePhone();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(intent);
        });

        // Message button → SMS
        binding.messageBtn.setOnClickListener(v -> {
            String phone = object.getTourGuidePhone();
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
            intent.putExtra("sms_body", "Hello, I have a question about my tour.");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        // ✅ DOWNLOAD TICKET → NOTIFIKASI SAJA
        binding.button.setOnClickListener(v -> {
            Toast.makeText(this, "Ticket downloaded successfully!", Toast.LENGTH_SHORT).show();
        });
    }
}
package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.ItemDomain;
import com.example.travelapp.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private ItemDomain object; // ✅ Ini nama variabelnya

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntenExtra();
        setVariable();
    }

    private void setVariable() {
        if (object != null) {
            binding.titleTxt.setText(object.getTitle());
            binding.priceTxt.setText("$" + object.getPrice());
            binding.backBtn.setOnClickListener(v -> finish());
            binding.bedTxt.setText("" + object.getBed());
            binding.durationTxt.setText(object.getDuration());
            binding.distancetxt.setText(object.getDistance());
            binding.descriptionTxt.setText(object.getDescription());
            binding.addressTxt.setText(object.getAddress());
            binding.ratingTxt.setText(object.getScore() + " Rating");
            binding.ratingBar.setRating((float) object.getScore());
            Glide.with(DetailActivity.this)
                    .load(object.getPic())
                    .into(binding.pic);

            // ✅ PERBAIKAN: Ganti 'destination' → 'object'
            binding.addToCartBtn.setOnClickListener(v -> {
                Intent intent = new Intent(DetailActivity.this, CartActivity.class);
                intent.putExtra("title", object.getTitle());
                intent.putExtra("location", object.getAddress());
                intent.putExtra("price", object.getPrice());
                intent.putExtra("image", object.getPic()); // ✅ Ubah dari imageResId ke image (URL)
                startActivity(intent);
            });
        }
    }

    private void getIntenExtra() {
        object = (ItemDomain) getIntent().getSerializableExtra("object");
    }
}
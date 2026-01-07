package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelapp.Adapter.CartAdapter;
import com.example.travelapp.Domain.ItemDomain;
import com.example.travelapp.Model.CartItem;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityCartBinding;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends BaseActivity {

    private ActivityCartBinding binding;
    private List<CartItem> cartItems = new ArrayList<>();
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Cart");
        }

        // Setup RecyclerView
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(this, cartItems);
        binding.cartRecyclerView.setAdapter(adapter);

        // Handle data dari DetailActivity
        handleIntentData();

        // Setup Bottom Navigation
        setupBottomNavigation();

        // ✅ SETUP CHECKOUT BUTTON — DIPERBAIKI
        binding.checkoutBtn.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                // Ambil item pertama (asumsi: 1 tiket per checkout)
                CartItem item = cartItems.get(0);

                // Buat objek ItemDomain dari CartItem
                ItemDomain ticket = new ItemDomain();
                ticket.setTitle(item.getTitle());
                ticket.setPic(item.getImageUrl());
                ticket.setAddress(item.getLocation());
                ticket.setPrice((int) item.getPrice());
                ticket.setDuration("3 Hours"); // bisa disesuaikan
                ticket.setDateTour("2026-01-10");
                ticket.setTimeTour("09:00 AM");
                ticket.setTourGuideName("John Doe");
                ticket.setTourGuidePhone("+628123456789");
                ticket.setTourGuidePic("https://example.com/guide.jpg");

                // Kirim ke TicketActivity
                Intent intent = new Intent(CartActivity.this, TicketActivity.class);
                intent.putExtra("object", ticket);
                startActivity(intent);
            }
        });
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("title")) {
            String title = intent.getStringExtra("title");
            String location = intent.getStringExtra("location");
            double price = intent.getDoubleExtra("price", 0.0);
            String imageUrl = intent.getStringExtra("image");

            boolean exists = false;
            for (CartItem item : cartItems) {
                if (item.getTitle().equals(title)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                cartItems.add(new CartItem(title, location, price, imageUrl));
                adapter.notifyDataSetChanged();
                updateTotalPrice();
                Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Item already in cart", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateTotalPrice() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getPrice();
        }
        binding.totalPrice.setText("$" + String.format("%.2f", total));
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setItemSelected(R.id.cart, true);
        binding.bottomNavigation.setOnItemSelectedListener(id -> {
            if (id == R.id.explorer) {
                startActivity(new Intent(CartActivity.this, ExplorerActivity.class));
                finish();
            } else if (id == R.id.profile) {
                startActivity(new Intent(CartActivity.this, ProfileActivity.class));
                finish();
            } else if (id == R.id.cart) {
                // Already here
            }
        });
    }

    public void onCartItemRemoved() {
        updateTotalPrice();
        if (cartItems.isEmpty()) {
            binding.totalPrice.setText("$0.00");
        }
    }
}
package com.example.travelapp.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelapp.adapter.CartAdapter;
import com.example.travelapp.Domain.ItemDomain;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityCartBinding;

import java.util.ArrayList;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private CartAdapter adapter;
    private ArrayList<ItemDomain> cartItems = new ArrayList<>();
    private double totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set toolbar title
        TextView titleTxt = findViewById(R.id.titleTxt);
        if (titleTxt != null) {
            titleTxt.setText("My Cart");
        }

        // Setup back button
        ImageView backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }

        setupRecyclerView();
        setupCheckoutButton();
        calculateTotal();
    }

    private void setupRecyclerView() {
        binding.cartRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Dummy data for testing
        if (cartItems.isEmpty()) {
            // Create dummy items
            ItemDomain item1 = new ItemDomain();
            item1.setTitle("Band Island Beach");
            item1.setPrice(900);
            item1.setAddress("Hawaii, USA");
            item1.setPic("https://example.com/image1.jpg");

            ItemDomain item2 = new ItemDomain();
            item2.setTitle("Komodo Island");
            item2.setPrice(800);
            item2.setAddress("Lombok, Indonesia");
            item2.setPic("https://example.com/image2.jpg");

            cartItems.add(item1);
            cartItems.add(item2);
        }

        adapter = new CartAdapter(cartItems);
        binding.cartRecycler.setAdapter(adapter);
    }

    private void setupCheckoutButton() {
        binding.checkoutBtn.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CartActivity.this, "Checkout successful!", Toast.LENGTH_SHORT).show();
                // Untuk sementara, kembali ke MainActivity
                finish();
            }
        });
    }

    private void calculateTotal() {
        totalPrice = 0;
        for (ItemDomain item : cartItems) {
            totalPrice += item.getPrice();
        }
        binding.totalPrice.setText("$" + totalPrice);
    }
}
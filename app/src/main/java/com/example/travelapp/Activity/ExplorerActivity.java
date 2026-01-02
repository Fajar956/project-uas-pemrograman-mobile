package com.example.travelapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.travelapp.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ExplorerActivity extends AppCompatActivity {

    // Deklarasi variabel UI dari layout
    private EditText searchEditText;
    private ImageButton filterButton;
    private RecyclerView categoriesRecyclerView;
    private RecyclerView trendingRecyclerView;
    private RecyclerView topDestinationsRecyclerView;
    private RecyclerView forYouRecyclerView;
    private ChipNavigationBar bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        initViews();
        setupRecyclerViews();
        setupListeners();
    }

    private void initViews() {
        searchEditText = findViewById(R.id.searchEditText);
        filterButton = findViewById(R.id.filterButton);
        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        trendingRecyclerView = findViewById(R.id.trendingRecyclerView);
        topDestinationsRecyclerView = findViewById(R.id.topDestinationsRecyclerView);
        forYouRecyclerView = findViewById(R.id.forYouRecyclerView);
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    private void setupRecyclerViews() {
        // Categories (horizontal)
        categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        // Trending (vertical)
        trendingRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        // Top Destinations (vertical)
        topDestinationsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        // For You (vertical)
        forYouRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
    }

    private void setupListeners() {
        // Filter button
        filterButton.setOnClickListener(v ->
                Toast.makeText(this, "Filter feature coming soon!", Toast.LENGTH_SHORT).show()
        );

        // Bottom Navigation
        bottomNavigation.setItemSelected(R.id.tvPrice, true); // Set "Explorer" sebagai aktif

        bottomNavigation.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                if (id == R.id.explorer) {
                    Toast.makeText(ExplorerActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    finish(); // Kembali ke MainActivity
                } else if (id == R.id.tvPrice) {
                    Toast.makeText(ExplorerActivity.this, "You are in Explorer", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.cart) {
                    Toast.makeText(ExplorerActivity.this, "Cart", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.profile) {
                    Toast.makeText(ExplorerActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
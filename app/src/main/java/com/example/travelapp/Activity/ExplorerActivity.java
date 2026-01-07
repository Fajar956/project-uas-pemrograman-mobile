package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelapp.Adapter.CategoryAdapter;
import com.example.travelapp.Adapter.TrendingAdapter;
import com.example.travelapp.Domain.Category;
import com.example.travelapp.Domain.ItemDomain;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityExplorerBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExplorerActivity extends BaseActivity {
    private ActivityExplorerBinding binding;
    private ArrayList<ItemDomain> trendingList = new ArrayList<>();
    private TrendingAdapter trendingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExplorerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupCategoryRecyclerView();      // Kategori tetap dummy (opsional)
        loadTrendingDataFromFirebase();  // Ambil dari Firebase
        setupBottomNavigation();

        // See All Trending → buka SeeAllActivity (Popular)
        binding.seeAllTrending.setOnClickListener(v -> {
            Intent intent = new Intent(ExplorerActivity.this, SeeAllActivity.class);
            intent.putExtra("type", "popular");
            startActivity(intent);
        });
    }

    // 🔹 Bagian Kategori (masih dummy)
    private void setupCategoryRecyclerView() {
        List<Category> categories = new ArrayList<>();
        categories.add(createCategory("Beach", "https://picsum.photos/seed/beach/150/150"));
        categories.add(createCategory("Mountain", "https://picsum.photos/seed/mountain/150/150"));
        categories.add(createCategory("City", "https://picsum.photos/seed/city/150/150"));
        categories.add(createCategory("Adventure", "https://picsum.photos/seed/adventure/150/150"));
        categories.add(createCategory("Cultural", "https://picsum.photos/seed/cultural/150/150"));

        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        binding.categoriesRecyclerView.setAdapter(categoryAdapter);
        binding.categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
    }

    // 🔹 Ambil data "Trending Now" dari Firebase (node "Popular")
    private void loadTrendingDataFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference popularRef = database.getReference("Popular");

        popularRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                trendingList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ItemDomain item = ds.getValue(ItemDomain.class);
                        if (item != null) {
                            trendingList.add(item);
                        }
                    }
                    if (trendingAdapter == null) {
                        trendingAdapter = new TrendingAdapter(ExplorerActivity.this, trendingList);
                        binding.trendingRecyclerView.setAdapter(trendingAdapter);
                        binding.trendingRecyclerView.setLayoutManager(new LinearLayoutManager(ExplorerActivity.this));
                    } else {
                        trendingAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ExplorerActivity.this, "Failed to load trending data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 🔹 Helper untuk membuat kategori dummy
    private Category createCategory(String name, String imageUrl) {
        Category category = new Category();
        category.setName(name);
        category.setImagePath(imageUrl);
        return category;
    }

    // 🔹 Setup Bottom Navigation
    private void setupBottomNavigation() {
        binding.bottomNavigation.setItemSelected(R.id.explorer, true);
        binding.bottomNavigation.setOnItemSelectedListener(id -> {
            if (id == R.id.cart) {
                startActivity(new Intent(this, CartActivity.class));
                finish();
            } else if (id == R.id.profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
            }
            // R.id.explorer: tetap di halaman ini
        });
    }
}
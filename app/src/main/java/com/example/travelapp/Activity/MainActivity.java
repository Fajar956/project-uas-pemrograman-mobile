package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.Adapter.CategoryAdapter;
import com.example.travelapp.Adapter.PopularAdapter;
import com.example.travelapp.Adapter.RecommendedAdapter;
import com.example.travelapp.Adapter.SliderAdapter;
import com.example.travelapp.Domain.Category;
import com.example.travelapp.Domain.ItemDomain;
import com.example.travelapp.Domain.Location;
import com.example.travelapp.Domain.SliderItems;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private DatabaseReference database; // ✅ field untuk Firebase Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ Inisialisasi Firebase Database DI SINI
        database = FirebaseDatabase.getInstance().getReference();

        initLocation();
        initBanner();
        initCategory();
        initRecommended();
        initPopular();
        setupBottomNavigation();
        setupSearch();

        // Search button click
        TextView searchBtn = findViewById(R.id.textView4);
        searchBtn.setOnClickListener(v -> {
            String query = binding.editTextText2.getText().toString().trim();
            performSearch(query);
        });

        // Bell icon click
        ImageView bellIcon = findViewById(R.id.imageView);
        bellIcon.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
        });
    }

    private void setupSearch() {
        EditText searchBar = findViewById(R.id.editTextText2);
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_GO) {
                String query = searchBar.getText().toString().trim();
                performSearch(query);
                searchBar.clearFocus();
                return true;
            }
            return false;
        });
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            initRecommended();
            initPopular();
            return;
        }

        // Filter Recommended
        ArrayList<ItemDomain> filteredRecommended = new ArrayList<>();
        for (ItemDomain item : getRecommendedList()) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    item.getAddress().toLowerCase().contains(query.toLowerCase())) {
                filteredRecommended.add(item);
            }
        }

        // Filter Popular
        ArrayList<ItemDomain> filteredPopular = new ArrayList<>();
        for (ItemDomain item : getPopularList()) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    item.getAddress().toLowerCase().contains(query.toLowerCase())) {
                filteredPopular.add(item);
            }
        }

        // Update RecyclerView
        RecommendedAdapter recommendedAdapter = new RecommendedAdapter(filteredRecommended);
        binding.recyclerViewRecommended.setAdapter(recommendedAdapter);

        PopularAdapter popularAdapter = new PopularAdapter(filteredPopular);
        binding.recyclerViewPopular.setAdapter(popularAdapter);

        if (filteredRecommended.isEmpty() && filteredPopular.isEmpty()) {
            Toast.makeText(this, "No results found for: " + query, Toast.LENGTH_SHORT).show();
        }
    }

    // ✅ NAVIGASI BARU – TANPA CHIP NAVIGATION
    private void setupBottomNavigation() {
        findViewById(R.id.homeBtn).setOnClickListener(v ->
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.discoverBtn).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ExplorerActivity.class))
        );

        findViewById(R.id.cartBtn).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CartActivity.class))
        );

        findViewById(R.id.profileBtn).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ProfileActivity.class))
        );
    }

    private void initPopular() {
        DatabaseReference myRef = database.child("Popular");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<ItemDomain> popularList = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                popularList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ItemDomain item = issue.getValue(ItemDomain.class);
                        if (item != null) popularList.add(item);
                    }
                    if (!popularList.isEmpty()) {
                        binding.recyclerViewPopular.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        PopularAdapter adapter = new PopularAdapter(popularList);
                        binding.recyclerViewPopular.setAdapter(adapter);
                    }
                }
                binding.progressBarPopular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarPopular.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to load popular items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecommended() {
        DatabaseReference myRef = database.child("Item");
        binding.progressBarRecommended.setVisibility(View.VISIBLE);
        ArrayList<ItemDomain> itemList = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ItemDomain item = issue.getValue(ItemDomain.class);
                        if (item != null) itemList.add(item);
                    }
                    if (!itemList.isEmpty()) {
                        binding.recyclerViewRecommended.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        RecommendedAdapter adapter = new RecommendedAdapter(itemList);
                        binding.recyclerViewRecommended.setAdapter(adapter);
                    }
                }
                binding.progressBarRecommended.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarRecommended.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to load recommended items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initCategory() {
        DatabaseReference myRef = database.child("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> categoryList = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Category category = issue.getValue(Category.class);
                        if (category != null) categoryList.add(category);
                    }
                    if (!categoryList.isEmpty()) {
                        binding.recyclerViewCategory.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        CategoryAdapter adapter = new CategoryAdapter(categoryList);
                        binding.recyclerViewCategory.setAdapter(adapter);
                    }
                }
                binding.progressBarCategory.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarCategory.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initLocation() {
        DatabaseReference myRef = database.child("Location");
        ArrayList<Location> locationList = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                locationList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Location location = issue.getValue(Location.class);
                        if (location != null) locationList.add(location);
                    }
                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this,
                            R.layout.sp_item, locationList);
                    adapter.setDropDownViewResource(R.layout.sp_item);
                    binding.spinner2.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load locations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewPagerSlider.setAdapter(new SliderAdapter(items, binding.viewPagerSlider));
        binding.viewPagerSlider.setClipToPadding(false);
        binding.viewPagerSlider.setClipChildren(false);
        binding.viewPagerSlider.setOffscreenPageLimit(3);
        binding.viewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    private void initBanner() {
        DatabaseReference myRef = database.child("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> bannerList = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bannerList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        SliderItems item = issue.getValue(SliderItems.class);
                        if (item != null) bannerList.add(item);
                    }
                    banners(bannerList);
                }
                binding.progressBarBanner.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarBanner.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to load banner", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper method untuk search (opsional)
    private ArrayList<ItemDomain> getRecommendedList() {
        // Catatan: karena data async, kita tidak bisa return langsung.
        // Tapi untuk pencarian, kita asumsikan data sudah di-load → jadi simpan di field jika diperlukan.
        // Untuk sementara, biarkan performSearch() panggil ulang init jika perlu.
        return new ArrayList<>();
    }

    private ArrayList<ItemDomain> getPopularList() {
        return new ArrayList<>();
    }
}
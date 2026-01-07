package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private ArrayList<ItemDomain> itemList = new ArrayList<>();
    private ArrayList<ItemDomain> popularList = new ArrayList<>();
    private RecommendedAdapter recommendedAdapter;
    private PopularAdapter popularAdapter;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        initLocation();
        initBanner();
        initCategory();
        initRecommended();
        initPopular();
        setupBottomNavigation();
        setupSearch();

        // Search Button
        binding.searchBtn.setOnClickListener(v -> {
            String query = binding.editTextText2.getText().toString().trim();
            performSearch(query);
        });

        // Notification Icon - Navigate to NotificationActivity
        binding.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

        // See All Buttons
        binding.seeAllRecommended.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SeeAllActivity.class);
            intent.putExtra("type", "recommended");
            startActivity(intent);
        });

        binding.seeAllPopular.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SeeAllActivity.class);
            intent.putExtra("type", "popular");
            startActivity(intent);
        });
    }

    private void setupSearch() {
        binding.editTextText2.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_GO) {
                String query = binding.editTextText2.getText().toString().trim();
                performSearch(query);
                binding.editTextText2.clearFocus();
                return true;
            }
            return false;
        });
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            if (!itemList.isEmpty()) {
                recommendedAdapter = new RecommendedAdapter(this, itemList);
                binding.recyclerViewRecommended.setAdapter(recommendedAdapter);
            }
            if (!popularList.isEmpty()) {
                popularAdapter = new PopularAdapter(this, popularList);
                binding.recyclerViewPopular.setAdapter(popularAdapter);
            }
            return;
        }

        ArrayList<ItemDomain> filteredRecommended = new ArrayList<>();
        for (ItemDomain item : itemList) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    item.getAddress().toLowerCase().contains(query.toLowerCase())) {
                filteredRecommended.add(item);
            }
        }

        ArrayList<ItemDomain> filteredPopular = new ArrayList<>();
        for (ItemDomain item : popularList) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    item.getAddress().toLowerCase().contains(query.toLowerCase())) {
                filteredPopular.add(item);
            }
        }

        recommendedAdapter = new RecommendedAdapter(this, filteredRecommended);
        binding.recyclerViewRecommended.setAdapter(recommendedAdapter);

        popularAdapter = new PopularAdapter(this, filteredPopular);
        binding.recyclerViewPopular.setAdapter(popularAdapter);

        if (filteredRecommended.isEmpty() && filteredPopular.isEmpty()) {
            Toast.makeText(this, "No results found for: " + query, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBottomNavigation() {
        // Set home/explorer sebagai item yang dipilih
        binding.bottomNavigation.setItemSelected(R.id.explorer, true);

        binding.bottomNavigation.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                if (id == R.id.explorer) {
                    // Already in MainActivity (Home)
                    // Tidak perlu action

                } else if (id == R.id.vPrice) {
                    // Navigate to ExplorerActivity
                    Intent intent = new Intent(MainActivity.this, ExplorerActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                } else if (id == R.id.cart) {
                    // Navigate to CartActivity
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                } else if (id == R.id.profile) {
                    // Navigate to ProfileActivity
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    private void initPopular() {
        DatabaseReference myRef = database.getReference("Popular");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        popularList.clear();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ItemDomain item = issue.getValue(ItemDomain.class);
                        if (item != null) {
                            popularList.add(item);
                        }
                    }
                    if (!popularList.isEmpty()) {
                        binding.recyclerViewPopular.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false)
                        );
                        popularAdapter = new PopularAdapter(MainActivity.this, popularList);
                        binding.recyclerViewPopular.setAdapter(popularAdapter);
                    }
                    binding.progressBarPopular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarPopular.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to load popular items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecommended() {
        DatabaseReference myRef = database.getReference("Item");
        binding.progressBarRecommended.setVisibility(View.VISIBLE);
        itemList.clear();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ItemDomain item = issue.getValue(ItemDomain.class);
                        if (item != null) {
                            itemList.add(item);
                        }
                    }
                    if (!itemList.isEmpty()) {
                        binding.recyclerViewRecommended.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false)
                        );
                        recommendedAdapter = new RecommendedAdapter(MainActivity.this, itemList);
                        binding.recyclerViewRecommended.setAdapter(recommendedAdapter);
                    }
                    binding.progressBarRecommended.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarRecommended.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to load recommended items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Category category = issue.getValue(Category.class);
                        if (category != null) {
                            list.add(category);
                        }
                    }
                    if (!list.isEmpty()) {
                        binding.recyclerViewCategory.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false)
                        );
                        CategoryAdapter adapter = new CategoryAdapter(list);
                        binding.recyclerViewCategory.setAdapter(adapter);
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarCategory.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initLocation() {
        DatabaseReference myRef = database.getReference("Location");
        ArrayList<Location> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Location location = issue.getValue(Location.class);
                        if (location != null) {
                            list.add(location);
                        }
                    }
                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(
                            MainActivity.this, R.layout.sp_item, list
                    );
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
        binding.viewPagerSlider.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        SliderItems item = issue.getValue(SliderItems.class);
                        if (item != null) {
                            list.add(item);
                        }
                    }
                    banners(list);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarBanner.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to load banner", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set bottom navigation ke home saat kembali dari activity lain
        binding.bottomNavigation.setItemSelected(R.id.explorer, true);
    }
}
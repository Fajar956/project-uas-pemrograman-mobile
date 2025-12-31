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

// ❌ HAPUS BARIS INI: import com.example.travelapp.Activity.MainActivity;
import com.example.travelapp.R;
import com.example.travelapp.adapter.CategoryAdapter;
import com.example.travelapp.adapter.PopularAdapter;
import com.example.travelapp.adapter.RecommendedAdapter;
import com.example.travelapp.adapter.SliderAdapter;
import com.example.travelapp.databinding.ActivityMainBinding;
import com.example.travelapp.Domain.Category;
import com.example.travelapp.Domain.ItemDomain;
import com.example.travelapp.Domain.Location;
import com.example.travelapp.Domain.SliderItems;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    private ArrayList<ItemDomain> itemList = new ArrayList<>();
    private ArrayList<ItemDomain> popularList = new ArrayList<>();
    private RecommendedAdapter recommendedAdapter;
    private PopularAdapter popularAdapter;
    // ✅ Tambahkan DatabaseReference global jika diperlukan
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance();

        initLocation();
        initBanner();
        initCategory();
        initRecommended();
        initPopular();
        setupBottomNavigation();
        setupSearch();

        // Tambah click listener untuk tombol Search
        TextView searchBtn = findViewById(R.id.textView4);
        searchBtn.setOnClickListener(v -> {
            String query = binding.editTextText2.getText().toString().trim();
            performSearch(query);
        });

        // Tambah click listener untuk icon bell (notification)
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
            if (recommendedAdapter != null && !itemList.isEmpty()) {
                recommendedAdapter = new RecommendedAdapter(itemList);
                binding.recyclerViewRecommended.setAdapter(recommendedAdapter);
            }
            if (popularAdapter != null && !popularList.isEmpty()) {
                popularAdapter = new PopularAdapter(popularList);
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

        recommendedAdapter = new RecommendedAdapter(filteredRecommended);
        binding.recyclerViewRecommended.setAdapter(recommendedAdapter);

        popularAdapter = new PopularAdapter(filteredPopular);
        binding.recyclerViewPopular.setAdapter(popularAdapter);

        if (filteredRecommended.isEmpty() && filteredPopular.isEmpty()) {
            Toast.makeText(this, "No results found for: " + query, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBottomNavigation() {
        ChipNavigationBar bottomNav = findViewById(R.id.bottom_navigation);

        if (bottomNav == null) {
            Toast.makeText(this, "Bottom navigation not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        bottomNav.setItemSelected(R.id.home, true);

        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                if (id == R.id.home) {
                    // Tetap di MainActivity (Home)
                } else if (id == R.id.explorer) {
                    startActivity(new Intent(MainActivity.this, ExplorerActivity.class));
                } else if (id == R.id.cart) {
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                } else if (id == R.id.profile) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
            }
        });
    }

    private void initPopular() {
        DatabaseReference myref = database.getReference("Popular");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        popularList.clear();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ItemDomain item = issue.getValue(ItemDomain.class);
                        popularList.add(item);
                    }
                    if (!popularList.isEmpty()) {
                        binding.recyclerViewPopular.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false)
                        );
                        popularAdapter = new PopularAdapter(popularList);
                        binding.recyclerViewPopular.setAdapter(popularAdapter);
                    }
                }
                binding.progressBarPopular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarPopular.setVisibility(View.GONE);
            }
        });
    }

    private void initRecommended() {
        DatabaseReference myref = database.getReference("Item");
        binding.progressBarRecommended.setVisibility(View.VISIBLE);
        itemList.clear();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ItemDomain item = issue.getValue(ItemDomain.class);
                        itemList.add(item);
                    }
                    if (!itemList.isEmpty()) {
                        binding.recyclerViewRecommended.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false)
                        );
                        recommendedAdapter = new RecommendedAdapter(itemList);
                        binding.recyclerViewRecommended.setAdapter(recommendedAdapter);
                    }
                }
                binding.progressBarRecommended.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarRecommended.setVisibility(View.GONE);
            }
        });
    }

    private void initCategory() {
        DatabaseReference myref = database.getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> list = new ArrayList<>();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Category.class));
                    }
                    if (!list.isEmpty()) {
                        binding.recyclerViewCategory.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false)
                        );
                        CategoryAdapter adapter = new CategoryAdapter(list);
                        binding.recyclerViewCategory.setAdapter(adapter);
                    }
                }
                binding.progressBarCategory.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarCategory.setVisibility(View.GONE);
            }
        });
    }

    private void initLocation() {
        DatabaseReference myref = database.getReference("Location");
        ArrayList<Location> list = new ArrayList<>();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Location.class));
                    }
                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this,
                            R.layout.sp_item, list);
                    adapter.setDropDownViewResource(R.layout.sp_item);
                    binding.spinner2.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Opsional: tampilkan error
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
        DatabaseReference myref = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> list = new ArrayList<>();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(SliderItems.class));
                    }
                    banners(list);
                }
                binding.progressBarBanner.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarBanner.setVisibility(View.GONE);
            }
        });
    }
}
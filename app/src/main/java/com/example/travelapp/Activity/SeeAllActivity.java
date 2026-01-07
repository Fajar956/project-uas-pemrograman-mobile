package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.Adapter.SeeAllAdapter;
import com.example.travelapp.Domain.ItemDomain;
import com.example.travelapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class SeeAllActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSeeAll;
    private SeeAllAdapter adapter;
    private ArrayList<ItemDomain> itemList;
    private ProgressBar progressBar;
    private String type;
    private ChipNavigationBar bottomNavigation;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);

        // Inisialisasi View
        recyclerViewSeeAll = findViewById(R.id.recyclerViewSeeAll);
        progressBar = findViewById(R.id.progressBarSeeAll); // Pastikan ada di XML!
        bottomNavigation = findViewById(R.id.bottom_navigation);
        // Jika Anda pakai Toolbar custom dengan TextView:
        toolbarTitle = findViewById(R.id.toolbarTitle); // Optional, sesuaikan

        // Ambil tipe dari MainActivity
        type = getIntent().getStringExtra("type");
        if (type == null) type = "popular";

        // Set judul berdasarkan tipe
        if ("recommended".equals(type)) {
            if (toolbarTitle != null) toolbarTitle.setText("Recommended");
        } else {
            if (toolbarTitle != null) toolbarTitle.setText("Popular Destination");
        }

        // Setup RecyclerView
        itemList = new ArrayList<>();
        adapter = new SeeAllAdapter(this, itemList);
        recyclerViewSeeAll.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSeeAll.setAdapter(adapter);

        // Load data dari Firebase
        loadDataFromFirebase();

        // Setup bottom navigation
        setupBottomNavigation();
    }

    private void loadDataFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        itemList.clear();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = "recommended".equals(type) ?
                database.getReference("Item") :
                database.getReference("Popular");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ItemDomain item = ds.getValue(ItemDomain.class);
                        if (item != null) {
                            itemList.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);

                if (itemList.isEmpty()) {
                    Toast.makeText(SeeAllActivity.this, "No items available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SeeAllActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigation.setItemSelected(R.id.explorer, true);
        bottomNavigation.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                if (id == R.id.explorer) {
                    finish(); // Kembali ke MainActivity
                } else if (id == R.id.vPrice) {
                    startActivity(new Intent(SeeAllActivity.this, ExplorerActivity.class));
                    finish();
                } else if (id == R.id.cart) {
                    startActivity(new Intent(SeeAllActivity.this, CartActivity.class));
                    finish();
                } else if (id == R.id.profile) {
                    startActivity(new Intent(SeeAllActivity.this, ProfileActivity.class));
                    finish();
                }
            }
        });
    }
}
package com.example.travelapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager; // 🔴 Tambahkan ini
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class ExplorerActivity extends AppCompatActivity {

    private MapView mapView;
    private IMapController mapController;
    private CardView mapContainer;
    private NestedScrollView contentScrollView;
    private MaterialButton mapToggleBtn;
    private EditText searchEditText;
    private FloatingActionButton filterFab;
    private RecyclerView categoriesRecyclerView, trendingRecyclerView;
    private MyLocationNewOverlay myLocationOverlay;

    private boolean isMapVisible = false;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 🔵 🔴 WAJIB: Inisialisasi OSMDroid config SEBELUM setContentView()
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        initViews();
        checkPermissions();
        setupMap();
        setupListeners();
        addDestinationMarkers();
    }

    private void initViews() {
        mapView = findViewById(R.id.mapView);
        mapContainer = findViewById(R.id.mapContainer);
        contentScrollView = findViewById(R.id.contentScrollView);
        mapToggleBtn = findViewById(R.id.mapToggleBtn);
        searchEditText = findViewById(R.id.searchEditText);
        filterFab = findViewById(R.id.filterFab);
        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        trendingRecyclerView = findViewById(R.id.trendingRecyclerView);

        categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        trendingRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
    }

    private void checkPermissions() {
        // ✅ Hanya minta location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void setupMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);

        mapController = mapView.getController();
        GeoPoint worldCenter = new GeoPoint(0.0, 0.0);
        mapController.setCenter(worldCenter);
        mapController.setZoom(3.0);

        // Compass
        CompassOverlay compassOverlay = new CompassOverlay(
                this,
                new InternalCompassOrientationProvider(this),
                mapView
        );
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        // My Location
        myLocationOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(this),
                mapView
        );
        // Hanya enable jika permission sudah ada
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            myLocationOverlay.enableMyLocation();
        }
        mapView.getOverlays().add(myLocationOverlay);
    }

    private void addDestinationMarkers() {
        addMarker(-8.4095, 115.1889, "Bali", "Island of the Gods - Beautiful beaches and temples");
        addMarker(48.8566, 2.3522, "Paris", "City of Love - Eiffel Tower and Louvre Museum");
        addMarker(35.6762, 139.6503, "Tokyo", "Modern metropolis with rich culture");
        addMarker(40.7128, -74.0060, "New York", "The Big Apple - Statue of Liberty");
        addMarker(25.2048, 55.2708, "Dubai", "City of Gold - Burj Khalifa");
        addMarker(-33.8688, 151.2093, "Sydney", "Opera House and Harbour Bridge");
        addMarker(51.5074, -0.1278, "London", "Big Ben and Buckingham Palace");
        addMarker(41.9028, 12.4964, "Rome", "Colosseum and Vatican City");
        addMarker(13.7563, 100.5018, "Bangkok", "Grand Palace and vibrant street life");
        addMarker(3.2028, 73.2207, "Maldives", "Paradise islands with crystal clear water");

        mapView.invalidate();
    }

    private void addMarker(double lat, double lon, String title, String description) {
        GeoPoint point = new GeoPoint(lat, lon);
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(title);
        marker.setSnippet(description);

        marker.setOnMarkerClickListener((marker1, mapView1) -> {
            Toast.makeText(ExplorerActivity.this,
                    title + " - " + description,
                    Toast.LENGTH_SHORT).show();
            return true;
        });

        mapView.getOverlays().add(marker);
    }

    private void setupListeners() {
        mapToggleBtn.setOnClickListener(v -> toggleMapView());

        filterFab.setOnClickListener(v ->
                Toast.makeText(this, "Filter feature coming soon!", Toast.LENGTH_SHORT).show());

        findViewById(R.id.homeBtn).setOnClickListener(v -> {
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            finish();
        });

        findViewById(R.id.discoverBtn).setOnClickListener(v ->
                Toast.makeText(this, "You are in Discover", Toast.LENGTH_SHORT).show());

        findViewById(R.id.cartBtn).setOnClickListener(v -> {
            Toast.makeText(this, "Cart", Toast.LENGTH_SHORT).show();
            // Intent ke CartActivity
        });

        findViewById(R.id.profileBtn).setOnClickListener(v -> {
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
            // Intent ke ProfileActivity
        });
    }

    private void toggleMapView() {
        isMapVisible = !isMapVisible;
        if (isMapVisible) {
            mapContainer.setVisibility(View.VISIBLE);
            contentScrollView.setVisibility(View.GONE);
            Toast.makeText(this, "Map View - Explore destinations worldwide", Toast.LENGTH_SHORT).show();
        } else {
            mapContainer.setVisibility(View.GONE);
            contentScrollView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "List View", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myLocationOverlay != null) {
            myLocationOverlay.disableMyLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted!", Toast.LENGTH_SHORT).show();
                // Enable location overlay setelah permission diterima
                myLocationOverlay.enableMyLocation();
                mapView.invalidate();
            } else {
                Toast.makeText(this, "Location permission needed for map features", Toast.LENGTH_LONG).show();
            }
        }
    }
}
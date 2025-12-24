package com.example.travelapp.Activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager; // Pastikan import ini ada

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database=FirebaseDatabase.getInstance();
        Window window = getWindow();
        // Hapus baris 'Object WindowsManager;'

        // Gunakan kelas WindowManager secara langsung
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}

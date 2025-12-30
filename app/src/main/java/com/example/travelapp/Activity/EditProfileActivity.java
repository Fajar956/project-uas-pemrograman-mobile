package com.example.travelapp.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityEditProfileBinding;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding binding;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        calendar = Calendar.getInstance();

        setupToolbar();
        loadProfileData();
        setupClickListeners();
    }

    private void setupToolbar() {
        ImageView backBtn = findViewById(R.id.btnBack);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }
    }

    private void loadProfileData() {
        // Load existing profile data
        // You can get this from SharedPreferences, Database, or API
        binding.etFullName.setText("John Doe");
        binding.etEmail.setText("johndoe@email.com");
        binding.etPhone.setText("+62 812-3456-7890");
        binding.etDateOfBirth.setText("01/01/1990");
        binding.rbMale.setChecked(true);
        binding.etAddress.setText("Jl. Example Street No. 123, Jakarta");
    }

    private void setupClickListeners() {
        // Change Photo
        binding.btnChangePhoto.setOnClickListener(v -> {
            Toast.makeText(this, "Change photo feature coming soon!", Toast.LENGTH_SHORT).show();
            // You can implement image picker here
        });

        // Date of Birth Picker
        binding.etDateOfBirth.setOnClickListener(v -> showDatePicker());

        // Save Button
        binding.btnSave.setOnClickListener(v -> saveProfile());
    }

    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    binding.etDateOfBirth.setText(date);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void saveProfile() {
        String fullName = binding.etFullName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String dateOfBirth = binding.etDateOfBirth.getText().toString().trim();
        String address = binding.etAddress.getText().toString().trim();

        int selectedGenderId = binding.rgGender.getCheckedRadioButtonId();
        RadioButton selectedGender = findViewById(selectedGenderId);
        String gender = selectedGender != null ? selectedGender.getText().toString() : "";

        // Validation
        if (fullName.isEmpty()) {
            binding.etFullName.setError("Name is required");
            binding.etFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            binding.etEmail.setError("Email is required");
            binding.etEmail.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            binding.etPhone.setError("Phone is required");
            binding.etPhone.requestFocus();
            return;
        }

        // Save to SharedPreferences or Database or API
        // Example: Save to SharedPreferences
        // SharedPreferences prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);
        // SharedPreferences.Editor editor = prefs.edit();
        // editor.putString("fullName", fullName);
        // editor.putString("email", email);
        // editor.putString("phone", phone);
        // editor.putString("dateOfBirth", dateOfBirth);
        // editor.putString("gender", gender);
        // editor.putString("address", address);
        // editor.apply();

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
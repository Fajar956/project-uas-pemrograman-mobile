package com.example.travelapp.Model;

public class CartItem {
    private String title;
    private String location;
    private double price;
    private String imageUrl; // ✅ HARUS ADA

    // ✅ Konstruktor dengan 4 parameter
    public CartItem(String title, String location, double price, String imageUrl) {
        this.title = title;
        this.location = location;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // ✅ Getter
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; } // ✅ JANGAN LUPA INI
}
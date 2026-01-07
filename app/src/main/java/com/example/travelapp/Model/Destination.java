package com.example.travelapp.Model;

public class Destination {
    private String title;
    private String location;
    private float rating;
    private double price;
    private int imageResId;

    public Destination(String title, String location, float rating, double price, int imageResId) {
        this.title = title;
        this.location = location;
        this.rating = rating;
        this.price = price;
        this.imageResId = imageResId;
    }

    // Getter
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public float getRating() { return rating; }
    public double getPrice() { return price; }
    public int getImageResId() { return imageResId; }
}
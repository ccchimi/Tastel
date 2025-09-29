package com.app.tasteit;

public class Recipe {
    private String title;
    private String description;
    private int imageResId;
    private String cookingTime;

    public Recipe(String title, String description, int imageResId, String cookingTime) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
        this.cookingTime = cookingTime;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }
    public String getCookingTime() { return cookingTime; }
}
package com.app.tasteit;

public class Recipe {
    private String title;
    private String category;
    private String description;
    private String imageUrl;

    public Recipe(String title, String category, String description, String imageUrl) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
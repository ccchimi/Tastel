package com.app.tasteit;

public class Recipe {
    private String title;
    private String description;
    private String imageUrl;
    private String cookingTime;

    public Recipe(String title, String description, String imageUrl, String cookingTime) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.cookingTime = cookingTime;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getCookingTime() { return cookingTime; }

    // Setters (opcional si pensás modificarlos)
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCookingTime(String cookingTime) { this.cookingTime = cookingTime; }
}
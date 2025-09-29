package com.app.tasteit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    ImageView detailImage;
    TextView detailTitle, detailDescription;
    Button btnFavorite, btnBack;

    SharedPreferences sharedPrefs;
    Gson gson = new Gson();

    String recipeTitle, recipeDescription, recipeTime;
    int recipeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Referencias
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailDescription = findViewById(R.id.detailDescription);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnBack = findViewById(R.id.btnBack);

        sharedPrefs = getSharedPreferences("FavoritesPrefs", Context.MODE_PRIVATE);

        // Recuperar datos desde MainActivity
        recipeTitle = getIntent().getStringExtra("title");
        recipeDescription = getIntent().getStringExtra("description");
        recipeImage = getIntent().getIntExtra("image", R.drawable.tastel);
        recipeTime = getIntent().getStringExtra("time");
        if(recipeTime == null) recipeTime = "";

        // Mostrar datos
        detailTitle.setText(recipeTitle);
        detailDescription.setText(recipeDescription);
        detailImage.setImageResource(recipeImage);

        // Botón Volver
        btnBack.setOnClickListener(v -> finish());

        // Botón Favorito
        btnFavorite.setOnClickListener(v -> {
            String currentUser = LoginActivity.currentUser;
            if(currentUser == null) {
                Toast.makeText(this, "Debes iniciar sesión para guardar favoritos", Toast.LENGTH_SHORT).show();
                return;
            }

            String key = "favorites_" + currentUser;

            // Recuperar lista existente
            String json = sharedPrefs.getString(key, null);
            Type type = new TypeToken<List<Recipe>>() {}.getType();
            List<Recipe> favorites = json == null ? new ArrayList<>() : gson.fromJson(json, type);

            // Verificar duplicado
            boolean exists = false;
            for(Recipe r : favorites) {
                if(r.getTitle().equals(recipeTitle)) {
                    exists = true;
                    break;
                }
            }

            if(exists) {
                Toast.makeText(this, "Receta ya está en favoritos", Toast.LENGTH_SHORT).show();
            } else {
                favorites.add(new Recipe(recipeTitle, recipeDescription, recipeImage, recipeTime));
                String updatedJson = gson.toJson(favorites);
                sharedPrefs.edit().putString(key, updatedJson).apply();
                Toast.makeText(this, "Receta guardada en favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
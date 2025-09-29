package com.app.tasteit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
        String currentUser = LoginActivity.currentUser;
        if(currentUser == null) {
            btnFavorite.setOnClickListener(v ->
                    Toast.makeText(this, "Debes iniciar sesión para guardar favoritos", Toast.LENGTH_SHORT).show()
            );
        } else {
            String key = "favorites_" + currentUser;
            Type type = new TypeToken<List<Recipe>>() {}.getType();
            List<Recipe> favorites = new ArrayList<>();
            String json = sharedPrefs.getString(key, null);
            if(json != null) favorites = gson.fromJson(json, type);

            boolean[] isFavorite = {false};
            for(Recipe r : favorites) {
                if(r.getTitle().equals(recipeTitle)) {
                    isFavorite[0] = true;
                    break;
                }
            }

            btnFavorite.setText(isFavorite[0] ? "❌ Quitar de favoritos" : "⭐ Favorito");

            List<Recipe> finalFavorites = favorites;
            btnFavorite.setOnClickListener(v -> {
                List<Recipe> updatedFavorites = new ArrayList<>(finalFavorites);
                if(isFavorite[0]) {
                    // Quitar
                    updatedFavorites.removeIf(r -> r.getTitle().equals(recipeTitle));
                    Toast.makeText(this, "Receta eliminada de favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    // Agregar
                    updatedFavorites.add(new Recipe(recipeTitle, recipeDescription, recipeImage, recipeTime));
                    Toast.makeText(this, "Receta agregada a favoritos", Toast.LENGTH_SHORT).show();
                }

                sharedPrefs.edit().putString(key, gson.toJson(updatedFavorites)).apply();
                finalFavorites.clear();
                finalFavorites.addAll(updatedFavorites);

                isFavorite[0] = !isFavorite[0];
                btnFavorite.setText(isFavorite[0] ? "❌ Quitar de favoritos" : "⭐ Favorito");
            });
        }
    }
}
package com.app.tasteit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class RecipeDetailActivity extends AppCompatActivity {

    ImageView detailImage;
    TextView detailTitle, detailDescription;
    Button btnFavorite, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Referencias UI
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailDescription = findViewById(R.id.detailDescription);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnBack = findViewById(R.id.btnBack);

        // Recibir datos desde el intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Setear datos en la vista
        detailTitle.setText(title);
        detailDescription.setText(description);

        // Cargar imagen desde URL con Glide
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.tastel) // Imagen por defecto si falla la carga
                .into(detailImage);

        // Botón favorito
        btnFavorite.setOnClickListener(v -> {
            boolean isFavorite = btnFavorite.getText().toString().contains("⭐");
            if (isFavorite) {
                Toast.makeText(this, title + " agregada a favoritos ⭐", Toast.LENGTH_SHORT).show();
                btnFavorite.setText("★ En favoritos");
            } else {
                Toast.makeText(this, title + " eliminada de favoritos", Toast.LENGTH_SHORT).show();
                btnFavorite.setText("⭐ Favorito");
            }
        });

        // Botón volver
        btnBack.setOnClickListener(v -> finish());
    }
}
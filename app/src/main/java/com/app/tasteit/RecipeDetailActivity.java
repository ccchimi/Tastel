package com.app.tasteit;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecipeDetailActivity extends AppCompatActivity {

    ImageView detailImage;
    TextView detailTitle, detailDescription;
    Button btnFavorite, btnBack;

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

        // Recibimos los datos de la receta desde el intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        int imageRes = getIntent().getIntExtra("image", R.drawable.tastel);
        
        detailTitle.setText(title);
        detailDescription.setText(description);
        detailImage.setImageResource(imageRes);

        // Botón favorito
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            boolean isFavorite = false;

            @Override
            public void onClick(View v) {
                isFavorite = !isFavorite;
                if (isFavorite) {
                    Toast.makeText(RecipeDetailActivity.this, title + " agregada a favoritos ⭐", Toast.LENGTH_SHORT).show();
                    btnFavorite.setText("★ En favoritos");
                } else {
                    Toast.makeText(RecipeDetailActivity.this, title + " eliminada de favoritos", Toast.LENGTH_SHORT).show();
                    btnFavorite.setText("⭐ Favorito");
                }
            }
        });

        // Botón volver
        btnBack.setOnClickListener(v -> finish());
    }
}
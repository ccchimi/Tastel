package com.app.tasteit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etSearch;
    Button btnSearch, btnPasta, btnCarnes;
    LinearLayout categoriesLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias a elementos
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnPasta = findViewById(R.id.btnPasta);
        btnCarnes = findViewById(R.id.btnCarnes);
        categoriesLayout = findViewById(R.id.categoriesLayout);

        // Evento boton Buscar
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = etSearch.getText().toString().trim();
                if (query.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingresa una receta para buscar", Toast.LENGTH_SHORT).show();
                } else {
                    // Mostramos mensaje + agregamos dinámicamente un TextView
                    Toast.makeText(MainActivity.this, "Buscando: " + query, Toast.LENGTH_SHORT).show();

                    TextView newResult = new TextView(MainActivity.this);
                    newResult.setText("🔎 Resultado para: " + query);
                    newResult.setTextSize(16f);
                    newResult.setPadding(10, 10, 10, 10);

                    categoriesLayout.addView(newResult);
                }
            }
        });

        // Evento boton Pastas
        btnPasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Recetas de pastas", Toast.LENGTH_SHORT).show();
            }
        });

        // Evento boton Carnes
        btnCarnes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Recetas de carnes", Toast.LENGTH_SHORT).show();
            }
        });

        // Contenedor de recetas
        LinearLayout recipesContainer = findViewById(R.id.recipesContainer);

        // Datos simulados de recetas (prueba)
        String[][] recetas = {
                {"Spaghetti Bolognesa", "Clásica pasta italiana con salsa de carne y tomate.", "logo"},
                {"Pollo al horno", "Jugoso pollo al horno con especias.", "logo"},
                {"Ensalada César", "Ensalada fresca con pollo, crutones y aderezo.", "logo"},
                {"Tarta de verduras", "Masa casera rellena de vegetales salteados.", "logo"},
                {"Paella Valenciana", "Arroz con mariscos, pollo y vegetales al estilo español.", "logo"}
        };

        // Transformar a cards
        for (String[] receta : recetas) {
            View cardView = getLayoutInflater().inflate(R.layout.item_recipe, recipesContainer, false);

            TextView title = cardView.findViewById(R.id.recipeTitle);
            TextView description = cardView.findViewById(R.id.recipeDescription);
            ImageView image = cardView.findViewById(R.id.recipeImage);

            title.setText(receta[0]);
            description.setText(receta[1]);

            // Usamos un drawable por ahora
            int imageId = getResources().getIdentifier(receta[2], "drawable", getPackageName());
            image.setImageResource(imageId);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
                    intent.putExtra("title", receta[0]);
                    intent.putExtra("description", receta[1]);
                    intent.putExtra("image", imageId);
                    startActivity(intent);
                }
            });

            recipesContainer.addView(cardView);
        }
    }
}
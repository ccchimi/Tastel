package com.app.tastel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    EditText etSearch;
    Button btnSearch, btnPasta, btnCarnes;
    LinearLayout categoriesLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Eventos menú
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_recetas) {
                Toast.makeText(this, "Sección Recetas", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_comunidad) {
                Toast.makeText(this, "Sección Comunidad", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_listas) {
                Toast.makeText(this, "Sección Listas", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
            drawerLayout.closeDrawers();
            return true;
        });

        // Referencias UI
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnPasta = findViewById(R.id.btnPasta);
        btnCarnes = findViewById(R.id.btnCarnes);
        categoriesLayout = findViewById(R.id.categoriesLayout);

        // Evento Buscar
        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            if (query.isEmpty()) {
                Toast.makeText(MainActivity.this, "Ingresa una receta para buscar", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Buscando: " + query, Toast.LENGTH_SHORT).show();
                TextView newResult = new TextView(MainActivity.this);
                newResult.setText("🔎 Resultado para: " + query);
                newResult.setTextSize(16f);
                newResult.setPadding(10, 10, 10, 10);
                categoriesLayout.addView(newResult);
            }
        });

        // Botones categorías
        btnPasta.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Recetas de pastas", Toast.LENGTH_SHORT).show());
        btnCarnes.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Recetas de carnes", Toast.LENGTH_SHORT).show());

        // Lista dinámica de recetas
        LinearLayout recipesContainer = findViewById(R.id.recipesContainer);

        String[][] recetas = {
                {"Spaghetti Bolognesa", "Clásica pasta italiana con salsa de carne y tomate.", "logo"},
                {"Pollo al horno", "Jugoso pollo al horno con especias.", "logo"},
                {"Ensalada César", "Ensalada fresca con pollo, crutones y aderezo.", "logo"},
                {"Tarta de verduras", "Masa casera rellena de vegetales salteados.", "logo"},
                {"Paella Valenciana", "Arroz con mariscos, pollo y vegetales al estilo español.", "logo"}
        };

        for (String[] receta : recetas) {
            View cardView = getLayoutInflater().inflate(R.layout.item_recipe, recipesContainer, false);

            TextView title = cardView.findViewById(R.id.recipeTitle);
            TextView description = cardView.findViewById(R.id.recipeDescription);
            ImageView image = cardView.findViewById(R.id.recipeImage);

            title.setText(receta[0]);
            description.setText(receta[1]);

            int imageId = getResources().getIdentifier(receta[2], "drawable", getPackageName());
            image.setImageResource(imageId);

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
                intent.putExtra("title", receta[0]);
                intent.putExtra("description", receta[1]);
                intent.putExtra("image", imageId);
                startActivity(intent);
            });

            recipesContainer.addView(cardView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
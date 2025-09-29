package com.app.tasteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // 👈 agregado
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etSearch;
    Button btnSearch;
    ChipGroup categoriesRow;
    LinearLayout recipesContainer;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    // Datos: categorias y recetas
    private final String[] categories = {
            "Pastas", "Carnes", "Veggie", "Postres", "Sopas",
            "Arroces", "Ensaladas", "Pescados & Mariscos", "Tapas & Snacks", "Sin TACC"
    };

    // Cada receta: {titulo, categoria, descripcion, drawableName}
    private final String[][] recipesData = {
            {"Spaghetti Bolognesa","Pastas","Clásica pasta italiana con salsa de carne y tomate.","tastel"},
            {"Fettuccine Alfredo","Pastas","Crema, manteca y parmesano para una salsa sedosa.","tastel"},
            {"Lasagna de Verduras","Pastas","Capas de vegetales asados y bechamel.","tastel"},
            {"Pollo al horno","Carnes","Jugoso pollo al horno con especias.","tastel"},
            {"Asado clásico","Carnes","Costillar con chimichurri y fuego lento.","tastel"},
            {"Albóndigas en salsa","Carnes","Albóndigas caseras con tomate y hierbas.","tastel"},
            {"Ensalada César","Ensaladas","Lechuga, pollo, crutones y aderezo César.","tastel"},
            {"Ensalada Mediterránea","Ensaladas","Tomate, pepino, aceitunas y feta.","tastel"},
            {"Sopa de Calabaza","Sopas","Cremosa y especiada, ideal para otoño.","tastel"},
            {"Minestrone","Sopas","Sopa italiana de verduras y legumbres.","tastel"},
            {"Paella Valenciana","Arroces","Arroz con mariscos, pollo y vegetales.","tastel"},
            {"Risotto de Hongos","Arroces","Cremoso, con parmesano y hongos salteados.","tastel"},
            {"Tarta de Manzana","Postres","Masa hojaldrada y relleno de manzana.","tastel"},
            {"Brownie Chocolate","Postres","Intenso y húmedo, con nueces.","tastel"},
            {"Ceviche de Pescado","Pescados & Mariscos","Pescado marinado en cítricos.","tastel"},
            {"Salmón a la Plancha","Pescados & Mariscos","Salmón con limón y eneldo.","tastel"},
            {"Croquetas de Jamón","Tapas & Snacks","Crujientes por fuera, cremosas por dentro.","tastel"},
            {"Bruschetta Clásica","Tapas & Snacks","Tomate, ajo y albahaca sobre pan tostado.","tastel"},
            {"Pizza Sin TACC","Sin TACC","Base sin gluten con mozzarella y tomate.","tastel"},
            {"Galletas de Avena","Postres","Saludables y crujientes, con pasas.","tastel"}
    };

    // Map categoria -> lista de index de recetasData
    private Map<String, List<Integer>> categoryMap = new LinkedHashMap<>();

    // Categoria activa (filtro). null = mostrar todas
    private String activeCategory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar + Drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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

        // Referencias de UI
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        categoriesRow = findViewById(R.id.categoriesLayout);
        recipesContainer = findViewById(R.id.recipesContainer);

        // Construir el mapa categoria -> indices
        buildCategoryMap();

        // Crear botones de categorias en la fila superior
        createCategoryChips();

        // Mostrar todas las recetas al iniciar
        activeCategory = null;
        renderSections(null);

        // Buscar por texto
        btnSearch.setOnClickListener(v -> {
            String q = etSearch.getText().toString().trim().toLowerCase();
            if (q.isEmpty()) {
                renderSections(activeCategory);
                Toast.makeText(this, "Mostrando todas las recetas", Toast.LENGTH_SHORT).show();
            } else {
                renderSearchResults(q);
            }
        });
    }

    // Construye el mapa de categoria -> indices
    private void buildCategoryMap() {
        for (int i = 0; i < recipesData.length; i++) {
            String cat = recipesData[i][1];
            if (!categoryMap.containsKey(cat)) {
                categoryMap.put(cat, new ArrayList<>());
            }
            categoryMap.get(cat).add(i);
        }
        for (String cat : categories) {
            if (!categoryMap.containsKey(cat)) categoryMap.put(cat, new ArrayList<>());
        }
        Log.d("TASTEL", "buildCategoryMap: total categories in map = " + categoryMap.size());
    }

    // Crear chips de categoria dinámicamente
    private void createCategoryChips() {
        categoriesRow.removeAllViews();
        for (String cat : categories) {
            Chip chip = new Chip(this, null, R.style.Widget_Tastel_Chip);
            chip.setText(cat);
            chip.setCheckable(true);

            chip.setOnClickListener(v -> {
                if (chip.isChecked()) {
                    activeCategory = cat;
                    renderSections(cat);
                } else {
                    activeCategory = null;
                    renderSections(null);
                }
            });

            categoriesRow.addView(chip);
        }
    }

    // Marca visualmente el boton activo (simple)
    private void highlightCategoryButton(String categoryToHighlight) {
        int childCount = categoriesRow.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = categoriesRow.getChildAt(i);
            if (v instanceof Button) {
                Button b = (Button) v;
                if (b.getText().toString().equals(categoryToHighlight)) {
                    b.setBackgroundTintList(getResources().getColorStateList(R.color.primary));
                    b.setTextColor(getResources().getColor(R.color.white));
                } else {
                    b.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
                    b.setTextColor(getResources().getColor(R.color.textPrimary));
                }
            }
        }
    }

    // Renderiza todas las secciones (o sólo una categoría si cat != null)
    private void renderSections(String catFilter) {
        recipesContainer.removeAllViews();
        Log.d("TASTEL", "renderSections catFilter=" + catFilter);

        if (catFilter != null) {
            List<Integer> indices = categoryMap.get(catFilter);
            if (indices != null && !indices.isEmpty()) {
                addCategorySection(catFilter, indices);
            } else {
                TextView empty = new TextView(this);
                empty.setText("No hay recetas en esta categoría.");
                empty.setPadding(16, 16, 16, 16);
                recipesContainer.addView(empty);
            }
            return;
        }

        for (String cat : categories) {
            List<Integer> indices = categoryMap.get(cat);
            if (indices != null && !indices.isEmpty()) {
                addCategorySection(cat, indices);
            }
        }
    }

    // Render para búsqueda por query
    private void renderSearchResults(String q) {
        recipesContainer.removeAllViews();
        Map<String, List<Integer>> results = new LinkedHashMap<>();

        for (int i = 0; i < recipesData.length; i++) {
            String title = recipesData[i][0].toLowerCase();
            if (title.contains(q)) {
                String cat = recipesData[i][1];
                if (!results.containsKey(cat)) results.put(cat, new ArrayList<>());
                results.get(cat).add(i);
            }
        }

        if (results.isEmpty()) {
            TextView none = new TextView(this);
            none.setText("No se encontraron recetas para: " + q);
            none.setPadding(16, 16, 16, 16);
            recipesContainer.addView(none);
            return;
        }

        for (Map.Entry<String, List<Integer>> e : results.entrySet()) {
            addCategorySection(e.getKey(), e.getValue());
        }
    }

    // Agrega una sección (titulo + tarjetas de recetas)
    private void addCategorySection(String categoryTitle, List<Integer> indices) {
        Log.d("TASTEL", "addCategorySection: " + categoryTitle + " count=" + indices.size());

        TextView sectionTitle = new TextView(this);
        sectionTitle.setText(categoryTitle);
        sectionTitle.setTextSize(20f);
        sectionTitle.setPadding(8, 18, 8, 8);
        sectionTitle.setTextColor(getResources().getColor(R.color.textPrimary));
        recipesContainer.addView(sectionTitle);

        for (int idx : indices) {
            Log.d("TASTEL", "Adding recipe idx=" + idx + " title=" + recipesData[idx][0]);

            View cardView = getLayoutInflater().inflate(R.layout.item_recipe, recipesContainer, false);

            TextView title = cardView.findViewById(R.id.recipeTitle);
            TextView description = cardView.findViewById(R.id.recipeDescription);
            ImageView image = cardView.findViewById(R.id.recipeImage);

            title.setText(recipesData[idx][0]);
            description.setText(recipesData[idx][2]);

            int imageId = getResources().getIdentifier(recipesData[idx][3], "drawable", getPackageName());
            if (imageId == 0) imageId = R.mipmap.ic_launcher;
            image.setImageResource(imageId);

            final int finalIdx = idx;
            int finalImageId = imageId;
            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
                intent.putExtra("title", recipesData[finalIdx][0]);
                intent.putExtra("description", recipesData[finalIdx][2]);
                intent.putExtra("image", finalImageId);
                startActivity(intent);
            });

            recipesContainer.addView(cardView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle != null && toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.app.tasteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.tasteit.models.Recipe;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etSearch;
    Button btnSearch;
    LinearLayout categoriesRow;
    RecyclerView rvRecipes;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    // Datos: categorias y recetas
    private final String[] categories = {
            "Pastas", "Carnes", "Veggie", "Postres", "Sopas",
            "Arroces", "Ensaladas", "Pescados & Mariscos", "Tapas & Snacks", "Sin TACC"
    };

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

    private RecipeAdapter adapter;
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

        // Referencias
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        categoriesRow = findViewById(R.id.categoriesLayout);
        rvRecipes = findViewById(R.id.rvRecipes);

        // RecyclerView setup
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(this, getAllRecipes());
        rvRecipes.setAdapter(adapter);

        // Botones de categorías
        createCategoryButtons();

        // Buscar por texto
        btnSearch.setOnClickListener(v -> {
            String q = etSearch.getText().toString().trim().toLowerCase();
            if (q.isEmpty()) {
                adapter.setRecipes(getAllRecipes());
            } else {
                adapter.setRecipes(searchRecipes(q));
            }
        });
    }

    // Construir lista completa
    private List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        for (String[] data : recipesData) {
            list.add(new Recipe(data[0], data[1], data[2], data[3]));
        }
        return list;
    }

    // Buscar
    private List<Recipe> searchRecipes(String q) {
        List<Recipe> list = new ArrayList<>();
        for (String[] data : recipesData) {
            if (data[0].toLowerCase().contains(q)) {
                list.add(new Recipe(data[0], data[1], data[2], data[3]));
            }
        }
        return list;
    }

    // Filtro por categorías
    private void createCategoryButtons() {
        categoriesRow.removeAllViews();
        for (String cat : categories) {
            Button b = new Button(this);
            b.setText(cat);
            b.setAllCaps(false);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(12, 6, 12, 6);
            b.setLayoutParams(lp);

            b.setOnClickListener(v -> {
                if (cat.equals(activeCategory)) {
                    activeCategory = null;
                    adapter.setRecipes(getAllRecipes());
                } else {
                    activeCategory = cat;
                    adapter.setRecipes(filterByCategory(cat));
                }
            });

            categoriesRow.addView(b);
        }
    }

    private List<Recipe> filterByCategory(String cat) {
        List<Recipe> list = new ArrayList<>();
        for (String[] data : recipesData) {
            if (data[1].equals(cat)) {
                list.add(new Recipe(data[0], data[1], data[2], data[3]));
            }
        }
        return list;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle != null && toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
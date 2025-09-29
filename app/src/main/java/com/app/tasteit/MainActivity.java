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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

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

    // Categorías
    private final String[] categories = {
            "Pastas", "Carnes", "Veggie", "Postres", "Sopas",
            "Arroces", "Ensaladas", "Pescados & Mariscos", "Tapas & Snacks", "Sin TACC"
    };

    // Datos: título, categoría, descripción, imagen, tiempo
    private final Object[][] recipesData = {
            {"Spaghetti Bolognesa","Pastas","Clásica pasta italiana con salsa de carne y tomate.", R.drawable.tastel,"30 min"},
            {"Fettuccine Alfredo","Pastas","Crema, manteca y parmesano para una salsa sedosa.", R.drawable.tastel,"25 min"},
            {"Lasagna de Verduras","Pastas","Capas de vegetales asados y bechamel.", R.drawable.tastel,"40 min"},
            {"Pollo al horno","Carnes","Jugoso pollo al horno con especias.", R.drawable.tastel,"50 min"},
            {"Asado clásico","Carnes","Costillar con chimichurri y fuego lento.", R.drawable.tastel,"2 hs"},
            {"Albóndigas en salsa","Carnes","Albóndigas caseras con tomate y hierbas.", R.drawable.tastel,"45 min"},
            {"Ensalada César","Ensaladas","Lechuga, pollo, crutones y aderezo César.", R.drawable.tastel,"15 min"},
            {"Ensalada Mediterránea","Ensaladas","Tomate, pepino, aceitunas y feta.", R.drawable.tastel,"20 min"},
            {"Sopa de Calabaza","Sopas","Cremosa y especiada, ideal para otoño.", R.drawable.tastel,"35 min"},
            {"Minestrone","Sopas","Sopa italiana de verduras y legumbres.", R.drawable.tastel,"40 min"},
            {"Paella Valenciana","Arroces","Arroz con mariscos, pollo y vegetales.", R.drawable.tastel,"1h 15min"},
            {"Risotto de Hongos","Arroces","Cremoso, con parmesano y hongos salteados.", R.drawable.tastel,"50 min"},
            {"Tarta de Manzana","Postres","Masa hojaldrada y relleno de manzana.", R.drawable.tastel,"1h"},
            {"Brownie Chocolate","Postres","Intenso y húmedo, con nueces.", R.drawable.tastel,"45 min"},
            {"Ceviche de Pescado","Pescados & Mariscos","Pescado marinado en cítricos.", R.drawable.tastel,"25 min"},
            {"Salmón a la Plancha","Pescados & Mariscos","Salmón con limón y eneldo.", R.drawable.tastel,"20 min"},
            {"Croquetas de Jamón","Tapas & Snacks","Crujientes por fuera, cremosas por dentro.", R.drawable.tastel,"35 min"},
            {"Bruschetta Clásica","Tapas & Snacks","Tomate, ajo y albahaca sobre pan tostado.", R.drawable.tastel,"15 min"},
            {"Pizza Sin TACC","Sin TACC","Base sin gluten con mozzarella y tomate.", R.drawable.tastel,"40 min"},
            {"Galletas de Avena","Postres","Saludables y crujientes, con pasas.", R.drawable.tastel,"30 min"}
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
            } else if (id == R.id.nav_favoritos) {
                Toast.makeText(this, "Sección Favoritos / Listas", Toast.LENGTH_SHORT).show();
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

        // ICONO DE CUENTA - PopupMenu login/logout
        ImageView ivAccount = findViewById(R.id.ivAccount);
        ivAccount.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(this, ivAccount);
            menu.getMenu().add("Login");
            menu.getMenu().add("Logout");
            menu.setOnMenuItemClickListener(item -> {
                if(item.getTitle().equals("Login")) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else if(item.getTitle().equals("Logout")) {
                    Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }
                return true;
            });
            menu.show();
        });
    }

    // Construir lista completa
    private List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        for (Object[] data : recipesData) {
            list.add(new Recipe(
                    (String) data[0],   // título
                    (String) data[2],   // descripción
                    (int) data[3],      // drawable
                    (String) data[4]    // tiempo
            ));
        }
        return list;
    }

    // Buscar
    private List<Recipe> searchRecipes(String q) {
        List<Recipe> list = new ArrayList<>();
        for (Object[] data : recipesData) {
            if (((String) data[0]).toLowerCase().contains(q)) {
                list.add(new Recipe(
                        (String) data[0],
                        (String) data[2],
                        (int) data[3],
                        (String) data[4]
                ));
            }
        }
        return list;
    }

    // Filtro por categorías
    private List<Recipe> filterByCategory(String cat) {
        List<Recipe> list = new ArrayList<>();
        for (Object[] data : recipesData) {
            if (((String) data[1]).equals(cat)) {
                list.add(new Recipe(
                        (String) data[0],
                        (String) data[2],
                        (int) data[3],
                        (String) data[4]
                ));
            }
        }
        return list;
    }

    // Crear botones dinámicamente
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle != null && toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
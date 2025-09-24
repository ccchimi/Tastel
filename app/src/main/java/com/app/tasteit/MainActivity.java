package com.app.tasteit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        // Evento botón Buscar
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

        // Evento botón Pastas
        btnPasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Recetas de pastas", Toast.LENGTH_SHORT).show();
            }
        });

        // Evento botón Carnes
        btnCarnes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Recetas de carnes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
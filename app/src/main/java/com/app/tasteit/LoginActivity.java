package com.app.tasteit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnCreateUser;
    TextView tvForgot;

    // Usuario actualmente logueado
    public static String currentUser = null;

    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Referencias
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnCreateUser = findViewById(R.id.btnCreateUser);
        tvForgot = findViewById(R.id.tvForgot);

        // Inicializar SharedPreferences
        sharedPrefs = getSharedPreferences("UsersPrefs", Context.MODE_PRIVATE);

        // Recuperar sesión activa si existe
        currentUser = sharedPrefs.getString("currentUser", null);

        // Asegurar que exista usuario admin
        if(!sharedPrefs.contains("admin")) {
            sharedPrefs.edit().putString("admin", "1234").apply();
        }

        // LOGIN
        btnLogin.setOnClickListener(v -> {
            String user = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            String storedPass = sharedPrefs.getString(user, null);
            if(storedPass != null && storedPass.equals(pass)) {
                currentUser = user;
                sharedPrefs.edit().putString("currentUser", currentUser).apply(); // Guardar sesión
                Toast.makeText(LoginActivity.this, "Bienvenido " + user, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });

        // CREAR NUEVO USUARIO
        btnCreateUser.setOnClickListener(v -> {
            String user = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if(user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Usuario y contraseña no pueden estar vacíos", Toast.LENGTH_SHORT).show();
            } else if(sharedPrefs.contains(user)) {
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
            } else {
                sharedPrefs.edit().putString(user, pass).apply();
                Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        // OLVIDASTE CONTRASEÑA
        tvForgot.setOnClickListener(v -> {
            String user = etUsername.getText().toString().trim();
            if(sharedPrefs.contains(user)) {
                EditText input = new EditText(this);
                input.setHint("Nueva contraseña");

                new AlertDialog.Builder(this)
                        .setTitle("Cambiar contraseña")
                        .setMessage("Ingresa la nueva contraseña para " + user)
                        .setView(input)
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            String newPass = input.getText().toString().trim();
                            sharedPrefs.edit().putString(user, newPass).apply();
                            Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Opcional: cerrar sesión
    public static void logout(Context context, SharedPreferences prefs) {
        currentUser = null;
        prefs.edit().remove("currentUser").apply();
        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show();
    }
}
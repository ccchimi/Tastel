package com.app.tasteit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnCreateUser;
    TextView tvForgot;

    // HashMap de usuarios
    static Map<String, String> users = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnCreateUser = findViewById(R.id.btnCreateUser);
        tvForgot = findViewById(R.id.tvForgot);

        // Usuario admin predefinido
        if(users.isEmpty()) {
            users.put("admin", "1234");
        }

        // LOGIN
        btnLogin.setOnClickListener(v -> {
            String user = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if(users.containsKey(user) && users.get(user).equals(pass)) {
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
            } else if(users.containsKey(user)) {
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
            } else {
                users.put(user, pass);
                Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        // OLVIDASTE CONTRASEÑA
        tvForgot.setOnClickListener(v -> {
            String user = etUsername.getText().toString().trim();
            if(users.containsKey(user)) {
                EditText input = new EditText(this);
                input.setHint("Nueva contraseña");

                new AlertDialog.Builder(this)
                        .setTitle("Cambiar contraseña")
                        .setMessage("Ingresa la nueva contraseña para " + user)
                        .setView(input)
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            String newPass = input.getText().toString().trim();
                            users.put(user, newPass);
                            Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
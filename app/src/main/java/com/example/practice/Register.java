package com.example.practice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.practice.R;
import com.example.practice.DatabaseHelper;

class RegisterActivity extends AppCompatActivity {
    EditText nameInput, emailInput, passwordInput;
    Button registerButton, loginRedirect;
    RegisterManager registerManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        nameInput = findViewById(R.id.name);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        registerButton = findViewById(R.id.register);
        loginRedirect = findViewById(R.id.Login);

        registerManager = new RegisterManager(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameInput.setVisibility(View.VISIBLE);
                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (registerManager.registerUser(name, password, email)) {
                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameInput.setVisibility(View.GONE);
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    // ================= RegisterManager ===================
    public static class RegisterManager {
        private final DatabaseHelper dbHelper;

        public RegisterManager(Context context) {
            dbHelper = new DatabaseHelper(context);
        }

        public boolean registerUser(String name, String password, String email) {
            return dbHelper.addUser(name, password, email);
        }
    }
}
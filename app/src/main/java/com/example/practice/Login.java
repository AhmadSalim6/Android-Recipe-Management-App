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

import com.example.MyApplication.R;

 class LoginActivity extends AppCompatActivity {
    EditText emailInput, passwordInput , nameInput;
    Button loginButton, registerRedirect;
    LoginManager loginManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        nameInput = findViewById(R.id.name);
        loginButton = findViewById(R.id.Login);
        registerRedirect = findViewById(R.id.register);

        loginManager = new LoginManager(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (loginManager.loginUser(email, password)) {
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameInput.setVisibility(View.VISIBLE);
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}

// ================= LoginManager ===================
class LoginManager {
    private final DatabaseHelper dbHelper;

    public LoginManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean loginUser(String email, String password) {
        return dbHelper.checkEmailExists(email , password);
    }
}

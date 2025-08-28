package com.example.practice;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecipesListActivityAdmin extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_recipe_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseHelper recipes = new DatabaseHelper(getApplicationContext());
        Cursor cursor = recipes.fetchAll();

        final RecyclerView recyclerView = findViewById(R.id.recyclerView22);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set the adapter for the RecyclerView
        recyclerView.setAdapter(new RecipesAdapterAdmin(cursor));

        // Add intent to go to movie addition activity
        Button addRecipeBtn = findViewById(R.id.return_to_add_recipe_btn);
        addRecipeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminHome.class);
            startActivity(intent);
        });

    }


}
package com.example.practice;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecipesListActivityAdmin extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private RecipesAdapterAdmin adapter;

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

        dbHelper = new DatabaseHelper(getApplicationContext());
        System.out.println("LOLMOJHAMEDDUMB");
        Cursor cursor = dbHelper.fetchAll();
        System.out.println("LOLPAPDLPALD9999999999999999999");


        final RecyclerView recyclerView = findViewById(R.id.recyclerView22); // Fixed ID mismatch
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipesAdapterAdmin(cursor);
        recyclerView.setAdapter(adapter);


        Button addRecipeBtn = findViewById(R.id.return_to_add_recipe_btn);
        addRecipeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminHome.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.swapCursor(null); // Close cursor to prevent leaks
        }
    }
}
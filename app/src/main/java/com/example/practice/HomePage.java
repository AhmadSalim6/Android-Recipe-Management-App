package com.example.practice;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomePage extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {
    private RecyclerView categoryRecyclerView;
    private RecyclerView recipeRecyclerView;
    private CategoryAdapter categoryAdapter;
    private UserAdapter recipeAdapter;
    private DatabaseHelper dbHelper;
    private int userId = 1; // Hardcoded for testing; replace with actual user ID from login
    private String currentCategory = "All"; // Track current category

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(getApplicationContext());

        // Set up category RecyclerView
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Category> categoryList = dbHelper.getAllCategories();
        categoryAdapter = new CategoryAdapter(this, categoryList, this);
        categoryRecyclerView.setAdapter(categoryAdapter);

        // Set up recipe RecyclerView
        recipeRecyclerView = findViewById(R.id.recipeRecycler);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Cursor cursor = dbHelper.filterByCategory(currentCategory);
        recipeAdapter = new UserAdapter(this, cursor, userId);
        recipeRecyclerView.setAdapter(recipeAdapter);

        // Set up search bar
        EditText searchBar = findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                Cursor newCursor = dbHelper.searchBar(query);
                recipeAdapter.swapCursor(newCursor);
                currentCategory = "All"; // Reset category filter on search
                categoryAdapter.notifyDataSetChanged(); // Update selected state
            }
        });
    }

    @Override
    public void onCategoryClick(String category) {
        currentCategory = category;
        Cursor newCursor = dbHelper.filterByCategory(category);
        recipeAdapter.swapCursor(newCursor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recipeAdapter != null) {
            recipeAdapter.swapCursor(null); // Close cursor
        }
    }
}
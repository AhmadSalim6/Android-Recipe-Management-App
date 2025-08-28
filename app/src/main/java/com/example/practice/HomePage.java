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
    private int userId = 1; // Hardcoded; replace with actual user ID
    private String currentCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        dbHelper = new DatabaseHelper(getApplicationContext());

        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Category> categoryList = dbHelper.getAllCategories();
        categoryAdapter = new CategoryAdapter(this, categoryList, this);
        categoryRecyclerView.setAdapter(categoryAdapter);

        recipeRecyclerView = findViewById(R.id.recipeRecycler);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Cursor cursor = dbHelper.filterByCategory(currentCategory);
        recipeAdapter = new UserAdapter(this, cursor, userId);
        recipeRecyclerView.setAdapter(recipeAdapter);

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
                currentCategory = "All";
                categoryAdapter.notifyDataSetChanged();
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
            recipeAdapter.swapCursor(null);
        }
    }
}
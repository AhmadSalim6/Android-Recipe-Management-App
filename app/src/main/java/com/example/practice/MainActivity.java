package com.example.practice;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CategoryAdapter adapter;
    List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.categoryRecyclerView);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        // create category list
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Desserts", R.drawable.desserts));
        categoryList.add(new Category("Fast Food", R.drawable.fast_food));
        categoryList.add(new Category("Fast Food", R.drawable.fast_food));

        // set adapter
        adapter = new CategoryAdapter(this, categoryList);
        recyclerView.setAdapter(adapter);

    }

}

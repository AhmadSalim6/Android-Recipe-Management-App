package com.example.practice;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Recipe_Cards_List extends AppCompatActivity {

    private ArrayList<Recipe> recipeList;
    private Map<Integer, Float> recipeRatings;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_cards_list);

        // Back button
        ImageButton btnBack = findViewById(R.id.btn_back_to_home);
        btnBack.setOnClickListener(v -> finish());

        // RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.rv_recipes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipeList = new ArrayList<>();
        recipeRatings = new HashMap<>();
        dbHelper = new DatabaseHelper(this);



        // Get category from intent
        String category = getIntent().getStringExtra("category");
        TextView txtCategoryName = findViewById(R.id.txt_category_name);
        txtCategoryName.setText(category);
        loadRecipesByCategory(category);

        RecipesListAdapter recipes_adapter = new RecipesListAdapter(recipeList, recipeRatings, recipe -> {
            Intent intent = new Intent(Recipe_Cards_List.this, RecipeDetails.class);
            intent.putExtra("recipe", recipe);
            Float rating = recipeRatings.get(recipe.getId());
            if (rating != null) {
                intent.putExtra("avgRating", rating); // send the rating
            }
            startActivity(intent);
        });
        recyclerView.setAdapter(recipes_adapter);
    }

    private void loadRecipesByCategory(String category) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Load recipes for this category
        Cursor cursor = db.rawQuery(
                "SELECT id, name, image, category, ingredients, steps, cooking_time FROM Recipe WHERE category = ?",
                new String[]{category}
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String image = cursor.getString(3);
                String cat = cursor.getString(4);
                String ingredients = cursor.getString(5);
                String steps = cursor.getString(6);
                Integer cookingTime = cursor.isNull(7) ? null : cursor.getInt(7);

                Recipe recipe = new Recipe(id, name,  image, cat, ingredients, steps, cookingTime);
                recipeList.add(recipe);

                // Load average rating for this recipe
                Cursor ratingCursor = db.rawQuery(
                        "SELECT AVG(points) FROM Rating WHERE recipe_id = ?",
                        new String[]{String.valueOf(id)}
                );
                if (ratingCursor.moveToFirst()) {
                    float avg = ratingCursor.isNull(0) ? 0f : ratingCursor.getFloat(0);
                    recipeRatings.put(id, avg);
                }
                ratingCursor.close();
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }
}

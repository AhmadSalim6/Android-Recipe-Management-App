package com.example.practice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetails extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details_page);

        // Back button
        ImageButton btnBack = findViewById(R.id.btn_back_to_category);
        btnBack.setOnClickListener(v -> finish());

        // Get the Recipe from Intent
        Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        Float avgRating = getIntent().getFloatExtra("avgRating", 0f);

        if (recipe != null) {
            // Bind recipe data to views
            ImageView imageView = findViewById(R.id.img_item);
            TextView nameText = findViewById(R.id.txt_itemName);
            TextView rateText = findViewById(R.id.txt_itemRating);
            TextView cookingTimeText = findViewById(R.id.txt_itemPreparinTime);
            TextView ingredientsText = findViewById(R.id.txt_ingredients);
            TextView stepsText = findViewById(R.id.txt_steps);

            // Image
            @SuppressLint("DiscouragedApi")
            int resId = getResources().getIdentifier(recipe.getImage(), "drawable", getPackageName());
            if (resId != 0) {
                imageView.setImageResource(resId);
            }

            nameText.setText(recipe.getName());
            rateText.setText(String.valueOf(avgRating));

            cookingTimeText.setText(recipe.getCookingTime()+ " min");

            String ingredients = recipe.getIngredients();
            String[] ings = ingredients.split(",");
            StringBuilder builder = new StringBuilder();
            for (String i : ings) {
                builder.append("• ").append(i.trim()).append("\n");
            }
            ingredientsText.setText(builder.toString());

            String steps = recipe.getSteps();
            String[] stps = steps.split(",");
            builder = new StringBuilder();
            for (String s : stps) {
                builder.append("• ").append(s.trim()).append("\n");
            }
            stepsText.setText(builder.toString());
        }
    }
}

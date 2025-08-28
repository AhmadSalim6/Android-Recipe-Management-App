package com.example.practice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminHome extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(getApplicationContext());
        final EditText recipeName = findViewById(R.id.Name_et);
        final EditText recipeImage = findViewById(R.id.Image_et);
        final EditText recipeCategory = findViewById(R.id.Category_et);
        final EditText recipeIngredients = findViewById(R.id.Ingredients_et);
        final EditText recipeSteps = findViewById(R.id.Steps_et);
        final EditText recipeCookingTime = findViewById(R.id.Cooking_time_et);
        Button saveRecipe = findViewById(R.id.Add_recipe_btn);

        saveRecipe.setOnClickListener(v -> {
            try {
                String name = recipeName.getText().toString().trim();
                String image = recipeImage.getText().toString().trim();
                String category = recipeCategory.getText().toString().trim();
                String ingredients = recipeIngredients.getText().toString().trim();
                String steps = recipeSteps.getText().toString().trim();
                String cookingTimeStr = recipeCookingTime.getText().toString().trim();

                if (name.isEmpty() || ingredients.isEmpty() || steps.isEmpty() || cookingTimeStr.isEmpty()) {
                    Toast.makeText(AdminHome.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int cookingTime = Integer.parseInt(cookingTimeStr);
                dbHelper.createNew(name, image, category, ingredients, steps, cookingTime);
                Toast.makeText(AdminHome.this, "Recipe Added", Toast.LENGTH_SHORT).show();

                recipeName.setText("");
                recipeImage.setText("");
                recipeCategory.setText("");
                recipeIngredients.setText("");
                recipeSteps.setText("");
                recipeCookingTime.setText("");
            } catch (NumberFormatException e) {
                Toast.makeText(AdminHome.this, "Invalid cooking time", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(AdminHome.this, "Error adding recipe: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Button goToRecipesListBtn = findViewById(R.id.Show_recipes_btn);
        goToRecipesListBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecipesListActivityAdmin.class);
            startActivity(intent);
        });
    }
}
package com.example.practice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminHome extends AppCompatActivity {

    RecyclerView recyclerView;
    CategoryAdapter adapter;
    List<Category> categoryList;

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


        final DatabaseHelper newRecipe = new DatabaseHelper(getApplicationContext());
        final EditText recipeName = findViewById(R.id.Name_et);
        final EditText recipeUserID = findViewById(R.id.User_id_et);
        final EditText recipeImage = findViewById(R.id.Image_et);
        final EditText recipeCategory = findViewById(R.id.Category_et);
        final EditText recipeSteps = findViewById(R.id.Steps_et);
        final EditText recipeCookingTime = findViewById(R.id.Cooking_time_et);
        Button saveRecipe = findViewById(R.id.Add_recipe_btn);


        saveRecipe.setOnClickListener(v -> {
            newRecipe.createNew(recipeName.getText().toString(), Integer.parseInt(recipeUserID.getText().toString()),
                    recipeImage.getText().toString(), recipeCategory.getText().toString(),
                    recipeSteps.getText().toString(), Integer.parseInt(recipeCookingTime.getText().toString()));

            Toast.makeText(AdminHome.this, "Recipe Added", Toast.LENGTH_SHORT).show();
            recipeName.setText("");
            recipeUserID.setText("");
            recipeImage.setText("");
            recipeCategory.setText("");
            recipeSteps.setText("");
            recipeCookingTime.setText("");

        });

        // Add intent to save and go to movies list btn
        Button goToRecipesListBtn = findViewById(R.id.Show_recipes_btn);
        goToRecipesListBtn.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(this, RecipesListActivityAdmin.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.e("AdminHome", "Error starting RecipesListActivityAdmin: " + e.getMessage(), e);
                Toast.makeText(AdminHome.this, "Error opening recipes list: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}

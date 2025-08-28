package com.example.practice;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CategoryAdapter adapter;
    List<Category> categoryList;

    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        //Ahmed Code//
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        recyclerView = findViewById(R.id.categoryRecyclerView);
//
//        recyclerView.setLayoutManager(
//                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        );



        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new ProfileFragment())
                .commit();

        // create category list
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Desserts", R.drawable.desserts));
        categoryList.add(new Category("Fast Food", R.drawable.fast_food));
        categoryList.add(new Category("Fast Food", R.drawable.fast_food));

        // set adapter
//        adapter = new CategoryAdapter(this, categoryList);
//        recyclerView.setAdapter(adapter);
        //Ahmed Code//


        //Mohamed Code//
        dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        java.util.List<String> tables = loadTables(db);
        for (String table : tables) {
            Log.d("DB_TABLE", "Table Name: " + table);
        }

        java.util.List<User> users = loadUsers(db);
        for (User user : users) {
            Log.d("DB_USER", "User: " + user);
        }

        java.util.List<Recipe> recipes = loadRecipes(db);
        for (Recipe recipe : recipes) {
            Log.d("DB_RECIPE", "Recipe: " + recipe);
        }

        java.util.List<Favorite> favorites = loadFavorites(db);
        for (Favorite favorite : favorites) {
            Log.d("DB_FAVORITE", "Favorite: " + favorite);
        }

        java.util.List<Rating> ratings = loadRatings(db);
        for (Rating rating : ratings) {
            Log.d("DB_RATING", "Rating: " + rating);
        }

        insertSampleData(db);
        db.close();
        //Mohamed Code//
    }

    private java.util.List<String> loadTables(SQLiteDatabase db) {
        java.util.List<String> tables = new java.util.ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (cursor.moveToFirst()) {
            do {
                tables.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tables;
    }

    private java.util.List<User> loadUsers(SQLiteDatabase db) {
        java.util.List<User> users = new java.util.ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT id, name, password, email FROM Users", null);
        if (cursor.moveToFirst()) {
            do {
                users.add(new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    private java.util.List<Recipe> loadRecipes(SQLiteDatabase db) {
        java.util.List<Recipe> recipes = new java.util.ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT id, name, user_id, image, category, steps, cooking_time FROM Recipe", null);
        if (cursor.moveToFirst()) {
            do {
                recipes.add(new Recipe(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.isNull(6) ? null : cursor.getInt(6)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    private java.util.List<Favorite> loadFavorites(SQLiteDatabase db) {
        java.util.List<Favorite> favorites = new java.util.ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT recipe_id, user_id FROM Favorite", null);
        if (cursor.moveToFirst()) {
            do {
                favorites.add(new Favorite(cursor.getInt(0), cursor.getInt(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favorites;
    }

    private java.util.List<Rating> loadRatings(SQLiteDatabase db) {
        java.util.List<Rating> ratings = new java.util.ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT recipe_id, user_id, points FROM Rating", null);
        if (cursor.moveToFirst()) {
            do {
                ratings.add(new Rating(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ratings;
    }

    private void insertSampleData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("name", "NewUser");
        values.put("password", "newpass");
        values.put("email", "newuser@example.com");
        db.insert("Users", null, values);

        values.clear();
        values.put("name", "NewRecipe");
        values.put("user_id", 1);
        values.put("image", "new.jpg");
        values.put("category", "Test");
        values.put("steps", "Test steps");
        values.put("cooking_time", 20);
        db.insert("Recipe", null, values);
    }

}
package com.example.practice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CookingApp";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL)");
        db.execSQL("CREATE TABLE Recipe (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "image TEXT, " +
                "category TEXT, " +
                "ingredients TEXT NOT NULL, " +
                "steps TEXT NOT NULL, " +
                "cooking_time INTEGER)");
        db.execSQL("CREATE TABLE Favorite (" +
                "recipe_id INTEGER NOT NULL, " +
                "user_id INTEGER NOT NULL, " +
                "PRIMARY KEY (recipe_id, user_id), " +
                "FOREIGN KEY(recipe_id) REFERENCES Recipe(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(user_id) REFERENCES Users(id) ON DELETE CASCADE)");
        db.execSQL("CREATE TABLE Rating (" +
                "recipe_id INTEGER NOT NULL, " +
                "user_id INTEGER NOT NULL, " +
                "points INTEGER CHECK(points BETWEEN 1 AND 5), " +
                "PRIMARY KEY (recipe_id, user_id), " +
                "FOREIGN KEY(recipe_id) REFERENCES Recipe(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(user_id) REFERENCES Users(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Rating");
        db.execSQL("DROP TABLE IF EXISTS Favorite");
        db.execSQL("DROP TABLE IF EXISTS Recipe");
        db.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(db);
    }

    public void createNew(String name, String image, String category, String ingredients, String steps, int cooking_time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("image", image);
        row.put("category", category);
        row.put("ingredients", ingredients);
        row.put("steps", steps);
        row.put("cooking_time", cooking_time);
        db.insert("Recipe", null, row);
        db.close();
    }

    public void updateOne(int recipeId, String name, String image, String category, String ingredients, String steps, int cooking_time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("image", image);
        row.put("category", category);
        row.put("ingredients", ingredients);
        row.put("steps", steps);
        row.put("cooking_time", cooking_time);
        db.update("Recipe", row, "id = ?", new String[]{String.valueOf(recipeId)});
        db.close();
    }

    public void deleteOne(int recipeId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Recipe", "id = ?", new String[]{String.valueOf(recipeId)});
        db.close();
    }

    public Cursor fetchAll() {
        System.out.println("LOL1");
        SQLiteDatabase db = getReadableDatabase();
        System.out.println("LOL2");
        String[] rowDetails = {"id", "name", "image", "category", "ingredients", "steps", "cooking_time"};
        System.out.println("LOL3");
        Cursor cur= db.query("Recipe", rowDetails, null, null, null, null, null);
        System.out.println("LOL4");
        return cur;
    }

    public boolean addUser(String name, String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        long result = db.insert("Users", null, values);
        db.close();
        return result != -1;
    }

    public Cursor searchBar(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] rowDetails = {"id", "name", "image", "category", "ingredients", "steps", "cooking_time"};
        String selection = "name LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};
        return db.query("Recipe", rowDetails, selection, selectionArgs, null, null, null);
    }

    public float getAverageRating(int recipeId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT AVG(points) as avg_rating FROM Rating WHERE recipe_id = ?",
                new String[]{String.valueOf(recipeId)});
        float averageRating = 0;
        if (cursor.moveToFirst()) {
            averageRating = cursor.getFloat(cursor.getColumnIndexOrThrow("avg_rating"));
        }
        cursor.close();
        return averageRating;
    }

    public boolean isFavorite(int recipeId, int userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                "Favorite",
                new String[]{"recipe_id", "user_id"},
                "recipe_id = ? AND user_id = ?",
                new String[]{String.valueOf(recipeId), String.valueOf(userId)},
                null, null, null);
        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();
        return isFavorite;
    }

    public void addFavorite(int recipeId, int userId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("user_id", userId);
        db.insert("Favorite", null, values);
        db.close();
    }

    public void removeFavorite(int recipeId, int userId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Favorite", "recipe_id = ? AND user_id = ?", new String[]{String.valueOf(recipeId), String.valueOf(userId)});
        db.close();
    }

    public Cursor filterByCategory(String category) {
        SQLiteDatabase db = getReadableDatabase();
        String[] rowDetails = {"id", "name", "image", "category", "ingredients", "steps", "cooking_time"};
        String selection = category.equals("All") ? null : "category = ?";
        String[] selectionArgs = category.equals("All") ? null : new String[]{category};
        return db.query("Recipe", rowDetails, selection, selectionArgs, null, null, null);
    }

    public boolean checkEmailExists(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email = ? AND password = ?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("All", "all_category")); // Updated placeholder
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT DISTINCT category, MIN(image) as image FROM Recipe WHERE category IS NOT NULL GROUP BY category",
                null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String imageName = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                categories.add(new Category(categoryName, imageName));
            }
            cursor.close();
        }
        return categories;
    }
}
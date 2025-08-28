package com.example.practice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CookingApp";

    SQLiteDatabase CookingAppDB;
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT NOT NULL, " + "password TEXT NOT NULL, " + "email TEXT UNIQUE NOT NULL)");
        db.execSQL("CREATE TABLE Recipe (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT NOT NULL, " + "user_id INTEGER NOT NULL, " + "image TEXT, " + "category TEXT, " + "steps TEXT NOT NULL, " + "cooking_time INTEGER, " + "FOREIGN KEY(user_id) REFERENCES Users(id) ON DELETE CASCADE)");
        db.execSQL("CREATE TABLE Favorite (" + "recipe_id INTEGER NOT NULL, " + "user_id INTEGER NOT NULL, " + "PRIMARY KEY (recipe_id, user_id), " + "FOREIGN KEY(recipe_id) REFERENCES Recipe(id) ON DELETE CASCADE, " + "FOREIGN KEY(user_id) REFERENCES Users(id) ON DELETE CASCADE)");
        db.execSQL("CREATE TABLE Rating (" + "recipe_id INTEGER NOT NULL, " + "user_id INTEGER NOT NULL, " + "points INTEGER CHECK(points BETWEEN 1 AND 5), " + "PRIMARY KEY (recipe_id, user_id), " + "FOREIGN KEY(recipe_id) REFERENCES Recipe(id) ON DELETE CASCADE, " + "FOREIGN KEY(user_id) REFERENCES Users(id) ON DELETE CASCADE)");
        insertSeedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Rating");
        db.execSQL("DROP TABLE IF EXISTS Favorite");
        db.execSQL("DROP TABLE IF EXISTS Recipe");
        db.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(db);
    }

    public void createNew(String name,int user_id,String image,String category,String steps,int cooking_time) {
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("user_id", user_id);
        row.put("image", image);
        row.put("category", category);
        row.put("steps", steps);
        row.put("cooking_time", cooking_time);
        CookingAppDB = getWritableDatabase();
        // NullColumnHack here means that if the ContentValues is empty, the database will insert a row with NULL values.
        CookingAppDB.insert("Recipe", null, row);
        CookingAppDB.close();
    }

    public void updateOne(int recipeId, String name,int user_id,String image,String category,String steps,int cooking_time) {
        CookingAppDB = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("user_id", user_id);
        row.put("image", image);
        row.put("category", category);
        row.put("steps", steps);
        row.put("cooking_time", cooking_time);
        CookingAppDB.update("Recipe", row, "id='" + recipeId + "'", null);
        CookingAppDB.close();
    }

    public void deleteOne(int recipeId) {
        CookingAppDB = getWritableDatabase();
        CookingAppDB.delete("Recipe", "id='" + recipeId + "'", null);
        CookingAppDB.close();
    }

    private void insertSeedData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO Users (name, password, email) VALUES ('Ali', 'pass123', 'ali@example.com')");
        db.execSQL("INSERT INTO Users (name, password, email) VALUES ('Sara', 'pass456', 'sara@example.com')");
        db.execSQL("INSERT INTO Users (name, password, email) VALUES ('Omar', 'pass789', 'omar@example.com')");
        db.execSQL("INSERT INTO Users (name, password, email) VALUES ('Mona', 'pass111', 'mona@example.com')");
        db.execSQL("INSERT INTO Users (name, password, email) VALUES ('Hassan', 'pass222', 'hassan@example.com')");
        db.execSQL("INSERT INTO Recipe (name, user_id, image, category, steps, cooking_time) VALUES ('Chocolate Cake', 1, 'choco.jpg', 'Dessert', 'Mix and bake', 45)");
        db.execSQL("INSERT INTO Recipe (name, user_id, image, category, steps, cooking_time) VALUES ('Pasta Alfredo', 1, 'pasta.jpg', 'Main', 'Boil pasta and mix sauce', 30)");
        db.execSQL("INSERT INTO Recipe (name, user_id, image, category, steps, cooking_time) VALUES ('Salad', 2, 'salad.jpg', 'Appetizer', 'Chop veggies and mix', 15)");
        db.execSQL("INSERT INTO Recipe (name, user_id, image, category, steps, cooking_time) VALUES ('Grilled Chicken', 3, 'chicken.jpg', 'Main', 'Grill chicken with spices', 60)");
        db.execSQL("INSERT INTO Recipe (name, user_id, image, category, steps, cooking_time) VALUES ('Soup', 3, 'soup.jpg', 'Starter', 'Boil veggies and blend', 40)");
        db.execSQL("INSERT INTO Recipe (name, user_id, image, category, steps, cooking_time) VALUES ('Pizza', 4, 'pizza.jpg', 'Main', 'Prepare dough and bake', 50)");
        db.execSQL("INSERT INTO Recipe (name, user_id, image, category, steps, cooking_time) VALUES ('Ice Cream', 5, 'icecream.jpg', 'Dessert', 'Freeze milk and sugar', 120)");
        db.execSQL("INSERT INTO Favorite (recipe_id, user_id) VALUES (1, 2)");
        db.execSQL("INSERT INTO Favorite (recipe_id, user_id) VALUES (3, 1)");
        db.execSQL("INSERT INTO Favorite (recipe_id, user_id) VALUES (4, 5)");
        db.execSQL("INSERT INTO Favorite (recipe_id, user_id) VALUES (6, 3)");
        db.execSQL("INSERT INTO Favorite (recipe_id, user_id) VALUES (7, 4)");
        db.execSQL("INSERT INTO Rating (recipe_id, user_id, points) VALUES (1, 2, 5)");
        db.execSQL("INSERT INTO Rating (recipe_id, user_id, points) VALUES (3, 1, 4)");
        db.execSQL("INSERT INTO Rating (recipe_id, user_id, points) VALUES (4, 5, 5)");
        db.execSQL("INSERT INTO Rating (recipe_id, user_id, points) VALUES (6, 3, 3)");
        db.execSQL("INSERT INTO Rating (recipe_id, user_id, points) VALUES (7, 4, 4)");
    }

    public Cursor fetchAll() {
        CookingAppDB = getReadableDatabase();
        String[] rowDetails = {"id","name", "user_id", "image", "category", "steps", "cooking_time"};
        Cursor cursor = CookingAppDB.query("Recipe", rowDetails, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        System.out.println("LOL99");

        // Do not close the database here, the cursor needs it to be open.
        return cursor;
    }


    public Cursor searchBar(String query) {
        CookingAppDB = getReadableDatabase();
        String[] rowDetails = {"id", "name", "user_id", "image", "category", "steps", "cooking_time"};
        String selection = "name LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};
        Cursor cursor = CookingAppDB.query("Recipe", rowDetails, selection, selectionArgs, null, null, null);
        return cursor;
    }

    public float getAverageRating(int recipeId) {
        CookingAppDB = getReadableDatabase();
        Cursor cursor = CookingAppDB.rawQuery(
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
        CookingAppDB = getReadableDatabase();
        Cursor cursor = CookingAppDB.query(
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
        CookingAppDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("user_id", userId);
        CookingAppDB.insert("Favorite", null, values);
        CookingAppDB.close();
    }

    public void removeFavorite(int recipeId, int userId) {
        CookingAppDB = getWritableDatabase();
        CookingAppDB.delete("Favorite",
                "recipe_id = ? AND user_id = ?",
                new String[]{String.valueOf(recipeId), String.valueOf(userId)});
        CookingAppDB.close();
    }

    public Cursor filterByCategory(String category) {
        CookingAppDB = getReadableDatabase();
        String[] rowDetails = {"id", "name", "user_id", "image", "category", "steps", "cooking_time"};
        String selection = category.equals("All") ? null : "category = ?";
        String[] selectionArgs = category.equals("All") ? null : new String[]{category};
        Cursor cursor = CookingAppDB.query("Recipe", rowDetails, selection, selectionArgs, null, null, null);
        return cursor;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("All", "ic_launcher_foreground")); // Default "All" category
        CookingAppDB = getReadableDatabase();
        Cursor cursor = CookingAppDB.rawQuery(
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

package com.example.practice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CookingApp.db";
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
}
public boolean addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);

        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        boolean valid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return valid;
    }
}

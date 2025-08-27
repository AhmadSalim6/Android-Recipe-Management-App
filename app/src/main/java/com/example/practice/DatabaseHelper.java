package com.example.practice;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CookingApp.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT NOT NULL, " + "password TEXT NOT NULL, " + "email TEXT UNIQUE NOT NULL)");
        db.execSQL("CREATE TABLE Recipe (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT NOT NULL, " + "user_id INTEGER NOT NULL, " + "image TEXT, " + "category TEXT, " + "ingredients TEXT NOT NULL, " + "steps TEXT NOT NULL, " + "cooking_time INTEGER, " + "FOREIGN KEY(user_id) REFERENCES Users(id) ON DELETE CASCADE)");
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
        db.execSQL("INSERT INTO Recipe (name, user_id, image, category, ingredients, steps, cooking_time) VALUES ('Chocolate Cake', 1, 'choco', 'Dessert', '1½ cups flour,1 cup sugar,½ cup cocoa powder,1 tsp baking powder,1 tsp baking soda,½ tsp salt,2 eggs,1 cup milk,1 tsp vanilla extract', 'Preheat oven to 180°C (350°F),Mix dry ingredients,Add wet ingredients,Mix well,Pour into greased baking pan,Bake for 30–35 min,check with a toothpick', 45)");
        db.execSQL("INSERT INTO Recipe (name, user_id, image, category, ingredients, steps, cooking_time) VALUES ('Pasta Alfredo', 1, 'pasta', 'Main Dish', '250g fettuccine pasta,2 tbsp butter,1 cup heavy cream,½ cup grated Parmesan cheese,2 cloves garlic,minced,Salt & pepper to taste', 'Boil pasta in salted water until al dente,In a pan melt butter and sauté garlic,Add cream,simmer 3–4 min,Stir in Parmesan until smooth,Toss pasta in the sauce', 30)");
        db.execSQL("INSERT INTO Recipe (name, user_id, image, category, ingredients, steps, cooking_time) VALUES ('Salad', 2, 'salad', 'Appetizers', '1 cucumber,2 tomatoes,½ red onion,1 carrot,2 tbsp olive oil,1 tbsp lemon juice,Salt & pepper', 'Wash and chop all vegetables,Place in a bowl,Drizzle with olive oil and lemon juice,Add salt and pepper,toss well', 15)");
        db.execSQL("INSERT INTO Recipe (name, user_id, image, category, ingredients, steps, cooking_time) VALUES ('Grilled Chicken', 3, 'chicken', 'Main', '1 whole chicken (or 4 thighs),2 tbsp olive oil,2 tsp paprika,1 tsp garlic powder,1 tsp black pepper,1 tsp salt,1 tsp cumin', 'Clean chicken,pat dry,Mix spices with olive oil then rub on chicken,Preheat grill to medium-high heat,Grill chicken 25–30 min per side (until cooked inside)', 60)");
        db.execSQL("INSERT INTO Recipe (name, user_id, image, category, ingredients, steps, cooking_time) VALUES ('Soup', 3, 'soup', 'Appetizers', '2 carrots,2 potatoes,1 onion,2 celery sticks,3 cups vegetable broth,Salt & pepper', 'Chop vegetables,Heat pot with onion and celery,sauté 2–3 min,Add carrots and potatoes,stir,Pour in broth,bring to boil,Simmer 20–25 min until soft,Blend with hand blender for creamy soup', 40)");
        db.execSQL("INSERT INTO Recipe (name, user_id, image, category, ingredients, steps, cooking_time) VALUES ('Pizza', 4, 'pizza', 'Main Dish', '2 cups flour,1 tsp yeast,½ cup warm water,1 tbsp olive oil,½ cup tomato sauce,1 cup shredded mozzarella,Toppings (pepperoni, veggies, etc.)', 'Mix flour,yeast,water,oil then knead dough,Let dough rise 1 hr,Roll out dough on tray,Spread tomato sauce,Add cheese and toppings,Bake at 220°C (425°F) for 15–20 min', 50)");
        db.execSQL("INSERT INTO Recipe (name, user_id, image, category, ingredients, steps, cooking_time) VALUES ('Ice Cream', 5, 'icecream', 'Dessert', '2 cups milk,1 cup heavy cream,¾ cup sugar,1 tsp vanilla extract', 'Heat milk and cream until warm,Stir in sugar until dissolved,Add vanilla,mix well,Cool in fridge for 2 hrs,Churn in ice cream maker (or freeze and stir every 30 min),Freeze until solid', 120)");
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

   public boolean checkEmailExists(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND password=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
}

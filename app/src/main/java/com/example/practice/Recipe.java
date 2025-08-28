package com.example.practice;
import java.io.Serializable;

public class Recipe implements Serializable {
    private int id;
    private String name;
    private String image;
    private String category;
    private String ingredients;
    private String steps;
    private Integer cookingTime;

    public Recipe(int id, String name, String image, String category, String ingredients, String steps, Integer cookingTime) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.category = category;
        this.ingredients = ingredients;
        this.steps = steps;
        this.cookingTime = cookingTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }


}

package com.example.practice;

class Recipe {
    private int id;
    private String name;
    private int userId;
    private String image;
    private String category;
    private String steps;
    private Integer cookingTime;

    public Recipe(int id, String name, int userId, String image, String category, String steps, Integer cookingTime) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.image = image;
        this.category = category;
        this.steps = steps;
        this.cookingTime = cookingTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUserId() {
        return userId;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String getSteps() {
        return steps;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    @Override
    public String toString() {
        return "Recipe{id=" + id + ", name='" + name + "', userId=" + userId + "}";
    }
}

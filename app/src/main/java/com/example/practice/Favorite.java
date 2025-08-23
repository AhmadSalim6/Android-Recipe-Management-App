package com.example.practice;

class Favorite {
    private int recipeId;
    private int userId;

    public Favorite(int recipeId, int userId) {
        this.recipeId = recipeId;
        this.userId = userId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Favorite{recipeId=" + recipeId + ", userId=" + userId + "}";
    }
}

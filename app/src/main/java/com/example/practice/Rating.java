package com.example.practice;

class Rating {
    private int recipeId;
    private int userId;
    private int points;

    public Rating(int recipeId, int userId, int points) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.points = points;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "Rating{recipeId=" + recipeId + ", userId=" + userId + ", points=" + points + "}";
    }
}

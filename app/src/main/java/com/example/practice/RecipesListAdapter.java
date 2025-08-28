package com.example.practice;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipeViewHolder>{

    private final ArrayList<Recipe> recipeList;
    private Map<Integer, Float> recipeRatings;
    private OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipesListAdapter(ArrayList<Recipe> recipeList, Map<Integer, Float> recipeRatings, OnRecipeClickListener listener) {
        this.recipeList = recipeList;
        this.recipeRatings = recipeRatings;
        this.listener = listener;
    }
    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    @NonNull
    @Override
    public RecipesListAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        //fav
        @SuppressLint("DiscouragedApi")
        int resId = holder.itemView.getContext().getResources()
                .getIdentifier(recipe.getImage(), "drawable",
                        holder.itemView.getContext().getPackageName());

        holder.recipeImg.setImageResource(resId);
        holder.recipeName.setText(recipe.getName());
        holder.recipePrepareTime.setText(
                (recipe.getCookingTime() != null ? recipe.getCookingTime() + " min" : "-")
        );
        Float avgRating = recipeRatings.get(recipe.getId());
        holder.recipeRating.setText(String.format(String.valueOf(avgRating)));

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onRecipeClick(recipe);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateRatings(Map<Integer, Float> newRatings) {
        this.recipeRatings = newRatings;
        notifyDataSetChanged();
    }


    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageButton fav;
        ImageView recipeImg;
        TextView recipeName, recipePrepareTime, recipeRating;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            fav = itemView.findViewById(R.id.btn_fav);
            recipeImg = itemView.findViewById(R.id.img_recipe);
            recipeName = itemView.findViewById(R.id.txt_recipeName);
            recipePrepareTime = itemView.findViewById(R.id.txt_prepareTime);
            recipeRating = itemView.findViewById(R.id.txt_recipeRating);
        }
    }
}

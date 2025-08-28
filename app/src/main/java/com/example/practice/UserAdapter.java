package com.example.practice;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Cursor cursor;
    private final Context context;
    private final int userId; // Current user's ID, passed from HomePage

    public UserAdapter(Context context, Cursor cursor, int userId) {
        this.context = context;
        this.cursor = cursor;
        this.userId = userId;
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cursor == null || !cursor.moveToPosition(position)) {
            return;
        }

        // Retrieve data from the Cursor
        int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String recipeCategory = cursor.getString(cursor.getColumnIndexOrThrow("category"));
        String recipeName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String recipeImage = cursor.getString(cursor.getColumnIndexOrThrow("image"));
        int cookingTime = cursor.getInt(cursor.getColumnIndexOrThrow("cooking_time"));

        // Bind data to ViewHolder
        holder.textViewName.setText(recipeName);
        holder.textViewCookingTime.setText(cookingTime + " min");

        // Load image
        int imageResId = context.getResources().getIdentifier(
                recipeImage, "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.imageView.setImageResource(imageResId);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_foreground); // Placeholder
        }

        System.out.println("LOLKAKA");

        // Calculate and set average rating
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        float averageRating = dbHelper.getAverageRating(recipeId);
        holder.textViewRating.setText(String.format("%.1f", averageRating));

        System.out.println("LOLKKAKAKKA");

        // Set favorite button state
        boolean isFavorite = dbHelper.isFavorite(recipeId, userId);
        holder.favoriteButton.setImageResource(isFavorite ?
                R.drawable.heart_filled : R.drawable.heart); // Assuming heart_filled is a filled heart icon
    }

    // Method to update cursor and refresh data
    public void swapCursor(Cursor newCursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewCookingTime;
        public TextView textViewRating;
        public ImageView imageView;
        public ImageButton favoriteButton;
        public ImageButton moreInfoButton;
        private final UserAdapter adapter;

        public ViewHolder(View view, UserAdapter adapter) {
            super(view);
            this.adapter = adapter;

            textViewName = view.findViewById(R.id.name_tv); // Update ID in recipe_card.xml
            textViewCookingTime = view.findViewById(R.id.cooking_time_tv); // Update ID in recipe_card.xml
            textViewRating = view.findViewById(R.id.rating_tv); // Update ID in recipe_card.xml
            imageView = view.findViewById(R.id.image_ib); // Update ID in recipe_card.xml
            favoriteButton = view.findViewById(R.id.favourite_ib); // Update ID in recipe_card.xml
            moreInfoButton = view.findViewById(R.id.more_info_ib); // Update ID in recipe_card.xml

            DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());

            favoriteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && adapter.cursor.moveToPosition(position)) {
                    int recipeId = adapter.cursor.getInt(adapter.cursor.getColumnIndexOrThrow("id"));
                    boolean isFavorite = dbHelper.isFavorite(recipeId, adapter.userId);
                    if (isFavorite) {
                        dbHelper.removeFavorite(recipeId, adapter.userId);
                        favoriteButton.setImageResource(R.drawable.heart);
                        Toast.makeText(view.getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.addFavorite(recipeId, adapter.userId);
                        favoriteButton.setImageResource(R.drawable.heart_filled);
                        Toast.makeText(view.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            moreInfoButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && adapter.cursor.moveToPosition(position)) {
                    int recipeId = adapter.cursor.getInt(adapter.cursor.getColumnIndexOrThrow("id"));
                    Intent intent = new Intent(view.getContext(), RecipeDetailsActivity.class);
                    intent.putExtra("RECIPE_ID", recipeId);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
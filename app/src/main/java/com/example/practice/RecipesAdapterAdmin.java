package com.example.practice;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipesAdapterAdmin extends RecyclerView.Adapter<com.example.practice.RecipesAdapterAdmin.ViewHolder> {
    public Cursor cursor;

    public RecipesAdapterAdmin(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card_admin, parent, false);
        return new ViewHolder(view, this);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        String recipeName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        int recipeUserID = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
        String recipeImage = cursor.getString(cursor.getColumnIndexOrThrow("image"));
        String recipeCategory = cursor.getString(cursor.getColumnIndexOrThrow("category"));
        String recipeSteps = cursor.getString(cursor.getColumnIndexOrThrow("steps"));
        int recipeCookingTime = cursor.getInt(cursor.getColumnIndexOrThrow("cooking_time"));

        holder.editTextName.setText(recipeName);
        holder.editTextUserID.setText(String.valueOf(recipeUserID));

        int imageResId = holder.itemView.getContext().getResources().getIdentifier(
                recipeImage, "drawable", holder.itemView.getContext().getPackageName());
        if (imageResId != 0) {
            holder.editTextImage.setImageResource(imageResId);
        } else {
            holder.editTextImage.setImageResource(R.drawable.ic_launcher_foreground); // Example placeholder
        }
        holder.imageFileNameTextView.setText(recipeImage); // Set the image file name in the TextView

        holder.editTextCategory.setText(recipeCategory);
        holder.editTextSteps.setText(recipeSteps);
        holder.editTextCookingTime.setText(String.valueOf(recipeCookingTime));
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText editTextName;
        public EditText editTextUserID;
        public ImageView editTextImage;
        public TextView imageFileNameTextView;
        public EditText editTextCategory;
        public EditText editTextSteps;
        public EditText editTextCookingTime;

        public final Button updateBtn;
        public final Button deleteBtn;
        com.example.practice.RecipesAdapterAdmin recyclerAdapter;

        public ViewHolder(View view, com.example.practice.RecipesAdapterAdmin recyclerAdapter) {
            super(view);

            editTextName = view.findViewById(R.id.Name_et);
            editTextUserID = view.findViewById(R.id.User_id_et);
            editTextImage = view.findViewById(R.id.Image_et);
            imageFileNameTextView = view.findViewById(R.id.image_name_tv);
            editTextCategory = view.findViewById(R.id.Category_et);
            editTextSteps = view.findViewById(R.id.Steps_et);
            editTextCookingTime = view.findViewById(R.id.Cooking_time_et);

            updateBtn = view.findViewById(R.id.update_btn);
            deleteBtn = view.findViewById(R.id.delete_btn);

            // Set the recyclerAdapter to the ViewHolder
            this.recyclerAdapter = recyclerAdapter;

            // Clear focus from the EditText
            editTextName.clearFocus();
            editTextUserID.clearFocus();
            editTextImage.clearFocus();
            editTextCategory.clearFocus();
            editTextSteps.clearFocus();
            editTextCookingTime.clearFocus();
            imageFileNameTextView.clearFocus();

            DatabaseHelper recipes = new DatabaseHelper(itemView.getContext());

            updateBtn.setOnClickListener(v -> {

                int recipePosition = getAdapterPosition();
                Cursor cursor = recyclerAdapter.cursor;

                if (recipePosition != RecyclerView.NO_POSITION && cursor.moveToPosition(recipePosition)) {

                    // Get the movieId from the Cursor
                    int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String newRecipe = editTextName.getText().toString();
                    int newUserID = Integer.parseInt(editTextUserID.getText().toString());
                    String newImage = imageFileNameTextView.getText().toString(); // Get image name from TextView

                    String newCategory = editTextCategory.getText().toString();
                    String newSteps = editTextSteps.getText().toString();
                    int newCookingTime = Integer.parseInt(editTextCookingTime.getText().toString());
                    recipes.updateOne(recipeId, newRecipe , newUserID, newImage, newCategory, newSteps, newCookingTime);

                    //Update data in the Cursor
                    recyclerAdapter.cursor = recipes.fetchAll();
                    //Notify the adapter that the data has changed to update the RecyclerView
                    recyclerAdapter.notifyItemChanged(recipePosition);

                    Toast.makeText(view.getContext(), "Recipe Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Recipe Not Updated", Toast.LENGTH_SHORT).show();
                }
            });

            deleteBtn.setOnClickListener(v -> {
                int recipePosition = getAdapterPosition();
                Cursor cursor = recyclerAdapter.cursor;

                if (recipePosition != RecyclerView.NO_POSITION && cursor.moveToPosition(recipePosition)) {
                    // Get the movieId from the Cursor
                    int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    recipes.deleteOne(recipeId);

                    recyclerAdapter.cursor = recipes.fetchAll();
                    recyclerAdapter.notifyItemRemoved(recipePosition);
                    recyclerAdapter.notifyItemRangeChanged(recipePosition, recyclerAdapter.getItemCount());

                    Toast.makeText(view.getContext(), "Recipe Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Recipe Not Deleted", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

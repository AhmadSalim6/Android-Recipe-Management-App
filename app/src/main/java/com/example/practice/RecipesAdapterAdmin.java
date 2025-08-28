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

public class RecipesAdapterAdmin extends RecyclerView.Adapter<RecipesAdapterAdmin.ViewHolder> {
    private Cursor cursor;

    public RecipesAdapterAdmin(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("LOLPAPDLPALD111111111111111111");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card_admin, parent, false);

        System.out.println("LOLPAPDDq32432222222222222222222222222");

        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cursor == null || !cursor.moveToPosition(position)) {
            return;
        }

        System.out.println("LOLPAPDLPALD67676767676767");

        int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String recipeName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String recipeImage = cursor.getString(cursor.getColumnIndexOrThrow("image"));
        String recipeCategory = cursor.getString(cursor.getColumnIndexOrThrow("category"));
        String recipeIngredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
        String recipeSteps = cursor.getString(cursor.getColumnIndexOrThrow("steps"));
        int recipeCookingTime = cursor.getInt(cursor.getColumnIndexOrThrow("cooking_time"));

        System.out.println("LOLPAPDLPALDdslfkpsdkfpodskf");


        holder.editTextName.setText(recipeName);
        holder.editTextCategory.setText(recipeCategory);
        holder.editTextIngredients.setText(recipeIngredients);
        holder.editTextSteps.setText(recipeSteps);
        holder.editTextCookingTime.setText(String.valueOf(recipeCookingTime));

        int imageResId = holder.itemView.getContext().getResources().getIdentifier(
                recipeImage, "drawable", holder.itemView.getContext().getPackageName());
        if (imageResId != 0) {
            holder.editTextImage.setImageResource(imageResId);
        } else {
            holder.editTextImage.setImageResource(R.drawable.ic_launcher_foreground);
        }
        holder.imageFileNameTextView.setText(recipeImage);
        holder.recipeId = recipeId;

        holder.editTextName.clearFocus();
        holder.editTextCategory.clearFocus();
        holder.editTextIngredients.clearFocus();
        holder.editTextSteps.clearFocus();
        holder.editTextCookingTime.clearFocus();
        holder.imageFileNameTextView.clearFocus();
    }

    public void swapCursor(Cursor newCursor) {

        System.out.println("LOL3ady");

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText editTextName;
        public ImageView editTextImage;
        public EditText imageFileNameTextView;
        public EditText editTextCategory;
        public EditText editTextIngredients;
        public EditText editTextSteps;
        public EditText editTextCookingTime;
        public Button updateBtn;
        public Button deleteBtn;
        public int recipeId;
        private final RecipesAdapterAdmin recyclerAdapter;

        public ViewHolder(View view, RecipesAdapterAdmin recyclerAdapter) {
            super(view);
            this.recyclerAdapter = recyclerAdapter;


            System.out.println("LOLANA HENA");
            editTextName = view.findViewById(R.id.Name_et);
            editTextImage = view.findViewById(R.id.Image_et);
            imageFileNameTextView = view.findViewById(R.id.image_name_et); // Assuming this ID from xml
            editTextCategory = view.findViewById(R.id.Category_et);
            editTextIngredients = view.findViewById(R.id.Ingredients_et);
            editTextSteps = view.findViewById(R.id.Steps_et);
            editTextCookingTime = view.findViewById(R.id.Cooking_time_et);
            updateBtn = view.findViewById(R.id.update_btn);
            deleteBtn = view.findViewById(R.id.delete_btn);



            DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());

            updateBtn.setOnClickListener(v -> {
                try {
                    int recipePosition = getAdapterPosition();
                    if (recipePosition != RecyclerView.NO_POSITION && recyclerAdapter.cursor.moveToPosition(recipePosition)) {
                        String newRecipe = editTextName.getText().toString().trim();
                        String newImage = imageFileNameTextView.getText().toString().trim();
                        String newCategory = editTextCategory.getText().toString().trim();
                        String newIngredients = editTextIngredients.getText().toString().trim();
                        String newSteps = editTextSteps.getText().toString().trim();
                        String newCookingTimeStr = editTextCookingTime.getText().toString().trim();

                        if (newRecipe.isEmpty() || newIngredients.isEmpty() || newSteps.isEmpty() || newCookingTimeStr.isEmpty()) {
                            Toast.makeText(view.getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int newCookingTime = Integer.parseInt(newCookingTimeStr);
                        dbHelper.updateOne(recipeId, newRecipe, newImage, newCategory, newIngredients, newSteps, newCookingTime);
                        recyclerAdapter.swapCursor(dbHelper.fetchAll());
                        Toast.makeText(view.getContext(), "Recipe Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(view.getContext(), "Recipe Not Updated", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(view.getContext(), "Invalid Cooking Time", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Error updating recipe: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            deleteBtn.setOnClickListener(v -> {
                int recipePosition = getAdapterPosition();
                if (recipePosition != RecyclerView.NO_POSITION && recyclerAdapter.cursor.moveToPosition(recipePosition)) {
                    dbHelper.deleteOne(recipeId);
                    recyclerAdapter.swapCursor(dbHelper.fetchAll());
                    Toast.makeText(view.getContext(), "Recipe Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Recipe Not Deleted", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
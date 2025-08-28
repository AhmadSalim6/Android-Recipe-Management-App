package com.example.practice;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener {

    private RecyclerView categoryRecyclerView;
    private RecyclerView recipeRecyclerView;
    private CategoryAdapter categoryAdapter;
    private UserAdapter recipeAdapter;
    private DatabaseHelper dbHelper;
    private int userId = 1; // Hardcoded; replace with actual user ID
    private String currentCategory = "All";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_page, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        List<Category> categoryList = dbHelper.getAllCategories();
        categoryAdapter = new CategoryAdapter(requireContext(), categoryList, this);
        categoryRecyclerView.setAdapter(categoryAdapter);

        recipeRecyclerView = view.findViewById(R.id.recipeRecycler);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        Cursor cursor = dbHelper.filterByCategory(currentCategory);
        recipeAdapter = new UserAdapter(requireContext(), cursor, userId);
        recipeRecyclerView.setAdapter(recipeAdapter);

        EditText searchBar = view.findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                Cursor newCursor = dbHelper.searchBar(query);
                recipeAdapter.swapCursor(newCursor);
                currentCategory = "All";
                categoryAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onCategoryClick(String category) {
        currentCategory = category;
        Cursor newCursor = dbHelper.filterByCategory(category);
        recipeAdapter.swapCursor(newCursor);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (recipeAdapter != null) {
            recipeAdapter.swapCursor(null);
        }
    }
}

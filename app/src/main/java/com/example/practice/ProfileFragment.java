package com.example.practice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private TextView tvUserName;
    private ImageView ivUserPicture;
    private Button btnLogout;
    private Button btnSettings;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_PICTURE = "user_picture";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    // Instead of onCreate, Fragments use onCreateView to setup UI
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        // Initialize views
        tvUserName = view.findViewById(R.id.tv_user_name);
        ivUserPicture = view.findViewById(R.id.iv_user_picture);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnSettings = view.findViewById(R.id.btn_settings);

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, requireContext().MODE_PRIVATE);

        // Setup listeners
        btnLogout.setOnClickListener(v -> {
            // logoutUser();
        });

        btnSettings.setOnClickListener(v -> {
            navigateToSettings();
        });

        // Display user info
        displayUserInfo();

        return view;
    }

    private void displayUserInfo() {
        String userName = sharedPreferences.getString(KEY_USER_NAME, "Guest User");
        String userPictureUrl = sharedPreferences.getString(KEY_USER_PICTURE, "");

        tvUserName.setText(userName);
        ivUserPicture.setImageResource(R.drawable.ic_default_profile);
    }

//    private void logoutUser() {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.remove(KEY_USER_NAME);
//        editor.remove(KEY_USER_PICTURE);
//        editor.putBoolean(KEY_IS_LOGGED_IN, false);
//        editor.apply();
//
//        Intent intent = new Intent(requireActivity(), LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        requireActivity().finish();
//    }

    private void navigateToSettings() {
        Intent intent = new Intent(requireActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        displayUserInfo();
    }
}

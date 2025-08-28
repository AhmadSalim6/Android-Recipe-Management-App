package com.example.practice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName;
    private ImageView ivUserPicture;
    private Button btnLogout;
    private Button btnSettings;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_PICTURE = "user_picture";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        initSharedPreferences();
        setupClickListeners();


        displayUserInfo();
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tv_user_name);
        ivUserPicture = findViewById(R.id.iv_user_picture);
        btnLogout = findViewById(R.id.btn_logout);
        btnSettings = findViewById(R.id.btn_settings);
    }

    private void initSharedPreferences() {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logoutUser();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSettings();
            }
        });
    }


    public void displayUserInfo() {

        String userName = sharedPreferences.getString(KEY_USER_NAME, "Guest User");
        String userPictureUrl = sharedPreferences.getString(KEY_USER_PICTURE, "");


        tvUserName.setText(userName);

        ivUserPicture.setImageResource(R.drawable.ic_default_profile);


    }


//    public void logoutUser() {
//        // Clear session data
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.remove(KEY_USER_NAME);
//        editor.remove(KEY_USER_PICTURE);
//        editor.putBoolean(KEY_IS_LOGGED_IN, false);
//        editor.apply();
//
//        // Navigate to LoginActivity
//        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
//    }


    public void navigateToSettings() {
        Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayUserInfo();
    }
}
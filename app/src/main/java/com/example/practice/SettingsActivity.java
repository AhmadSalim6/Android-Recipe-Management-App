package com.example.practice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchTheme;


    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "AppSettings";
    private static final String KEY_THEME_MODE = "theme_mode";

    // Theme mode constants
    public static final int THEME_LIGHT = 1;
    public static final int THEME_DARK = 2;
    public static final int THEME_SYSTEM = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        initSharedPreferences();
        loadCurrentTheme();
        setupClickListeners();
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initViews() {
        switchTheme = findViewById(R.id.switch_theme);


        // Setup action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }
    }

    private void initSharedPreferences() {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }

    private void loadCurrentTheme() {
        int currentTheme = sharedPreferences.getInt(KEY_THEME_MODE, THEME_SYSTEM);


        switch (currentTheme) {
            case THEME_DARK:
                switchTheme.setChecked(true);
                break;
            case THEME_LIGHT:
                switchTheme.setChecked(false);
                break;
            case THEME_SYSTEM:
            default:
                // For system default, we'll check if current mode is dark
                int currentNightMode = getResources().getConfiguration().uiMode &
                        android.content.res.Configuration.UI_MODE_NIGHT_MASK;
                switchTheme.setChecked(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES);
                break;
        }
    }

    private void setupClickListeners() {
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int themeMode = isChecked ? THEME_DARK : THEME_LIGHT;
            toggleTheme(themeMode);
        });



    }


    public void toggleTheme(int mode) {
        switch (mode) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }

        // Store the selected theme mode temporarily
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_THEME_MODE, mode);
        editor.apply();
    }


    public void saveThemePreference() {
        int selectedTheme = switchTheme.isChecked() ? THEME_DARK : THEME_LIGHT;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_THEME_MODE, selectedTheme);
        editor.apply();

        // Apply the theme immediately
        toggleTheme(selectedTheme);

        // Show confirmation message
        String themeText = selectedTheme == THEME_DARK ? "Dark" : "Light";
        Toast.makeText(this, themeText + " theme saved successfully!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        // Handle back button in action bar
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCurrentTheme();
    }


    public int getCurrentThemeMode() {
        return sharedPreferences.getInt(KEY_THEME_MODE, THEME_SYSTEM);
    }


    public static void applySavedTheme(SharedPreferences preferences) {
        int savedTheme = preferences.getInt(KEY_THEME_MODE, THEME_SYSTEM);

        switch (savedTheme) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
}
package com.ravemaster.recipeapp;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.ravemaster.recipeapp.utilities.Constants;
import com.ravemaster.recipeapp.utilities.PreferenceManager;

public class MyApp extends Application {
    PreferenceManager preferenceManager;
    @Override
    public void onCreate() {
        super.onCreate();
        preferenceManager = new PreferenceManager(this);
        boolean isDarkTheme = preferenceManager.getIsDarkTheme(Constants.IS_DARK_THEME);
        if (isDarkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}

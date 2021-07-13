package com.shreya.team.Utill;

import android.content.Context;
import android.content.SharedPreferences;
//set theme preferences for display
public class ThemePref {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public ThemePref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public Boolean getIsDarkTheme() {
        return sharedPreferences.getBoolean("theme", false);
    }
    public void setIsDarkTheme(Boolean isDarkTheme) {
        editor.putBoolean("theme", isDarkTheme);
        editor.apply();
    }
    public Boolean getIsNotification() {
        return sharedPreferences.getBoolean("notification", true);
    }
    public void setIsNotification(Boolean isDarkTheme) {
        editor.putBoolean("notification", isDarkTheme);
        editor.apply();
    }
}

package com.shreya.team.Utill;

import android.content.Context;

import com.shreya.team.R;
//apply theme after obtaining info about which one has been selected
public class Tools {
    public static void getTheme(Context context) {
        ThemePref themePref = new ThemePref(context);
        if (themePref.getIsDarkTheme()) {
            context.setTheme(R.style.AppDarkTheme);
        } else {
            context.setTheme(R.style.AppTheme);
        }
    }
}

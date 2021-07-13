package com.shreya.team.Utill;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.shreya.team.Constant.Constant.EMAIL;
import static com.shreya.team.Constant.Constant.IMG_URL;
import static com.shreya.team.Constant.Constant.ISLOGIN;
import static com.shreya.team.Constant.Constant.NAME;
//storing preferences made up in the xml file in the form of key-value pairs onto device storage
public class AppSharedPreferences
{
    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static void setLoggedIn(Context context, boolean loggedIn,String setname,String setemail,String setimg_url) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(ISLOGIN, loggedIn);
        editor.putString(NAME, setname);
        editor.putString(EMAIL, setemail);
        editor.putString(IMG_URL, setimg_url);

        editor.apply();
    }
    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(ISLOGIN, false);
    }
    public static String getEmail(Context context) {
        return getPreferences(context).getString(EMAIL, "s");

    }



}
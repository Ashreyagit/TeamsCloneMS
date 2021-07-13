package com.shreya.team.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


import com.shreya.team.R;
import com.shreya.team.Utill.AppSharedPreferences;

import static com.shreya.team.Constant.Constant.ISLOGIN;
//Code for activity to performed when the splash screen appears
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            if (restorePrefData())
            {
                     if (AppSharedPreferences.getLoggedStatus(getApplicationContext())) {
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainActivity);
                    finish();
                            }
                else {
                    Intent mainActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(mainActivity);
                    finish();
                }
            }
            else {
                startActivity(new Intent(SplashActivity.this.getApplicationContext(), IntroActivity.class));
                finish();
            }
        }, 3000);
    }
    //restore preference data
    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend", false);
        return isIntroActivityOpnendBefore;
    }
    private boolean isLoggedIn() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isLoggedIn = pref.getBoolean(ISLOGIN, false);
        return isLoggedIn;
    }

}

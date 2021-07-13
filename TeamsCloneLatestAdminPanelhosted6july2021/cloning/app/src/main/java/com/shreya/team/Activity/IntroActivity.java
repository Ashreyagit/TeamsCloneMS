package com.shreya.team.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import com.shreya.team.Adapter.IntroViewPagerAdapter;
import com.shreya.team.Model.WelcomeScreenItem;
import com.shreya.team.R;
//Code for activity to performed when the app opens initially
public class IntroActivity extends AppCompatActivity {

    //declaring components of intro activity
    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    Button btnGetStarted;
    Animation btnAnim ;
    TextView tvSkip;

    //Function to be performed on creation of intro activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(), LoginActivity.class );
            startActivity(mainActivity);
            finish();
        }
        //assign values to components
        setContentView(R.layout.activity_intro);
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);

        // Text which gives a brief description of the function of the app when installed for the first time
        final List<WelcomeScreenItem> mList = new ArrayList<>();
        mList.add(new WelcomeScreenItem("Start a Meeting","A new Meeting will be created. The host can access all the functions and control the settings. Team members can attend the meet.",R.drawable.img1));
        mList.add(new WelcomeScreenItem("Join Meeting","Join Meeting on click of a button. For Joining the meeting simply type in the meeting code and join.",R.drawable.img2));
        mList.add(new WelcomeScreenItem("Meeting History","You can view all the meetings that you have attended. You can rejoin that meeting if needed.",R.drawable.img3));
        //to enable multiple swipe views
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);
        tabIndicator.setupWithViewPager(screenPager);
        btnNext.setOnClickListener(v -> {

            position = screenPager.getCurrentItem();
            if (position < mList.size()) {

                position++;
                screenPager.setCurrentItem(position);


            }

            if (position == mList.size()-1) { // when the last intro description placard has been reached



                loaddLastScreen();


            }


        });
        //function when tab is selected
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1) {

                    loaddLastScreen();

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //once last placard reached start Login Activity code(using Google authentication)
        btnGetStarted.setOnClickListener(v -> {
            Intent mainActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(mainActivity);
            savePrefsData();
            finish();
        });
        tvSkip.setOnClickListener(v -> screenPager.setCurrentItem(mList.size()));
    }
    //on opening the app again retain the user's preferences
    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;
    }
  //save the user's preferences for future use
    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();


    }

    //Making certain components visible when the user reaches the last screen
    private void loaddLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // Animation on get started button
        btnGetStarted.setAnimation(btnAnim);
    }
}

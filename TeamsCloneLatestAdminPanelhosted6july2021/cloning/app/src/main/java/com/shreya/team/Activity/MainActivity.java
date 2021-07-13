package com.shreya.team.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import com.shreya.team.Fragment.HistoryFragment;
import com.shreya.team.Fragment.HomeFragment;
import com.shreya.team.Fragment.SettingFragment;
import com.shreya.team.R;
import com.shreya.team.Utill.AppSharedPreferences;
import com.shreya.team.Utill.ThemePref;
import com.shreya.team.Utill.Tools;
//Code for activity to performed when the user has successfully logged in to the app and now is on the main page
public class MainActivity extends AppCompatActivity {

   //declare page components
    private static final String TAG = MainActivity.class.getSimpleName();
    Toolbar myToolbar;
    private BottomNavigationView navigation;
    private ViewPager viewPager;
    int pager_number = 3;
    MenuItem prevMenuItem;
    ThemePref themePref;
    //On the start of the main activity set status of component functions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.getTheme(this);
        setContentView(R.layout.activity_main);


        themePref = new ThemePref(this);
        myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        myToolbar.setTitle(getTitle());
        checkdynamiclinks();
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(pager_number);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
          //define display items when a particular page is selected (Main page/Past meetings/Settings)
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);

                if (viewPager.getCurrentItem() == 0) {
                    myToolbar.setTitle("SHREYA TEAMS");

                } else if (viewPager.getCurrentItem() == 1) {
                    myToolbar.setTitle("PAST MEETINGS");

                }else if (viewPager.getCurrentItem() == 2) {
                    myToolbar.setTitle("SETTINGS");

                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //navigate to a particular page
        navigation = findViewById(R.id.nav_view);
        initToolbarIcon();
        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_trending:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_favorite:
                    viewPager.setCurrentItem(2);
                    return true;

            }
            return false;
        });

    }
//check dynamic links for Firebase
    private void checkdynamiclinks() {

            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(this, pendingDynamicLinkData -> {
                        // Get a deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.d(TAG, "getDynamicLink:"+ deepLink.toString());
                            Log.d(TAG, "meetingcode:"+ pendingDynamicLinkData.getLink().getQueryParameter("meetingCode"));
                            joinmeeting_dialog(pendingDynamicLinkData.getLink().getQueryParameter("meetingCode"));
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "getDynamicLink:onFailure", e);
                        }
                    });
    }
 //on joinmeeting_dialog
    private void joinmeeting_dialog(String meeting_code) {

        joinMettingbtn(meeting_code);

    }
    //Call Jitsi SDK with various with different custom functions
    private void joinMettingbtn(String meeting_id) {
        JitsiMeetConferenceOptions options
                = null;
        try {
            options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))
                    .setRoom(meeting_id)
                    .setAudioMuted(false)
                    .setVideoMuted(false)
                    .setAudioOnly(false)
                    .setWelcomePageEnabled(false)
                    .setFeatureFlag("invite.enabled",false)

                    .build();
        } catch (MalformedURLException e) {
            Log.e("errorddd",e.getLocalizedMessage());
            e.printStackTrace();
        }
        JitsiMeetActivity.launch(this, options);
       

    }
//Specify characteristics and actions of toolbar
    public void initToolbarIcon() {
        ColorStateList iconsColorStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Color.parseColor("#8312e6"),
                        Color.parseColor("#03A9F4")
                });
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (themePref.getIsDarkTheme()) {
            navigation.setItemIconTintList(iconsColorStates);
            navigation.setItemTextColor(iconsColorStates);
            myToolbar.setBackgroundColor(getResources().getColor(R.color.colorToolbarDark));
        } else {
            myToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

    }
    //Go to main page if back pressed
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem((0), true);
        } else {
            finish();
        }
    }
    public class MyAdapter extends FragmentPagerAdapter {


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new HistoryFragment();
                case 2:
                    return new SettingFragment();


            }
            return null;
        }

        @Override
        public int getCount() {
            return pager_number;
        }

    }
}
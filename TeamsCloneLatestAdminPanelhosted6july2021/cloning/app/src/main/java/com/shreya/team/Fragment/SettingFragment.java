package com.shreya.team.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.onesignal.OneSignal;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import com.shreya.team.BuildConfig;
import com.shreya.team.Activity.LoginActivity;
import com.shreya.team.Activity.MainActivity;
import com.shreya.team.R;
import com.shreya.team.Utill.AppSharedPreferences;
import com.shreya.team.Utill.ThemePref;
import com.shreya.team.Utill.Tools;

import static com.shreya.team.Constant.Constant.EMAIL;
import static com.shreya.team.Constant.Constant.IMG_URL;
import static com.shreya.team.Constant.Constant.NAME;
import android.net.Uri;
import android.widget.Toast;
import android.content.pm.PackageManager;
//to bind components to their actions in the Settings Page
public class SettingFragment extends Fragment {

    @BindView(R.id.btn_switch_theme)
    MaterialRippleLayout darktheme;


    @BindView(R.id.btn_share)
    MaterialRippleLayout share;
    @BindView(R.id.sentiment)
    MaterialRippleLayout sentiment;
    @BindView(R.id.chatroom)
    MaterialRippleLayout chatroom;
    @BindView(R.id.switch_theme)
    Switch switch_theme;
    @BindView(R.id.switch_notification)
    Switch switch_notification;
    SignInButton siginbutton;
    @BindView(R.id.profile_card_view)
    CardView profile_card;
    @BindView(R.id.cardView_about)
    CardView about_card;
    @BindView(R.id.profile_img)
    CircleImageView profile_image;
    @BindView(R.id.name)
    TextView nametext;
    @BindView(R.id.email)
    TextView emailtext;
    @BindView(R.id.logout)
    TextView logout;

    private static final int RC_SIGN_IN = 234;
    ThemePref themePref;
    GoogleSignInClient mGoogleSignInClient;
    SharedPreferences preferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
//Google sign in with an inflated layout to highlight the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Tools.getTheme(getContext());
        View root =inflater.inflate(R.layout.fragment_setting, container, false);
        themePref = new ThemePref(getContext());
        siginbutton=root.findViewById(R.id.sign_in_button);
        ButterKnife.bind(this,root);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(AppSharedPreferences.getLoggedStatus(getActivity())) {
            profile_card.setVisibility(View.VISIBLE);
            String savedname=preferences.getString(NAME,null);
            String savedemail=preferences.getString(EMAIL,null);
            String savedimage=preferences.getString(IMG_URL,null);
            siginbutton.setVisibility(View.GONE);
            nametext.setText(savedname);
            emailtext.setText(savedemail);
            Context context = getContext();
            Glide.with(getContext())
                    .load(savedimage)
                    .into(profile_image);
        } else {
            siginbutton.setVisibility(View.VISIBLE);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        siginbutton.setOnClickListener(v -> signIn());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSharedPreferences.setLoggedIn(getContext(), false,"","","");
                profile_card.setVisibility(View.GONE);
                siginbutton.setVisibility(View.VISIBLE);
                getActivity().finish();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

 //Direct to sentiment analysis demo on clicking button
       sentiment.setOnClickListener(v -> {
           Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
           try {
               intent.setData(Uri.parse("https://www.youtube.com/embed/EWrUZK8jlSY"));
               startActivity(intent);
           } catch (ActivityNotFoundException exception) {
               Toast.makeText(getContext(), "Error text", Toast.LENGTH_SHORT).show();
           }
       });
    //Direct to a permanent chatroom on clicking the button
        chatroom.setOnClickListener(v -> {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            try {
                intent.setData(Uri.parse("https://onec-9f393.web.app"));
                startActivity(intent);
            } catch (ActivityNotFoundException exception) {
                Toast.makeText(getContext(), "Error text", Toast.LENGTH_SHORT).show();
            }
        });





        share.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage= "\nPlease download this APK to get the app on your android device\n\n";
                shareMessage = shareMessage + "https://drive.google.com/file/d/15_OFtmz3Ks4Yo3XuWPEtw3oVo5tonLfe/view?usp=sharing"+"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {

            }
        });

        if (themePref.getIsDarkTheme()) {
            profile_card.setCardBackgroundColor(getResources().getColor(R.color.CardColorDark));
            about_card.setCardBackgroundColor(getResources().getColor(R.color.CardColorDark));
            switch_theme.setChecked(true);
        } else {
            switch_theme.setChecked(false);
        }
        switch_theme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            themePref.setIsDarkTheme(isChecked);
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        if (themePref.getIsNotification()) {
            switch_notification.setChecked(false);
        } else {
            switch_notification.setChecked(true);
        }
        switch_notification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            themePref.setIsNotification(!isChecked);
            switch_notification.setChecked(isChecked);
            if(isChecked)
            {
                OneSignal.setSubscription(false);
            }
            else
            {
                OneSignal.setSubscription(true);
            }
        });
        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String name = account.getDisplayName();
                String email = account.getEmail();
                String img_url = String.valueOf(account.getPhotoUrl());
                //authenticating with firebas
                Toasty.success(getActivity(),"Login Success",Toasty.LENGTH_LONG).show();
                AppSharedPreferences.setLoggedIn(getContext(), true,name,email,img_url);
                profile_card.setVisibility(View.VISIBLE);
                siginbutton.setVisibility(View.GONE);
                nametext.setText(name);
                emailtext.setText(email);
                Glide.with(getContext())
                        .load(img_url)
                        .into(profile_image);
            } catch (ApiException e) {
                Toasty.error(getContext(),e.getMessage(),Toasty.LENGTH_SHORT).show();
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
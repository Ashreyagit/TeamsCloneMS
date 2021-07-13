package com.shreya.team.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import com.shreya.team.Adapter.MeetingHistoryAdapter;
import com.shreya.team.BuildConfig;
import com.shreya.team.Model.RecentMeetingModel;
import com.shreya.team.R;
import com.shreya.team.Retrofit.Api;
import com.shreya.team.Utill.AppSharedPreferences;
import com.shreya.team.Utill.ThemePref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//to bind components to their actions in the Home Page
public class HomeFragment extends Fragment {

    @BindView(R.id.new_meeting)
    MaterialRippleLayout new_meeting;
    @BindView(R.id.join_meeting)
    MaterialRippleLayout join_meeting;
    @BindView(R.id.card_1)
    CardView card_1;
    @BindView(R.id.card_2)
    CardView card_2;
    @BindView(R.id.oval)
    ImageView oval;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.relative_main)
    RelativeLayout relative_main;
    @BindView(R.id.view_all_recent)
    TextView allrecent;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.adView)
    AdView mAdView ;
    String email;
    URL serverURL;

    private InterstitialAd mInterstitialAd;
    List<RecentMeetingModel> categoryListResponseData;

    MeetingHistoryAdapter categoryNewsAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            serverURL = new URL("https://meet.jit.si");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);


    }

//action to be performed when the user selects dark theme
    private void darktheme() {
        ThemePref themePref;
        themePref = new ThemePref(getContext());
        if (themePref.getIsDarkTheme()) {
            card_1.setCardBackgroundColor(getResources().getColor(R.color.CardColorDark));
            card_2.setCardBackgroundColor(getResources().getColor(R.color.CardColorDark));
            relative_main.setBackgroundColor(getResources().getColor(R.color.colorBackgroundDark));
            scrollview.setBackgroundColor(getResources().getColor(R.color.colorBackgroundDark));
        } else {

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this,view);
        new_meeting.setOnClickListener(v -> show_newmeeting_dialog());
        join_meeting.setOnClickListener(v -> joinmeeting_dialog());
        darktheme();


        refresh(AppSharedPreferences.getEmail(getContext()));
        if(Boolean.parseBoolean(getString(R.string.isAdsEnabled)))
        {
            loadbannerads();
            loadinterstialads();
        }
        else
        {
            mAdView.setVisibility(View.GONE);
        }

        return view;
    }

    private void loadinterstialads() {
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getString(R.string.inter_adunit));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void loadbannerads() {

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

//on joining meet
    private void joinmeeting_dialog() {
        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(getContext());
        View mView=getLayoutInflater().inflate(R.layout.join_meeting_dialog,null);
        Button btn_join;
        EditText join_meeting_id;
        Switch switch_video_join;
        join_meeting_id=mView.findViewById(R.id.join_meeting_code);
        switch_video_join=mView.findViewById(R.id.switch_video_join);
        btn_join=mView.findViewById(R.id.join_now_btn);
        alertDialogBuilder.setView(mView);
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        switch_video_join.setChecked(true);
        switch_video_join.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switch_video_join.setChecked(isChecked);
        });
        btn_join.setOnClickListener(v -> {
            if(join_meeting_id.getText().length()>5)
            {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    alertDialog.hide();
                    joinMettingbtn(join_meeting_id.getText().toString(),!switch_video_join.isChecked());
                }

            }
            else
            {
                Toasty.error(getContext(),"Please Enter Valid Meeting Code",Toasty.LENGTH_SHORT).show();
            }



        });
    }

    private void refresh(String email) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        (Api.createAPI().getRecentMeetings(email)).enqueue(new Callback<List<RecentMeetingModel>>() {
            @Override
            public void onResponse(Call<List<RecentMeetingModel>> call, Response<List<RecentMeetingModel>> response) {
                if(!response.body().isEmpty())
                {

                    recyclerView.setVisibility(View.VISIBLE);

                    categoryListResponseData = response.body();
                    categoryNewsAdapter = new MeetingHistoryAdapter(categoryListResponseData, getContext());
                    recyclerView.setAdapter(categoryNewsAdapter);
                }
                else
                {


                    recyclerView.setVisibility(View.INVISIBLE);


                }

            }

            @Override
            public void onFailure(Call<List<RecentMeetingModel>> call, Throwable t) {


                recyclerView.setVisibility(View.INVISIBLE);


            }
        });
    }
    private void createdynamiclink(String meeting_code) {
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(getString(R.string.app_deep_link_url)+meeting_code))
                .setDomainUriPrefix(getString(R.string.app_dynamic_link_url_prefix))
                .buildShortDynamicLink()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            shareText(shortLink.toString());
                        } else {

                        }
                    }
                });
    }

    private void joinMettingbtn(String meeting_id, boolean isVideoOn) {
        JitsiMeetConferenceOptions options
                = null;
        try {
            options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))
                    .setRoom(meeting_id)
                    .setAudioMuted(false)
                    .setVideoMuted(isVideoOn)
                    .setAudioOnly(false)
                    .setWelcomePageEnabled(false)
                    .setFeatureFlag("invite.enabled",false)

                    .build();
        } catch (MalformedURLException e) {
            Log.e("errorddd",e.getLocalizedMessage());
            e.printStackTrace();
        }
        JitsiMeetActivity.launch(getContext(), options);
        try {
            DatasendtoDatabase(meeting_id,meeting_id);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startMetting(String meeting_id,String metting_title,boolean isVideoOn) throws IOException {
        JitsiMeetConferenceOptions options
                = null;
        try {
            options = new JitsiMeetConferenceOptions.Builder()
            .setServerURL(new URL("https://meet.jit.si"))
            .setRoom(meeting_id)
            .setAudioMuted(false)
             .setSubject(metting_title)
            .setVideoMuted(isVideoOn)
            .setAudioOnly(false)
            .setWelcomePageEnabled(false)
                    .setFeatureFlag("invite.enabled",false)

                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        JitsiMeetActivity.launch(getContext(), options);


        DatasendtoDatabase(meeting_id,metting_title);
    }

    public void show_newmeeting_dialog()
    {

        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(getContext());

        View mView=getLayoutInflater().inflate(R.layout.activity_done_acivity,null);

        TextView meeting_link,meeting_id;
        Button btn_continue,btn_share_link;
        EditText meeting_title;
        Switch switch_video;
        ImageView sharelink,sharecode;
        boolean isVideoOn;
        btn_continue=mView.findViewById(R.id.continue_btn);
        btn_share_link=mView.findViewById(R.id.sharelink_btn);
        meeting_id=mView.findViewById(R.id.meeting_id);
        meeting_title=mView.findViewById(R.id.meeting_title);
        switch_video=mView.findViewById(R.id.switch_video);

        sharecode=mView.findViewById(R.id.share_code);


         final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
         SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder( 7 );
        for( int i = 0; i < 7; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        meeting_id.setText(sb.toString());


        alertDialogBuilder.setView(mView);
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        switch_video.setChecked(true);
        switch_video.setOnCheckedChangeListener((buttonView, isChecked) -> {

            switch_video.setChecked(isChecked);

        });
        btn_continue.setOnClickListener(v -> {
            if(meeting_title.getText().length()>3)
            {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    alertDialog.hide();
                    try {
                        startMetting(meeting_id.getText().toString(),  meeting_title.getText().toString(),!switch_video.isChecked());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            else
            {
                Toasty.warning(getContext(),"Please Enter Title",Toasty.LENGTH_SHORT).show();
            }


        });
        btn_share_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createdynamiclink(meeting_id.getText().toString());
            }
        });
        sharecode.setOnClickListener(v -> {

            shareText("Meeting Id - \n" + meeting_id.getText().toString());
        });


    }

    public  void shareText(String sharetext)
    {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String shareMessage= "\nJoin this Meeting now\n"+sharetext+"\n";
            shareMessage = shareMessage + "TIf you do not have the Shreya Teams app,install the apk on your android device:\n https://drive.google.com/file/d/15_OFtmz3Ks4Yo3XuWPEtw3oVo5tonLfe/view?usp=sharing"+"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {

        }
    }

    private void DatasendtoDatabase(String meeting_id,String metting_title) throws IOException {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String current_Date = df.format(c.getTime());
        (Api.createAPI().doCreateMeetingWithField(metting_title,meeting_id,current_Date,email)).enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("responseseror", String.valueOf(t));
            }


        });

    }

    @Override
    public void onStart() {
        super.onStart();

        if (AppSharedPreferences.getLoggedStatus(getContext())){
            // No user is signed in
            email=AppSharedPreferences.getEmail(getContext());
        } else {
            email="demo@gmail.com";
            // User logged in
        }
    }
}
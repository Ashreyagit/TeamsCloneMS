package com.shreya.team.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.shreya.team.Adapter.MeetingHistoryAdapter;
import com.shreya.team.Model.RecentMeetingModel;
import com.shreya.team.R;
import com.shreya.team.Retrofit.Api;
import com.shreya.team.Utill.AppSharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//to bind components to their actions in the History Page
public class HistoryFragment extends Fragment {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.nodatafound)
    LinearLayout nodatafound;
    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;
    @BindView(R.id.adView)
    AdView mAdView ;
    List<RecentMeetingModel> categoryListResponseData;
    MeetingHistoryAdapter categoryNewsAdapter;
    //On creation of history page
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this,view);

        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        refresh(AppSharedPreferences.getEmail(getContext()));
        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            recyclerView.setVisibility(View.GONE);
            refresh(AppSharedPreferences.getEmail(getContext()));

        });

        if(Boolean.parseBoolean(getString(R.string.isAdsEnabled)))
        {
            loadbannerads();
        }
        else
        {
            mAdView.setVisibility(View.GONE);
        }

        timeago();
        return view;
    }
    //update recent meetings on refreshing
    private void refresh(String email) {
        (Api.createAPI().getRecentMeetings(email)).enqueue(new Callback<List<RecentMeetingModel>>() {
            @Override
            public void onResponse(Call<List<RecentMeetingModel>> call, Response<List<RecentMeetingModel>> response) {
                if(!response.body().isEmpty())
                {

                    mSwipeRefreshLayout.setRefreshing(false);
                    recyclerView.setVisibility(View.VISIBLE);
                    progress_circular.setVisibility(View.INVISIBLE);
                    nodatafound.setVisibility(View.INVISIBLE);
                    categoryListResponseData = response.body();
                    categoryNewsAdapter = new MeetingHistoryAdapter(categoryListResponseData, getContext());
                    recyclerView.setAdapter(categoryNewsAdapter);
                }
                else
                {

                    mSwipeRefreshLayout.setRefreshing(false);
                    nodatafound.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    progress_circular.setVisibility(View.INVISIBLE);

                }

            }
          //When recent meeting can't be obtained erroneously
            @Override
            public void onFailure(Call<List<RecentMeetingModel>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                nodatafound.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                progress_circular.setVisibility(View.INVISIBLE);

            }
        });
    }
//google admob for a production ready app
    private void loadbannerads() {

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
  //display the time of the recent meeting in the assigned format
    void timeago()
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            Date past = format.parse("2016.02.05 AD at 23:59:30");
            Date now = new Date();
            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
//
//          System.out.println(TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + " milliseconds ago");
//          System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
//          System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
//          System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");

            if(seconds<60)
            {
                Log.d("seconds",seconds+" seconds ago");
            }
            else if(minutes<60)
            {
                Log.d("minutes",minutes+" minutes ago");
                System.out.println(minutes+" minutes ago");
            }
            else if(hours<24)
            {
                Log.d("hours",hours+" hours ago");
                System.out.println(hours+" hours ago");
            }
            else
            {
                Log.d("days",days+" days ago");
                System.out.println(days+" days ago");
            }
        }
        catch (Exception j){
            j.printStackTrace();
        }

    }
}
package com.shreya.team.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.shreya.team.Model.RecentMeetingModel;
import com.shreya.team.R;
//display the Meeting History Page's UI using lists, count of objects in it and actions
public class MeetingHistoryAdapter extends RecyclerView.Adapter<MeetingHistoryAdapter.MyHolder>
{

    List<RecentMeetingModel> noteslist;
    private Context context;
    URL serverURL;
    public MeetingHistoryAdapter(List<RecentMeetingModel> noteslist, Context context)
    {
        this.context=context;
        this.noteslist=noteslist;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);

        MyHolder myHolder=new MyHolder(view);
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
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int position) {
        RecentMeetingModel data=noteslist.get(position);
        myHolder.title.setText(data.getTitle());
        myHolder.desc.setText(data.getTime());
    }

    @Override
    public int getItemCount() {
        return noteslist.size();
    }

    class  MyHolder extends RecyclerView.ViewHolder  {
        TextView title,desc;
        Button rejoin_btn;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            rejoin_btn=itemView.findViewById(R.id.rejoin_btn);
            desc=itemView.findViewById(R.id.date);
            rejoin_btn.setOnClickListener(v -> {
                RecentMeetingModel listdata=noteslist.get(getAdapterPosition());
                joinmeeting(listdata.getMeetingId());

            });

        }


    }
    //identify items in the list by their id to proceed
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    private void joinmeeting(String meeting_id) {
        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);
        View mView=((Activity)context).getLayoutInflater().inflate(R.layout.join_meeting_dialog,null);
        Button btn_join;
        EditText join_meeting_id;
        Switch switch_video_join;
        join_meeting_id=mView.findViewById(R.id.join_meeting_code);
        join_meeting_id.setText(meeting_id);
        join_meeting_id.setEnabled(false);
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

            alertDialog.hide();
            joinMettingbtn(meeting_id,!switch_video_join.isChecked());

        });

    }
//on clicking join meeting button options and prefs before joining the meet
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
            e.printStackTrace();
        }
        // Launching the new activity with the given options. The launch() method takes care of creating the required Intent and passing the options.
        JitsiMeetActivity.launch(context, options);
    }
}

 package com.shreya.team.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
//retrieve recent meetings and their details
public class RecentMeetingModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("meeting_id")
    @Expose
    private String meetingId;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("createdby")
    @Expose
    private String createdby;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

}
package com.shreya.team.Retrofit;
import java.util.List;
import com.shreya.team.Model.RecentMeetingModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//creating the interface of retrofit
public interface RetrofitInstance {

    @GET("meetings/{createdby}")
    Call<List<RecentMeetingModel>> getRecentMeetings(@Path("createdby") String createdby);
    @GET("adduser")
    Call<String> doCreateUserWithField(@Query("name") String name,
                                       @Query("email") String email,
                                       @Query("img_url") String img_url
    );
    @GET("addmeeting")
    Call<String> doCreateMeetingWithField(@Query("title") String title,
                                       @Query("meeting_id") String meeting_id,
                                       @Query("time") String time,
                                          @Query("createdby") String createdby
    );

}

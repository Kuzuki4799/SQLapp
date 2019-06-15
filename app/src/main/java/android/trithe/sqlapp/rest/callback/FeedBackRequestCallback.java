package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataRatingFilmResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FeedBackRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_FEED_BACK)
    Call<BaseResponse> feedBackApp(@Field("user_id") String user_id, @Field("content") String content);

    @FormUrlEncoded
    @POST(Config.API_CHECK_FEED_BACK)
    Call<BaseResponse> checkFeedBackApp(@Field("user_id") String user_id);
}

package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetNotificationResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NotificationRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_NOTIFICATION)
    Call<GetNotificationResponse> getDataNotification(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(Config.API_GET_COUNT_NOTIFICATION)
    Call<GetNotificationResponse> getCountNotification(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(Config.API_SEEN_NOTIFICATION)
    Call<BaseResponse> checkSeenNotification(@Field("user_id") String user_id, @Field("film_id") String id);
}

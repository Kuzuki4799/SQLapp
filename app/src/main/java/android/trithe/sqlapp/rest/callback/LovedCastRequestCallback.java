package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataLoveCountResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LovedCastRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_COUNT_LOVE_CAST)
    Call<GetDataLoveCountResponse> getCountLoveCast(@Field("cast_id") String cast_id);

    @FormUrlEncoded
    @POST(Config.API_INSERT_LOVE_CAST)
    Call<BaseResponse> insertLove(@Field("user_id") String user_id, @Field("cast_id") String cast_id);

    @FormUrlEncoded
    @POST(Config.API_DELETE_LOVE_CAST)
    Call<BaseResponse> deleteLove(@Field("user_id") String user_id, @Field("cast_id") String cast_id);

}

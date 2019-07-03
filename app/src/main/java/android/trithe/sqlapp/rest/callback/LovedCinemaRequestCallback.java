package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataLoveCountCinemaResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LovedCinemaRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_COUNT_LOVE_CINEMA)
    Call<GetDataLoveCountCinemaResponse> getCountLoveCinema(@Field("cinema_id") String cinema_id);

    @FormUrlEncoded
    @POST(Config.API_INSERT_LOVE_CINEMA)
    Call<BaseResponse> insertLoveCinema(@Field("user_id") String user_id, @Field("cinema_id") String cinema_id);

    @FormUrlEncoded
    @POST(Config.API_DELETE_LOVE_CINEMA)
    Call<BaseResponse> deleteLoveCinema(@Field("user_id") String user_id, @Field("cinema_id") String cinema_id);

}

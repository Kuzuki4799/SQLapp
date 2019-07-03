package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetAllDataCinemaResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CinemaRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_GET_CINEMA)
    Call<GetAllDataCinemaResponse> getAllCinema(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(Config.API_DETAIL_CINEMA)
    Call<GetAllDataCinemaResponse> getDetailCinema(@Field("user_id") String user_id, @Field("id") String id);

    @FormUrlEncoded
    @POST(Config.API_UPDATE_VIEWS_CINEMA)
    Call<BaseResponse> pushUpdateViews(@Field("id") String id, @Field("views") String views);
}

package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataRatingCinemaResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RatingCinemaRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_RATING_CINEMA)
    Call<GetDataRatingCinemaResponse> getRatingCinema(@Field("cinema_id") String cinema_id);

    @FormUrlEncoded
    @POST(Config.API_CHECK_RAT_CINEMA)
    Call<BaseResponse> pushCheckRatCinema(@Field("user_id") String user_id, @Field("cinema_id") String cinema_id);

    @FormUrlEncoded
    @POST(Config.API_PUSH_INSERT_RAT_CINEMA)
    Call<BaseResponse> pushRatFilm(@Field("user_id") String user_id, @Field("cinema_id") String cinema_id, @Field("rat") String rat);
}

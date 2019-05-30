package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataRatingFilmResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RatingFilmRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_RATING_FILM)
    Call<GetDataRatingFilmResponse> getRatingFilm(@Field("film_id") String film_id);

    @FormUrlEncoded
    @POST(Config.API_CHECK_RAT)
    Call<BaseResponse> getCheckRat(@Field("user_id") String user_id, @Field("film_id") String film_id);

    @FormUrlEncoded
    @POST(Config.API_PUSH_RAT)
    Call<BaseResponse> pushRatFilm(@Field("rat") String rat, @Field("user_id") String user_id, @Field("film_id") String film_id);
}

package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.GetAllDataFilmCinemaResponse;
import android.trithe.sqlapp.rest.response.GetAllDataShowingCinemaResponse;
import android.trithe.sqlapp.rest.response.GetAllDataShowingDateResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ShowingCinemaRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_GET_SHOWING_TIME)
    Call<GetAllDataShowingDateResponse> getShowingTime(@Field("cinema_id") int cinema_id,
                                                       @Field("film_id") int film_id);

    @FormUrlEncoded
    @POST(Config.API_GET_SHOWING_TIME_BY_DATE)
    Call<GetAllDataShowingCinemaResponse> getShowingTimeByDate(@Field("cinema_id") int cinema_id,
                                                               @Field("film_id") int film_id,
                                                               @Field("date") String time,
                                                               @Field("status") int status);

    @FormUrlEncoded
    @POST(Config.API_GET_FILM_BY_DATE_AND_CINEMA)
    Call<GetAllDataFilmCinemaResponse> getFilmByDateCinema(@Field("cinema_id") int cinema_id,
                                                           @Field("user_id") int user_id);
}

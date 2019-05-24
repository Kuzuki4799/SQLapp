package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.GetDataRatingFilmResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RatingFilmRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_RATING_FILM)
    Call<GetDataRatingFilmResponse> getRatingFilm(@Field("film_id") String film_id);
}

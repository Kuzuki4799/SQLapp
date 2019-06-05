package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataSeriesFilmResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FilmRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_FILM)
    Call<GetDataFilmResponse> getFilm(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(Config.API_SEARCH_FILM)
    Call<GetDataFilmResponse> getSearchFilm(@Field("user_id") String user_id, @Field("name") String name);

    @FormUrlEncoded
    @POST(Config.API_GET_FILM_BY_CAST)
    Call<GetDataFilmResponse> getFilmByCast(@Field("user_id") String user_id, @Field("cast_id") String cast_id);

    @FormUrlEncoded
    @POST(Config.API_GET_FILM_BY_ID)
    Call<GetDataFilmResponse> getFilmById(@Field("user_id") String user_id, @Field("id") String id);


    @FormUrlEncoded
    @POST(Config.API_GET_SERIES)
    Call<GetDataSeriesFilmResponse> getSeriesFilm(@Field("film_id") String film_id);

}

package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.GetDataFilmDetailResponse;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataSeriesFilmResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FilmRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_FILM)
    Call<GetDataFilmResponse> getFilm(@Field("get_status") int get_status,
                                      @Field("user_id") String user_id,
                                      @Field("page") int page,
                                      @Field("per_page") int per_page);

    @FormUrlEncoded
    @POST(Config.API_GET_FILM_SAVED)
    Call<GetDataFilmResponse> getSavedFilm(@Field("user_id") String user_id,
                                           @Field("page") int page,
                                           @Field("per_page") int per_page);

    @FormUrlEncoded
    @POST(Config.API_GET_FILM_BY_ID)
    Call<GetDataFilmDetailResponse> getFilmById(@Field("user_id") String user_id,
                                                @Field("id") String id);

    @FormUrlEncoded
    @POST(Config.API_SEARCH_FILM)
    Call<GetDataFilmResponse> getSearchFilm(@Field("user_id") String user_id, @Field("name") String name,
                                            @Field("page") int page,
                                            @Field("per_page") int per_page);

    @FormUrlEncoded
    @POST(Config.API_GET_FILM_BY_CAST)
    Call<GetDataFilmResponse> getFilmByCast(@Field("user_id") String user_id, @Field("cast_id") String cast_id,
                                            @Field("page") int page,
                                            @Field("per_page") int per_page);

}

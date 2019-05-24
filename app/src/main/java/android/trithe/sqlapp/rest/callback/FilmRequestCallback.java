package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FilmRequestCallback {

    @GET(Config.API_FILM)
    Call<GetDataFilmResponse> getFilm();

    @FormUrlEncoded
    @POST(Config.API_SEARCH_FILM)
    Call<GetDataFilmResponse> getSearchFilm(@Field("name") String name);

}

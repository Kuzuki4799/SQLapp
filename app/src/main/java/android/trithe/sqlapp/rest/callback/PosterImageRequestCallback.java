package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.GetDataPosterImageResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PosterImageRequestCallback {

    @GET(Config.API_POSTER)
    Call<GetDataPosterImageResponse> getPosterImage();

}

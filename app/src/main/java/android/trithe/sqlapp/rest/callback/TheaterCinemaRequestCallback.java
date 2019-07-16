package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.GetAllDataShowingCinemaResponse;
import android.trithe.sqlapp.rest.response.GetDataTheaterResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TheaterCinemaRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_GET_THEATER)
    Call<GetDataTheaterResponse> getDataTheater(@Field("cinema_id") int cinema_id);
}

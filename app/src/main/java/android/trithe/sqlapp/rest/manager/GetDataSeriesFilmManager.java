package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataSeriesFilmResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetDataSeriesFilmManager {

    private ResponseCallbackListener<GetDataSeriesFilmResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_SERIES_FILM = "GET_SERIES_FILM";

    public GetDataSeriesFilmManager(ResponseCallbackListener<GetDataSeriesFilmResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataSeriesFilm(String film_id) {
        Call<GetDataSeriesFilmResponse> call = mRestApiManager.filmRequestCallback()
                .getSeriesFilm(film_id);
        call.enqueue(new Callback<GetDataSeriesFilmResponse>() {
            @Override
            public void onResponse(Call<GetDataSeriesFilmResponse> call, Response<GetDataSeriesFilmResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(GET_SERIES_FILM, response.body());
                } else {
                    mListener.onResponseFailed(GET_SERIES_FILM, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataSeriesFilmResponse> call, Throwable t) {
                mListener.onResponseFailed(GET_SERIES_FILM, t.getMessage());
            }
        });
    }
}

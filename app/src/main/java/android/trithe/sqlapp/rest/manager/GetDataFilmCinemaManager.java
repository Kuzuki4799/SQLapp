package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetAllDataFilmCinemaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDataFilmCinemaManager {

    private ResponseCallbackListener<GetAllDataFilmCinemaResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_DATA_FILM_CINEMA = "GET_DATA_FILM_CINEMA";

    public GetDataFilmCinemaManager(ResponseCallbackListener<GetAllDataFilmCinemaResponse> mListener) {
        this.mListener = mListener;
    }

    public void getFilmByDateCinema(int cinema_id, int user_id) {
        Call<GetAllDataFilmCinemaResponse> call = mRestApiManager.showingCinemaRequestCallback()
                .getFilmByDateCinema(cinema_id, user_id);
        call.enqueue(new Callback<GetAllDataFilmCinemaResponse>() {
            @Override
            public void onResponse(Call<GetAllDataFilmCinemaResponse> call, Response<GetAllDataFilmCinemaResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(GET_DATA_FILM_CINEMA, response.body());
                } else {
                    mListener.onResponseFailed(GET_DATA_FILM_CINEMA, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetAllDataFilmCinemaResponse> call, Throwable t) {
                mListener.onResponseFailed(GET_DATA_FILM_CINEMA, t.getMessage());
            }
        });
    }
}

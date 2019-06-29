package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataRatingFilmResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_RATING_FILM;


public class GetDataRatingFilmManager {

    private ResponseCallbackListener<GetDataRatingFilmResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public GetDataRatingFilmManager(ResponseCallbackListener<GetDataRatingFilmResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataRating(String id) {
        Call<GetDataRatingFilmResponse> call = mRestApiManager.ratingFilmRequestCallback()
                .getRatingFilm(id);
        call.enqueue(new Callback<GetDataRatingFilmResponse>() {
            @Override
            public void onResponse(Call<GetDataRatingFilmResponse> call, Response<GetDataRatingFilmResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(API_RATING_FILM, response.body());
                } else {
                    mListener.onResponseFailed(API_RATING_FILM, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataRatingFilmResponse> call, Throwable t) {
                mListener.onResponseFailed(API_RATING_FILM, t.getMessage());
            }
        });
    }
}


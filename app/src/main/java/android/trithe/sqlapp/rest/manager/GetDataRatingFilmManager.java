package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataRatingFilmResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetDataRatingFilmManager {

    private ResponseCallbackListener<GetDataRatingFilmResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_RATING = "GET_RATING";

    public GetDataRatingFilmManager(ResponseCallbackListener<GetDataRatingFilmResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataRatingFilm(String film_id) {
        Call<GetDataRatingFilmResponse> call = mRestApiManager.ratingFilmRequestCallback()
                .getRatingFilm(film_id);
        call.enqueue(new Callback<GetDataRatingFilmResponse>() {
            @Override
            public void onResponse(Call<GetDataRatingFilmResponse> call, Response<GetDataRatingFilmResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(GET_RATING, response.body());
                } else {
                    mListener.onResponseFailed(GET_RATING, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataRatingFilmResponse> call, Throwable t) {
                mListener.onResponseFailed(GET_RATING, t.getMessage());
            }
        });
    }
}

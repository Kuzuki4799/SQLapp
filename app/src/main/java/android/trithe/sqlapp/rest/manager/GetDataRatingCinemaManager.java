package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataRatingCinemaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_RATING_CINEMA;

public class GetDataRatingCinemaManager {

    private ResponseCallbackListener<GetDataRatingCinemaResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public GetDataRatingCinemaManager(ResponseCallbackListener<GetDataRatingCinemaResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataRating(String id) {
        Call<GetDataRatingCinemaResponse> callRatingCinema = mRestApiManager.ratingCinemaRequestCallback()
                .getRatingCinema(id);
        callRatingCinema.enqueue(new Callback<GetDataRatingCinemaResponse>() {
            @Override
            public void onResponse(Call<GetDataRatingCinemaResponse> call, Response<GetDataRatingCinemaResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(API_RATING_CINEMA, response.body());
                } else {
                    mListener.onResponseFailed(API_RATING_CINEMA, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataRatingCinemaResponse> call, Throwable t) {
                mListener.onResponseFailed(API_RATING_CINEMA, t.getMessage());
            }
        });
    }
}

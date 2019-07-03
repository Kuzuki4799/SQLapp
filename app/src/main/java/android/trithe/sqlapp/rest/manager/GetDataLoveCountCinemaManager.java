package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataLoveCountCinemaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDataLoveCountCinemaManager {

    private ResponseCallbackListener<GetDataLoveCountCinemaResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_LOVE_COUNT_CINEMA = "GET_LOVE_COUNT_CINEMA";

    public GetDataLoveCountCinemaManager(ResponseCallbackListener<GetDataLoveCountCinemaResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataLoveCount(String cinema_id) {
        Call<GetDataLoveCountCinemaResponse> call = mRestApiManager.lovedCinemaRequestCallback()
                .getCountLoveCinema(cinema_id);
        call.enqueue(new Callback<GetDataLoveCountCinemaResponse>() {
            @Override
            public void onResponse(Call<GetDataLoveCountCinemaResponse> call, Response<GetDataLoveCountCinemaResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(GET_LOVE_COUNT_CINEMA, response.body());
                } else {
                    mListener.onResponseFailed(GET_LOVE_COUNT_CINEMA, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataLoveCountCinemaResponse> call, Throwable t) {
                mListener.onResponseFailed(GET_LOVE_COUNT_CINEMA, t.getMessage());
            }
        });
    }
}

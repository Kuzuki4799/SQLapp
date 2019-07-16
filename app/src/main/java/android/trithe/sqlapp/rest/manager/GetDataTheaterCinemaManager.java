package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataTheaterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDataTheaterCinemaManager {
    private ResponseCallbackListener<GetDataTheaterResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_DATA_THEATER_CINEMA = "GET_DATA_THEATER_CINEMA";

    public GetDataTheaterCinemaManager(ResponseCallbackListener<GetDataTheaterResponse> mListener) {
        this.mListener = mListener;
    }

    public void getDataTheaterCinema(int cinema_id) {
        Call<GetDataTheaterResponse> call = mRestApiManager.theaterCinemaRequestCallback()
                .getDataTheater(cinema_id);
        call.enqueue(new Callback<GetDataTheaterResponse>() {
            @Override
            public void onResponse(Call<GetDataTheaterResponse> call, Response<GetDataTheaterResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(GET_DATA_THEATER_CINEMA, response.body());
                } else {
                    mListener.onResponseFailed(GET_DATA_THEATER_CINEMA, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataTheaterResponse> call, Throwable t) {
                mListener.onResponseFailed(GET_DATA_THEATER_CINEMA, t.getMessage());
            }
        });
    }
}

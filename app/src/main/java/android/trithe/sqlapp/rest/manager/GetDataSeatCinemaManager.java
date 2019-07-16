package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataSeatResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDataSeatCinemaManager {

    private ResponseCallbackListener<GetDataSeatResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_DATA_SEAT_CINEMA = "GET_DATA_SEAT_CINEMA";

    public GetDataSeatCinemaManager(ResponseCallbackListener<GetDataSeatResponse> mListener) {
        this.mListener = mListener;
    }

    public void getDataSeatCinema(int theater_id) {
        Call<GetDataSeatResponse> call = mRestApiManager.seatCinemaRequestCallback()
                .getSeatByTime(theater_id);
        call.enqueue(new Callback<GetDataSeatResponse>() {
            @Override
            public void onResponse(Call<GetDataSeatResponse> call, Response<GetDataSeatResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(GET_DATA_SEAT_CINEMA, response.body());
                } else {
                    mListener.onResponseFailed(GET_DATA_SEAT_CINEMA, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataSeatResponse> call, Throwable t) {
                mListener.onResponseFailed(GET_DATA_SEAT_CINEMA, t.getMessage());
            }
        });
    }
}

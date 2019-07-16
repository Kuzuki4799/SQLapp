package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetAllDataShowingCinemaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_GET_SHOWING_TIME;
import static android.trithe.sqlapp.config.Config.API_GET_SHOWING_TIME_BY_DATE;

public class GetDataShowingCinemaManager {

    private ResponseCallbackListener<GetAllDataShowingCinemaResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_JOB = "GET_DATA_SHOWING_CAST";

    public GetDataShowingCinemaManager(ResponseCallbackListener<GetAllDataShowingCinemaResponse> mListener) {
        this.mListener = mListener;
    }

    public void getDataShowingCinema(int cinema_id, int film_id, String date, int status) {
                Call<GetAllDataShowingCinemaResponse> callByDate = mRestApiManager.showingCinemaRequestCallback()
                        .getShowingTimeByDate(cinema_id, film_id, date, status);
                callByDate.enqueue(new Callback<GetAllDataShowingCinemaResponse>() {
                    @Override
                    public void onResponse(Call<GetAllDataShowingCinemaResponse> call, Response<GetAllDataShowingCinemaResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(GET_JOB, response.body());
                        } else {
                            mListener.onResponseFailed(GET_JOB, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetAllDataShowingCinemaResponse> call, Throwable t) {
                        mListener.onResponseFailed(GET_JOB, t.getMessage());
                    }
                });
    }
}

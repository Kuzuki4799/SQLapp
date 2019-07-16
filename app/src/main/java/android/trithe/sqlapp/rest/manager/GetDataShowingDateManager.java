package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetAllDataShowingDateResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDataShowingDateManager {

    private ResponseCallbackListener<GetAllDataShowingDateResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_SHOWING_DATE = "GET_SHOWING_DATE";

    public GetDataShowingDateManager(ResponseCallbackListener<GetAllDataShowingDateResponse> mListener) {
        this.mListener = mListener;
    }

    public void getDataShowingDate(int cinema_id, int film_id) {
                Call<GetAllDataShowingDateResponse> call = mRestApiManager.showingCinemaRequestCallback()
                        .getShowingTime(cinema_id, film_id);
                call.enqueue(new Callback<GetAllDataShowingDateResponse>() {
                    @Override
                    public void onResponse(Call<GetAllDataShowingDateResponse> call, Response<GetAllDataShowingDateResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(GET_SHOWING_DATE, response.body());
                        } else {
                            mListener.onResponseFailed(GET_SHOWING_DATE, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetAllDataShowingDateResponse> call, Throwable t) {
                        mListener.onResponseFailed(GET_SHOWING_DATE, t.getMessage());
                    }
                });
        }

    }

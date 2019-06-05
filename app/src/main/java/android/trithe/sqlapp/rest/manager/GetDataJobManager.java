package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataJobResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetDataJobManager {

    private ResponseCallbackListener<GetDataJobResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_JOB = "GET_CAST";

    public GetDataJobManager(ResponseCallbackListener<GetDataJobResponse> mListener) {
        this.mListener = mListener;
    }
    public void startGetJobData(String cast_id) {
        Call<GetDataJobResponse> call = mRestApiManager.castRequestCallback()
                .getJob(cast_id);
        call.enqueue(new Callback<GetDataJobResponse>() {
            @Override
            public void onResponse(Call<GetDataJobResponse> call, Response<GetDataJobResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(GET_JOB, response.body());
                } else {
                    mListener.onResponseFailed(GET_JOB, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataJobResponse> call, Throwable t) {
                mListener.onResponseFailed(GET_JOB, t.getMessage());
            }
        });
    }
}

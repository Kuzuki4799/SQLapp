package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataLoveCountResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDataLoveCountManager {

    private ResponseCallbackListener<GetDataLoveCountResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_LOVE_COUNT = "GET_LOVE_COUNT";

    public GetDataLoveCountManager(ResponseCallbackListener<GetDataLoveCountResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataLoveCount(String cast_id) {
        Call<GetDataLoveCountResponse> call = mRestApiManager.lovedCastRequestCallback()
                .getCountLoveCast(cast_id);
        call.enqueue(new Callback<GetDataLoveCountResponse>() {
            @Override
            public void onResponse(Call<GetDataLoveCountResponse> call, Response<GetDataLoveCountResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(GET_LOVE_COUNT, response.body());
                } else {
                    mListener.onResponseFailed(GET_LOVE_COUNT, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataLoveCountResponse> call, Throwable t) {
                mListener.onResponseFailed(GET_LOVE_COUNT, t.getMessage());
            }
        });
    }
}

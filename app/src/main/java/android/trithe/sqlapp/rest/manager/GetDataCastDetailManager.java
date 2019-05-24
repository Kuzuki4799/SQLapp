package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataCastDetailResponse;
import android.trithe.sqlapp.rest.response.GetDataCastDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetDataCastDetailManager {

    private ResponseCallbackListener<GetDataCastDetailResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_CAST = "GET_CAST";

    public GetDataCastDetailManager(ResponseCallbackListener<GetDataCastDetailResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataCast(String id) {
        Call<GetDataCastDetailResponse> call = mRestApiManager.castRequestCallback()
                .getInfoCast(id);
        call.enqueue(new Callback<GetDataCastDetailResponse>() {
            @Override
            public void onResponse(Call<GetDataCastDetailResponse> call, Response<GetDataCastDetailResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(GET_CAST, response.body());
                } else {
                    mListener.onResponseFailed(GET_CAST, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataCastDetailResponse> call, Throwable t) {
                mListener.onResponseFailed(GET_CAST, t.getMessage());
            }
        });
    }
}

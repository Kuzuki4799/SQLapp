package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateViewCastManager {

    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String UPDATE_VIEW_CAST = "UPDATE_VIEW_CAST";

    public UpdateViewCastManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataCast(String id, String views) {
        Call<BaseResponse> call = mRestApiManager.castRequestCallback()
                .updateViewCast(id, views);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(UPDATE_VIEW_CAST, response.body());
                } else {
                    mListener.onResponseFailed(UPDATE_VIEW_CAST, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mListener.onResponseFailed(UPDATE_VIEW_CAST, t.getMessage());
            }
        });
    }
}

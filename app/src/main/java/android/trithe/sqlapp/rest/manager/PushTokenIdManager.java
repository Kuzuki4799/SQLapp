package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushTokenIdManager {

    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String PUSH_TOKEN_ID = "PUSH_TOKEN_ID";

    public PushTokenIdManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void setPushTokenId(String user_id, String token_id, String device_token) {
        Call<BaseResponse> call = mRestApiManager.loginUserRequestCallback()
                .pushTokenId(user_id, token_id, device_token);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(PUSH_TOKEN_ID, response.body());
                } else {
                    mListener.onResponseFailed(PUSH_TOKEN_ID, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mListener.onResponseFailed(PUSH_TOKEN_ID, t.getMessage());
            }
        });
    }
}

package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushForgetPassManager {

    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String PUSH_FORGET_PASS = "PUSH_FORGET_PASS";

    public PushForgetPassManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void pushForgetPass(String username) {
        Call<BaseResponse> call = mRestApiManager.loginUserRequestCallback()
                .forgetPass(username);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(PUSH_FORGET_PASS, response.body());
                } else {
                    mListener.onResponseFailed(PUSH_FORGET_PASS, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mListener.onResponseFailed(PUSH_FORGET_PASS, t.getMessage());
            }
        });
    }
}

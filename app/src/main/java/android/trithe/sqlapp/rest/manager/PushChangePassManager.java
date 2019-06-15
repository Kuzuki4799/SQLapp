package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushChangePassManager {

    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String PUSH_CHANGE_PASS = "PUSH_CHANGE_PASS";

    public PushChangePassManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void pushChangePass(String id, String current, String password) {
        Call<BaseResponse> call = mRestApiManager.loginUserRequestCallback()
                .changePass(id, current, password);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(PUSH_CHANGE_PASS, response.body());
                } else {
                    mListener.onResponseFailed(PUSH_CHANGE_PASS, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mListener.onResponseFailed(PUSH_CHANGE_PASS, t.getMessage());
            }
        });
    }
}

package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_CHANGE_IMAGE;
import static android.trithe.sqlapp.config.Config.API_CHANGE_NAME;
import static android.trithe.sqlapp.config.Config.API_CHANGE_PASS;

public class PushChangeInfoManager {

    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public PushChangeInfoManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void pushChangeInfo(String id, String current, String password, String key) {
        switch (key) {
            case API_CHANGE_NAME:
                Call<BaseResponse> callName = mRestApiManager.loginUserRequestCallback()
                        .changeName(id, current);
                callName.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_CHANGE_PASS, response.body());
                        } else {
                            mListener.onResponseFailed(API_CHANGE_PASS, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_CHANGE_PASS, t.getMessage());
                    }
                });
                break;
            case API_CHANGE_IMAGE:
                Call<BaseResponse> callImage = mRestApiManager.loginUserRequestCallback()
                        .changeImage(id, current);
                callImage.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_CHANGE_IMAGE, response.body());
                        } else {
                            mListener.onResponseFailed(API_CHANGE_IMAGE, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_CHANGE_IMAGE, t.getMessage());
                    }
                });
                break;
            case API_CHANGE_PASS:
                Call<BaseResponse> callPass = mRestApiManager.loginUserRequestCallback()
                        .changePass(id, current, password);
                callPass.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_CHANGE_PASS, response.body());
                        } else {
                            mListener.onResponseFailed(API_CHANGE_PASS, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_CHANGE_PASS, t.getMessage());
                    }
                });
                break;
        }
    }
}

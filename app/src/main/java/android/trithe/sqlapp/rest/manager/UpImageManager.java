package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpImageManager {

    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private String UPLOAD_IMAGE = "UPLOAD_IMAGE";

    public UpImageManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void startUpImage(MultipartBody.Part body) {
        Call<BaseResponse> call = mRestApiManager.loginUserRequestCallback()
                .uploadPhoto(body);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(UPLOAD_IMAGE, response.body());
                } else {
                    mListener.onResponseFailed(UPLOAD_IMAGE, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mListener.onResponseFailed(UPLOAD_IMAGE, t.getMessage());
            }
        });
    }
}

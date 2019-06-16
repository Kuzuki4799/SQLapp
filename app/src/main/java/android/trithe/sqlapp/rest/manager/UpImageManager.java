package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataImageUploadResponse;
import android.trithe.sqlapp.rest.response.GetDataImageUploadResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpImageManager {

    private ResponseCallbackListener<GetDataImageUploadResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private String UPLOAD_IMAGE = "UPLOAD_IMAGE";

    public UpImageManager(ResponseCallbackListener<GetDataImageUploadResponse> mListener) {
        this.mListener = mListener;
    }

    public void startUpImage(MultipartBody.Part body, RequestBody name) {
        Call<GetDataImageUploadResponse> call = mRestApiManager.loginUserRequestCallback()
                .uploadPhoto(body, name);
        call.enqueue(new Callback<GetDataImageUploadResponse>() {
            @Override
            public void onResponse(Call<GetDataImageUploadResponse> call, Response<GetDataImageUploadResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(UPLOAD_IMAGE, response.body());
                } else {
                    mListener.onResponseFailed(UPLOAD_IMAGE, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataImageUploadResponse> call, Throwable t) {
                mListener.onResponseFailed(UPLOAD_IMAGE, t.getMessage());
            }
        });
    }
}

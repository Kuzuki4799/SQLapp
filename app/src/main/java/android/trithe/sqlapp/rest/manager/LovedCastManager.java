package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_DELETE_LOVE_CAST;
import static android.trithe.sqlapp.config.Config.API_INSERT_LOVE_CAST;


public class LovedCastManager {

    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public LovedCastManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void startCheckSavedFilm(String user_id, String cast_id, String key) {
        switch (key) {
            case API_INSERT_LOVE_CAST:
                Call<BaseResponse> callInsert = mRestApiManager.lovedCastRequestCallback()
                        .insertLove(user_id, cast_id);
                callInsert.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_INSERT_LOVE_CAST, response.body());
                        } else {
                            mListener.onResponseFailed(API_INSERT_LOVE_CAST, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_INSERT_LOVE_CAST, t.getMessage());
                    }
                });
                break;
            case API_DELETE_LOVE_CAST:
                Call<BaseResponse> callDelete = mRestApiManager.lovedCastRequestCallback()
                        .deleteLove(user_id, cast_id);
                callDelete.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_DELETE_LOVE_CAST, response.body());
                        } else {
                            mListener.onResponseFailed(API_DELETE_LOVE_CAST, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_DELETE_LOVE_CAST, t.getMessage());
                    }
                });
                break;
        }
    }
}

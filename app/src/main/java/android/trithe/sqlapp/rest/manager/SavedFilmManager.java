package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_DELETE_SAVED;
import static android.trithe.sqlapp.config.Config.API_INSERT_SAVED;

public class SavedFilmManager {

    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public SavedFilmManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void startCheckSavedFilm(String user_id, String film_id, String key) {
        switch (key) {
            case API_INSERT_SAVED:
                Call<BaseResponse> callInsert = mRestApiManager.savedRequestCallback()
                        .getInsertSaved(user_id, film_id);
                callInsert.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_INSERT_SAVED, response.body());
                        } else {
                            mListener.onResponseFailed(API_INSERT_SAVED, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_INSERT_SAVED, t.getMessage());
                    }
                });
                break;
            case API_DELETE_SAVED:
                Call<BaseResponse> callDelete = mRestApiManager.savedRequestCallback()
                        .getDeleteSaved(user_id, film_id);
                callDelete.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_DELETE_SAVED, response.body());
                        } else {
                            mListener.onResponseFailed(API_DELETE_SAVED, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_DELETE_SAVED, t.getMessage());
                    }
                });
                break;
        }
    }
}

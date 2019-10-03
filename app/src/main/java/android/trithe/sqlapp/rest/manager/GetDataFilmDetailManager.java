package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataFilmDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDataFilmDetailManager {
    private ResponseCallbackListener<GetDataFilmDetailResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public GetDataFilmDetailManager(ResponseCallbackListener<GetDataFilmDetailResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataFilmDetail(String user_id, String id) {
        Call<GetDataFilmDetailResponse> call = mRestApiManager.filmRequestCallback()
                .getFilmById(user_id, id);
        call.enqueue(new Callback<GetDataFilmDetailResponse>() {
            @Override
            public void onResponse(Call<GetDataFilmDetailResponse> call, Response<GetDataFilmDetailResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(Config.API_GET_FILM_BY_ID, response.body());
                } else {
                    mListener.onResponseFailed(Config.API_GET_FILM_BY_ID, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataFilmDetailResponse> call, Throwable t) {
                mListener.onResponseFailed(Config.API_GET_FILM_BY_ID, t.getMessage());
            }
        });
    }
}

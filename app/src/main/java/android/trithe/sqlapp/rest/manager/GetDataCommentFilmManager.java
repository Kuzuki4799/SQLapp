package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetAllDataCommentFilmResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDataCommentFilmManager {

    private ResponseCallbackListener<GetAllDataCommentFilmResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_DATA_COMMENT_FILM = "GET_DATA_COMMENT_FILM";

    public GetDataCommentFilmManager(ResponseCallbackListener<GetAllDataCommentFilmResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataCommentFilm(String film_id) {
        Call<GetAllDataCommentFilmResponse> call = mRestApiManager.commentFilmRequestCallback()
                .getAllCommentFilm(film_id);
        call.enqueue(new Callback<GetAllDataCommentFilmResponse>() {
            @Override
            public void onResponse(Call<GetAllDataCommentFilmResponse> call, Response<GetAllDataCommentFilmResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(GET_DATA_COMMENT_FILM, response.body());
                } else {
                    mListener.onResponseFailed(GET_DATA_COMMENT_FILM, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetAllDataCommentFilmResponse> call, Throwable t) {
                mListener.onResponseFailed(GET_DATA_COMMENT_FILM, t.getMessage());
            }
        });
    }
}

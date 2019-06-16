package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushSendCommentFilmManager {

    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String PUSH_SEND_COMMENT_FILM = "PUSH_SEND_COMMENT_FILM";

    public PushSendCommentFilmManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void pushSendCommentFilm(String content, String user_id, String film_id) {
        Call<BaseResponse> call = mRestApiManager.commentFilmRequestCallback()
                .sendCommentFilm(content, user_id, film_id);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(PUSH_SEND_COMMENT_FILM, response.body());
                } else {
                    mListener.onResponseFailed(PUSH_SEND_COMMENT_FILM, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mListener.onResponseFailed(PUSH_SEND_COMMENT_FILM, t.getMessage());
            }
        });
    }
}

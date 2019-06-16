package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetAllDataCommentFilmResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CommentFilmRequestCallback {
    @FormUrlEncoded
    @POST(Config.API_COMMENT_FILM)
    Call<GetAllDataCommentFilmResponse> getAllCommentFilm(@Field("film_id") String film_id);

    @FormUrlEncoded
    @POST(Config.API_SEND_COMMENT_FILM)
    Call<BaseResponse> sendCommentFilm(@Field("content") String content,
                                       @Field("user_id") String user_id,
                                       @Field("film_id") String film_id);
}

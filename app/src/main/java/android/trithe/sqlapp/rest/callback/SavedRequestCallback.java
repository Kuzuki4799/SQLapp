package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SavedRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_CHECK_SAVED)
    Call<BaseResponse> getCheckSaved(@Field("user_id") String user_id, @Field("film_id") String film_id);

    @FormUrlEncoded
    @POST(Config.API_INSERT_SAVED)
    Call<BaseResponse> getInsertSaved(@Field("user_id") String user_id, @Field("film_id") String film_id);

    @FormUrlEncoded
    @POST(Config.API_DELETE_SAVED)
    Call<BaseResponse> getDeleteSaved(@Field("user_id") String user_id, @Field("film_id") String film_id);

}

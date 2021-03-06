package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetAllDataCastResponse;
import android.trithe.sqlapp.rest.response.GetDataCastDetailResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CastRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_GET_ALL_CAST_BY_LOVED)
    Call<GetAllDataCastResponse> getAllCastByLoved(@Field("user_id") String user_id,
                                                   @Field("page") int page,
                                                   @Field("per_page") int per_page);

    @FormUrlEncoded
    @POST(Config.API_SEARCH_CAST)
    Call<GetAllDataCastResponse> getDataSearchCast(@Field("user_id") String user_id,
                                                   @Field("name") String name,
                                                   @Field("page") int page,
                                                   @Field("per_page") int per_page);

    @FormUrlEncoded
    @POST(Config.API_CAST)
    Call<GetDataCastDetailResponse> getInfoCast(@Field("user_id") String user_id, @Field("id") String id);

    @FormUrlEncoded
    @POST(Config.UPDATE_VIEWS_CAST)
    Call<BaseResponse> updateViewCast(@Field("id") String id, @Field("views") String views);
}

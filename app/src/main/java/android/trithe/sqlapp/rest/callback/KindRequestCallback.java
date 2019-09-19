package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataKindResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface KindRequestCallback {

    @GET(Config.API_KIND)
    Call<GetDataKindResponse> getKind();

    @FormUrlEncoded
    @POST(Config.API_KIND_DETAIL)
    Call<GetDataFilmResponse> getKindDetail(@Field("user_id") String user_id,
                                            @Field("kind_id") String kind_id,
                                            @Field("page") int page,
                                            @Field("per_page") int per_page);

    @FormUrlEncoded
    @POST(Config.API_KIND_FILM_DETAIL)
    Call<GetDataKindResponse> getKindFilmDetail(@Field("film_id") String film_id);

    @FormUrlEncoded
    @POST(Config.API_DATA_KIND)
    Call<GetDataKindResponse> getDataKind(@Field("id") String id);

}

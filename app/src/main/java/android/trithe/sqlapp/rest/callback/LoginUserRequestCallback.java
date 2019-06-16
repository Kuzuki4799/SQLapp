package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataUserResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LoginUserRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_LOGIN)
    Call<GetDataUserResponse> getLoginUser(@Field("username") String username,
                                           @Field("password") String password);

    @FormUrlEncoded
    @POST(Config.API_REGISTER)
    Call<GetDataUserResponse> getRegisterUser(@Field("name") String name,
                                              @Field("username") String username,
                                              @Field("password") String password,
                                              @Field("image") String image);

    @FormUrlEncoded
    @POST(Config.API_GET_USER_BY_ID)
    Call<GetDataUserResponse> getUserById(@Field("id") String id);

    @FormUrlEncoded
    @POST(Config.API_FORGET_PASS)
    Call<BaseResponse> forgetPass(@Field("username") String username);

    @FormUrlEncoded
    @POST(Config.API_CHANGE_PASS)
    Call<BaseResponse> changePass(@Field("id") String id,
                                  @Field("current_password") String current_password,
                                  @Field("password") String password);

    @FormUrlEncoded
    @POST(Config.API_CHANGE_NAME)
    Call<BaseResponse> changeName(@Field("id") String id,
                                  @Field("new_name") String new_name);

    @Multipart
    @POST(Config.API_UPLOAD_IMG)
    Call<BaseResponse> uploadPhoto(@Part MultipartBody.Part photo);

}

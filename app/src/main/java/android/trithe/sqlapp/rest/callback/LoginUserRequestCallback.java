package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataImageUploadResponse;
import android.trithe.sqlapp.rest.response.GetDataUserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
                                              @Field("image") String image,
                                              @Field("token_id") String token_id,
                                              @Field("device_token") String device_token,
                                              @Field("notification") int notification);

    @FormUrlEncoded
    @POST(Config.API_GET_USER_BY_ID)
    Call<GetDataUserResponse> getUserById(@Field("id") String id);

    @FormUrlEncoded
    @POST(Config.API_FORGET_PASS)
    Call<BaseResponse> forgetPass(@Field("username") String username);

    @FormUrlEncoded
    @POST(Config.API_CHECK_USER)
    Call<GetDataUserResponse> checkUser(@Field("username") String username);

    @FormUrlEncoded
    @POST(Config.API_CHANGE_PASS)
    Call<BaseResponse> changePass(@Field("id") String id,
                                  @Field("current_password") String current_password,
                                  @Field("password") String password);

    @FormUrlEncoded
    @POST(Config.API_CHANGE_NAME)
    Call<BaseResponse> changeName(@Field("id") String id,
                                  @Field("new_name") String new_name);

    @FormUrlEncoded
    @POST(Config.API_CHANGE_IMAGE)
    Call<BaseResponse> changeImage(@Field("id") String id,
                                   @Field("new_image") String new_image);


    @FormUrlEncoded
    @POST(Config.API_PUSH_TOKEN_ID)
    Call<BaseResponse> pushTokenId(@Field("id") String user_id,
                                   @Field("token_id") String token_id,
                                   @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST(Config.API_PUSH_TURN_NOTIFICATION)
    Call<BaseResponse> pushTurnNotification(@Field("id") String user_id,
                                            @Field("notification") int notification);

    @Multipart
    @POST(Config.API_UPLOAD_IMG)
    Call<GetDataImageUploadResponse> uploadPhoto(@Part MultipartBody.Part file, @Part("file") RequestBody name);
}

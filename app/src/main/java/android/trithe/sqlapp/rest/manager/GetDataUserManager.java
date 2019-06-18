package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.request.DataUserInfoRequest;
import android.trithe.sqlapp.rest.response.GetDataUserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_CHECK_USER;
import static android.trithe.sqlapp.config.Config.API_GET_USER_BY_ID;
import static android.trithe.sqlapp.config.Config.API_LOGIN;
import static android.trithe.sqlapp.config.Config.API_REGISTER;

public class GetDataUserManager {

    private ResponseCallbackListener<GetDataUserResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public GetDataUserManager(ResponseCallbackListener<GetDataUserResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataInfo(DataUserInfoRequest request, String key) {
        switch (key) {
            case API_LOGIN:
                Call<GetDataUserResponse> call = mRestApiManager.loginUserRequestCallback()
                        .getLoginUser(request.getUsername(), request.getPassword());
                call.enqueue(new Callback<GetDataUserResponse>() {
                    @Override
                    public void onResponse(Call<GetDataUserResponse> call, Response<GetDataUserResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(Config.API_LOGIN, response.body());
                        } else {
                            mListener.onResponseFailed(Config.API_LOGIN, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetDataUserResponse> call, Throwable t) {
                        mListener.onResponseFailed(Config.API_LOGIN, t.getMessage());
                    }
                });
                break;
            case API_REGISTER:
                Call<GetDataUserResponse> call1 = mRestApiManager.loginUserRequestCallback()
                        .getRegisterUser(request.getName(), request.getUsername(), request.getPassword(), request.getImage());
                call1.enqueue(new Callback<GetDataUserResponse>() {
                    @Override
                    public void onResponse(Call<GetDataUserResponse> call, Response<GetDataUserResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(Config.API_REGISTER, response.body());
                        } else {
                            mListener.onResponseFailed(Config.API_REGISTER, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetDataUserResponse> call, Throwable t) {
                        mListener.onResponseFailed(Config.API_REGISTER, t.getMessage());
                    }
                });
                break;
            case API_GET_USER_BY_ID:
                Call<GetDataUserResponse> callUser = mRestApiManager.loginUserRequestCallback()
                        .getUserById(request.getName());
                callUser.enqueue(new Callback<GetDataUserResponse>() {
                    @Override
                    public void onResponse(Call<GetDataUserResponse> call, Response<GetDataUserResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(Config.API_GET_USER_BY_ID, response.body());
                        } else {
                            mListener.onResponseFailed(Config.API_GET_USER_BY_ID, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetDataUserResponse> call, Throwable t) {
                        mListener.onResponseFailed(Config.API_GET_USER_BY_ID, t.getMessage());
                    }
                });
                break;

            case API_CHECK_USER:
                Call<GetDataUserResponse> callCheckUser = mRestApiManager.loginUserRequestCallback()
                        .checkUser(request.getName());
                callCheckUser.enqueue(new Callback<GetDataUserResponse>() {
                    @Override
                    public void onResponse(Call<GetDataUserResponse> call, Response<GetDataUserResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(Config.API_CHECK_USER, response.body());
                        } else {
                            mListener.onResponseFailed(Config.API_CHECK_USER, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetDataUserResponse> call, Throwable t) {
                        mListener.onResponseFailed(Config.API_CHECK_USER, t.getMessage());
                    }
                });
                break;
        }
    }

}

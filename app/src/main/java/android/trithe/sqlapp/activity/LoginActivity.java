package android.trithe.sqlapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataUserManager;
import android.trithe.sqlapp.rest.request.DataUserInfoRequest;
import android.trithe.sqlapp.rest.response.GetDataUserResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText edUsername, edPassword;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        pDialog = new ProgressDialog(this);
    }

    private void initView() {
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);

    }

    private void showProcessDialog() {
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void disProcessDialog() {
        pDialog.isShowing();
        pDialog.dismiss();
    }

    public void onRegister(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void onLogin(View view) {
        String username = edUsername.getText().toString();
        String password = edPassword.getText().toString();
        DataUserInfoRequest dataUserInfoRequest = new DataUserInfoRequest("", username, password, "");
        if (username.equals("")) {
            Toast.makeText(LoginActivity.this, "Username rỗng", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(LoginActivity.this, "Password rỗng", Toast.LENGTH_SHORT).show();
        } else {
            showProcessDialog();
            GetDataUserManager getDataUserManager = new GetDataUserManager(new ResponseCallbackListener<GetDataUserResponse>() {
                @Override
                public void onObjectComplete(String TAG, GetDataUserResponse data) {
                    if (data.status.equals("200")) {
                        disProcessDialog();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        SharedPrefUtils.putString(Constant.KEY_USER_ID, data.result.id);
                        SharedPrefUtils.putString(Constant.KEY_USER_NAME, data.result.username);
                        SharedPrefUtils.putString(Constant.KEY_USER_PASSWORD, data.result.password);
                        SharedPrefUtils.putString(Constant.KEY_NAME_USER, data.result.name);
                        SharedPrefUtils.putString(Constant.KEY_USER_IMAGE, data.result.image);
                    }

                }

                @Override
                public void onResponseFailed(String TAG, String message) {
                    disProcessDialog();
                }
            });
            getDataUserManager.startGetDataInfo(dataUserInfoRequest, Config.API_LOGIN);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if(SharedPrefUtils.getString(Constant.KEY_USER_ID,"") != null){
//            startActivity(new Intent(LoginActivity.this,MainActivity.class));
//        }
    }
}

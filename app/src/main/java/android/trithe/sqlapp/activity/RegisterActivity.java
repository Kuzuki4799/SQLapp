package android.trithe.sqlapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataUserManager;
import android.trithe.sqlapp.rest.manager.UpImageManager;
import android.trithe.sqlapp.rest.request.DataUserInfoRequest;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataUserResponse;
import android.trithe.sqlapp.utils.FileUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterActivity extends AppCompatActivity {
    private CircleImageView img;
    private EditText edName;
    private EditText edUsername;
    private EditText edPassword;
    private EditText edConfigPassword;
    private Button btnRegister;
    private TextView txtLogin;
    private static final int GALLERY_PICK = 1;
    Uri imageUri;
    String pathData;
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        pDialog = new ProgressDialog(this);
        txtLogin.setOnClickListener(v -> finish());

        img.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"), GALLERY_PICK);
        });

        btnRegister.setOnClickListener(v -> {
//            uploadImage();
                register();
        });
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


    private void register() {
        String name = edName.getText().toString();
        String username = edUsername.getText().toString();
        String password = edPassword.getText().toString();
        String configPassword = edConfigPassword.getText().toString();
        DataUserInfoRequest dataUserInfoRequest = new DataUserInfoRequest(name, username, password, imageUri.toString());
        if (name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Name rỗng", Toast.LENGTH_SHORT).show();
        } else if (username.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Username rỗng", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Password rỗng", Toast.LENGTH_SHORT).show();
        } else if (configPassword.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Password rỗng", Toast.LENGTH_SHORT).show();
        } else if (!configPassword.equals(password)) {
            Toast.makeText(RegisterActivity.this, "Password phải giống với Config Passoword", Toast.LENGTH_SHORT).show();
        } else {
            showProcessDialog();
            GetDataUserManager getDataUserManager = new GetDataUserManager(new ResponseCallbackListener<GetDataUserResponse>() {
                @Override
                public void onObjectComplete(String TAG, GetDataUserResponse data) {
                    if (data.status.equals("200")) {
                        disProcessDialog();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
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
            getDataUserManager.startGetDataInfo(dataUserInfoRequest, Config.API_REGISTER);
        }
    }

//    public void uploadImage() {
//        final File file = new File(pathData);
//        String file_path = file.getAbsolutePath();
//        String[] arrayNameFile = file_path.split("\\.");
//        file_path = arrayNameFile[0] + System.currentTimeMillis() + "." + arrayNameFile[1];
//        Toast.makeText(RegisterActivity.this, file_path, Toast.LENGTH_LONG).show();
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("uploads", file_path, requestBody);
//
//        UpImageManager upImageManager = new UpImageManager(new ResponseCallbackListener<BaseResponse>() {
//            @Override
//            public void onObjectComplete(String TAG, BaseResponse data) {
//                if (data.status.equals("200")) {
//                    Toast.makeText(RegisterActivity.this,"Ok", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onResponseFailed(String TAG, String message) {
//                Log.d("error",message);
//            }
//        });
//        upImageManager.startUpImage(body);
//
//
//    }

    private void initView() {
        img = findViewById(R.id.img);
        edName = findViewById(R.id.edName);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        edConfigPassword = findViewById(R.id.edConfigPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            imageUri = data.getData();
            img.setImageURI(imageUri);
//            pathData = FileUtils.getPath(RegisterActivity.this, data.getData());
        }
    }
}

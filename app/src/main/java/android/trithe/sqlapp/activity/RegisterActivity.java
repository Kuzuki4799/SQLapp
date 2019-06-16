package android.trithe.sqlapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.trithe.sqlapp.rest.response.GetDataImageUploadResponse;
import android.trithe.sqlapp.rest.response.GetDataUserResponse;
import android.trithe.sqlapp.utils.FileUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.utils.Utils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterActivity extends AppCompatActivity {
    private CircleImageView img;
    private ImageView imgBack;
    private EditText edName;
    private EditText edUsername;
    private EditText edPassword;
    private EditText edConfigPassword;
    private Button btnRegister;
    private TextView txtLogin;
    Uri imageUri;
    String pathData, name, username, password, configPassword;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        pDialog = new ProgressDialog(this);
    }

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(RegisterActivity.this);
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
        name = edName.getText().toString();
        username = edUsername.getText().toString();
        password = edPassword.getText().toString();
        configPassword = edConfigPassword.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, R.string.new_name_null, Toast.LENGTH_SHORT).show();
        } else if (!Utils.isValidEmail(username)) {
            Toast.makeText(getApplicationContext(), R.string.email_error, Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, R.string.curent_pass_null, Toast.LENGTH_SHORT).show();
        } else if (configPassword.isEmpty()) {
            Toast.makeText(RegisterActivity.this, R.string.config_current_pass_null, Toast.LENGTH_SHORT).show();
        } else if (!configPassword.equals(password)) {
            Toast.makeText(RegisterActivity.this, R.string.same_pass_error, Toast.LENGTH_SHORT).show();
        } else {
            uploadImage();
        }
    }

    public void uploadImage() {
        File file = new File(FileUtils.getPath(imageUri, this));
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        UpImageManager upImageManager = new UpImageManager(new ResponseCallbackListener<GetDataImageUploadResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataImageUploadResponse data) {
                if (data.status.equals("200")) {
                    callApiRegister(data.result.replace("uploads/", ""));
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                Log.d("error", message);
            }
        });
        upImageManager.startUpImage(fileToUpload, filename);
    }

    private void callApiRegister(String image) {
        showProcessDialog();
        DataUserInfoRequest dataUserInfoRequest = new DataUserInfoRequest(name, username, password, image);
        GetDataUserManager getDataUserManager = new GetDataUserManager(new ResponseCallbackListener<GetDataUserResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataUserResponse data) {
                if (data.status.equals("200")) {
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    SharedPrefUtils.putString(Constant.KEY_USER_ID, data.result.id);
                    SharedPrefUtils.putString(Constant.KEY_USER_NAME, data.result.username);
                    SharedPrefUtils.putString(Constant.KEY_USER_PASSWORD, data.result.password);
                    SharedPrefUtils.putString(Constant.KEY_NAME_USER, data.result.name);
                    SharedPrefUtils.putString(Constant.KEY_USER_IMAGE, data.result.image);
                    setResult(RESULT_OK);
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getDataUserManager.startGetDataInfo(dataUserInfoRequest, Config.API_REGISTER);
    }

    private void initView() {
        img = findViewById(R.id.img);
        edName = findViewById(R.id.edName);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        edConfigPassword = findViewById(R.id.edConfigPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);
        imgBack = findViewById(R.id.imgBack);

        txtLogin.setOnClickListener(v -> onBackPressed());
        img.setOnClickListener(v -> BringImagePicker());
        imgBack.setOnClickListener(v -> finish());
        btnRegister.setOnClickListener(v -> {
            register();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = Objects.requireNonNull(result).getUri();
                img.setImageURI(imageUri);
            }
        }
    }
}

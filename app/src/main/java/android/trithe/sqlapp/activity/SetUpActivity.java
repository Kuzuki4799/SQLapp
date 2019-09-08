package android.trithe.sqlapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.aplication.AppSharedPreferences;
import android.trithe.sqlapp.aplication.MyApplication;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataUserManager;
import android.trithe.sqlapp.rest.manager.UpImageManager;
import android.trithe.sqlapp.rest.request.DataUserInfoRequest;
import android.trithe.sqlapp.rest.response.GetDataImageUploadResponse;
import android.trithe.sqlapp.rest.response.GetDataUserResponse;
import android.trithe.sqlapp.utils.FileUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SetUpActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView img;
    private EditText edName;
    private Button btnRegister;
    private ImageView btnBack;
    private ProgressDialog pDialog;
    private String token;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        pDialog = new ProgressDialog(this);
        initView();
        listener();
        token = AppSharedPreferences.getInstance(
                MyApplication.with(this).getSharedPreferencesApp()).getmFirebaseToken();
    }

    private void bringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(SetUpActivity.this);
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
        DataUserInfoRequest dataUserInfoRequest = new DataUserInfoRequest(edName.getText().toString(),
                getIntent().getStringExtra(Constant.KEY_USER_NAME),
                "null",
                image,
                token, String.valueOf(MyApplication.getID()),
                0);
        GetDataUserManager getDataUserManager = new GetDataUserManager(new ResponseCallbackListener<GetDataUserResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataUserResponse data) {
                if (data.status.equals("200")) {
                    if (getIntent().getStringExtra(Constant.KEY_CHECK).equals(Constant.FACEBOOK)) {
                        SharedPrefUtils.putString(Constant.KEY_CHECK_LOGIN, Constant.FACEBOOK);
                    } else {
                        SharedPrefUtils.putString(Constant.KEY_CHECK_LOGIN, Constant.GOOGLE);
                    }
                    SharedPrefUtils.putBoolean(Constant.REGISTER, true);
                    SharedPrefUtils.putString(Constant.KEY_USER_ID, data.result.id);
                    SharedPrefUtils.putString(Constant.KEY_USER_NAME, data.result.username);
                    SharedPrefUtils.putString(Constant.KEY_USER_PASSWORD, data.result.password);
                    SharedPrefUtils.putString(Constant.KEY_NAME_USER, data.result.name);
                    SharedPrefUtils.putString(Constant.KEY_USER_IMAGE, data.result.image);
                    setResult(RESULT_OK);
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

    private void listener() {
        img.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void initView() {
        img = findViewById(R.id.img);
        edName = findViewById(R.id.edName);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        edName.setText(getIntent().getStringExtra(Constant.KEY_NAME_USER));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.img:
                bringImagePicker();
                break;
            case R.id.btnRegister:
                if (imageUri != null) {
                    uploadImage();
                } else {
                    Toast.makeText(SetUpActivity.this, R.string.not_avatar_image, Toast.LENGTH_SHORT).show();
                }
                break;
        }
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

package android.trithe.sqlapp.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.ChangePassActivity;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.PushChangeInfoManager;
import android.trithe.sqlapp.rest.manager.UpImageManager;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataImageUploadResponse;
import android.trithe.sqlapp.utils.FileUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.utils.Utils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment implements View.OnClickListener {
    private ImageView imgAvatar;
    private TextView txtNameUser;
    private TextView txtEmailUser;
    private TextView txtUserEmail;
    private TextView txtPass;
    private ImageView imgEditPass;
    private ImageView iconAccount;
    private RelativeLayout rlPassword;
    private ImageView imgChangeName;
    private ImageView imgEditImage;
    private Uri mainImageURI = null;
    private RelativeLayout rlAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_account, container, false);
        initView(view);
        initData();
        listener();
        return view;
    }

    private void initView(View view) {
        rlAccount = view.findViewById(R.id.rlAccount);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        txtNameUser = view.findViewById(R.id.txtNameUser);
        txtEmailUser = view.findViewById(R.id.txtEmailUser);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);
        txtPass = view.findViewById(R.id.txtPass);
        imgEditPass = view.findViewById(R.id.imgEditPass);
        imgChangeName = view.findViewById(R.id.imgChangeName);
        imgEditImage = view.findViewById(R.id.imgEditImage);
        iconAccount = view.findViewById(R.id.icon_account);
        rlPassword = view.findViewById(R.id.rlPassword);
    }

    private void initData() {
        Glide.with(Objects.requireNonNull(getActivity())).load(Config.LINK_LOAD_IMAGE + SharedPrefUtils.getString(Constant.KEY_USER_IMAGE, "")).into(imgAvatar);
        txtNameUser.setText(SharedPrefUtils.getString(Constant.KEY_NAME_USER, ""));
        txtEmailUser.setText(SharedPrefUtils.getString(Constant.KEY_USER_NAME, ""));
        txtUserEmail.setText(SharedPrefUtils.getString(Constant.KEY_USER_NAME, ""));
        txtPass.setText(SharedPrefUtils.getString(Constant.KEY_USER_PASSWORD, ""));
        if (SharedPrefUtils.getString(Constant.KEY_CHECK_LOGIN, "").equals(Constant.FACEBOOK)) {
            rlPassword.setVisibility(View.GONE);
            Glide.with(getActivity()).load(R.drawable.fb_icon).into(iconAccount);
            rlAccount.setOnClickListener(v -> Utils.infoFacebook(getActivity()));
        } else if (SharedPrefUtils.getString(Constant.KEY_CHECK_LOGIN, "").equals(Constant.GOOGLE)) {
            rlPassword.setVisibility(View.GONE);
            Glide.with(Objects.requireNonNull(getActivity())).load(R.drawable.google_icon).into(iconAccount);
        }
    }

    private void listener() {
        imgEditPass.setOnClickListener(this);
        imgChangeName.setOnClickListener(this);
        imgEditImage.setOnClickListener(this);
    }

    private void bringImagePicker() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                .start(Objects.requireNonNull(getContext()), this);
    }

    private void showDialogChangeName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        SpannableStringBuilder ssBuilder =
                new SpannableStringBuilder(getResources().getString(R.string.change_your_name));
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);
        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                getResources().getString(R.string.change_your_name).length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        builder.setTitle(ssBuilder);
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_info, (ViewGroup) getView(), false);
        final EditText editText = viewInflated.findViewById(R.id.edChange);
        editText.setText(SharedPrefUtils.getString(Constant.KEY_NAME_USER, ""));
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (!editText.getText().toString().isEmpty()) {
                        callApiChangeName(editText, dialog);
                    } else {
                        Toast.makeText(getContext(), R.string.new_name_null, Toast.LENGTH_SHORT).show();
                    }
                    Utils.hideKeyboard(getActivity());
                }
        );
        builder.setNegativeButton(R.string.str_cancel, (dialog, which) -> {
            Utils.hideKeyboard(getActivity());
            dialog.cancel();
        });
        builder.show();
    }

    private void callApiChangeName(EditText editText, DialogInterface dialog) {
        new PushChangeInfoManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    Toast.makeText(getContext(), R.string.change_name_successful, Toast.LENGTH_SHORT).show();
                    SharedPrefUtils.putString(Constant.KEY_NAME_USER, editText.getText().toString());
                    txtNameUser.setText(SharedPrefUtils.getString(Constant.KEY_NAME_USER, ""));
                    dialog.dismiss();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        }).pushChangeInfo(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), editText.getText().toString(), null, Config.API_CHANGE_NAME);
    }

    @Override
    public void onResume() {
        super.onResume();
        txtPass.setText(SharedPrefUtils.getString(Constant.KEY_USER_PASSWORD, ""));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = Objects.requireNonNull(result).getUri();
                imgAvatar.setImageURI(mainImageURI);
                uploadImage();
            }
        }

        if (requestCode == 999 && resultCode == RESULT_OK){
            Toast.makeText(getContext(), R.string.change_pass_success, Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadImage() {
        File file = new File(FileUtils.getPath(mainImageURI, getContext()));
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        new UpImageManager(new ResponseCallbackListener<GetDataImageUploadResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataImageUploadResponse data) {
                if (data.status.equals("200")) {
                    callApiChangeImage(data.result.replace("uploads/", ""));
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
            }
        }).startUpImage(fileToUpload, filename);
    }

    private void callApiChangeImage(String image) {
        new PushChangeInfoManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    SharedPrefUtils.putBoolean(Constant.REGISTER, false);
                    SharedPrefUtils.putString(Constant.KEY_USER_IMAGE, image);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
            }
        }).pushChangeInfo(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), image, null, Config.API_CHANGE_IMAGE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgEditPass:
                startActivityForResult(new Intent(getContext(), ChangePassActivity.class), 999);
                break;
            case R.id.imgChangeName:
                showDialogChangeName();
                break;
            case R.id.imgEditImage:
                bringImagePicker();
                break;
        }
    }
}

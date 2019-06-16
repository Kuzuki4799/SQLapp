package android.trithe.sqlapp.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {
    private ImageView imgAvatar;
    private TextView txtNameUser;
    private TextView txtEmailUser;
    private TextView txtUserEmail;
    private TextView txtPass;
    private ImageView imgEditPass;
    private ImageView imgChangeName;
    private ImageView imgEditImage;
    private Uri mainImageURI = null;
    private ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_account, container, false);
        initView(view);
        pDialog = new ProgressDialog(getContext());
        return view;
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

    private void initView(View view) {
        imgAvatar = view.findViewById(R.id.imgAvatar);
        txtNameUser = view.findViewById(R.id.txtNameUser);
        txtEmailUser = view.findViewById(R.id.txtEmailUser);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);
        txtPass = view.findViewById(R.id.txtPass);
        imgEditPass = view.findViewById(R.id.imgEditPass);
        imgChangeName = view.findViewById(R.id.imgChangeName);
        imgEditImage = view.findViewById(R.id.imgEditImage);

        Glide.with(Objects.requireNonNull(getActivity())).load(Config.LINK_LOAD_IMAGE + SharedPrefUtils.getString(Constant.KEY_USER_IMAGE, "")).into(imgAvatar);
        txtNameUser.setText(SharedPrefUtils.getString(Constant.KEY_NAME_USER, ""));
        txtEmailUser.setText(SharedPrefUtils.getString(Constant.KEY_USER_NAME, ""));
        txtUserEmail.setText(SharedPrefUtils.getString(Constant.KEY_USER_NAME, ""));
        txtPass.setText(SharedPrefUtils.getString(Constant.KEY_USER_PASSWORD, ""));

        imgEditPass.setOnClickListener(v -> startActivity(new Intent(getContext(), ChangePassActivity.class)));
        imgChangeName.setOnClickListener(v -> showDialogChangeName());
        imgEditImage.setOnClickListener(v -> BringImagePicker());
    }

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(Objects.requireNonNull(getActivity()));
    }

    private void showDialogChangeName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder("Change Your Name");
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);
        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                "Change Your Name".length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        builder.setTitle(ssBuilder);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_info, (ViewGroup) getView(), false);
        final EditText editText = viewInflated.findViewById(R.id.edChange);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (!editText.getText().toString().isEmpty()) {
                        callApiChangeName(editText, dialog);
                    } else {
                        Toast.makeText(getContext(), R.string.new_name_null, Toast.LENGTH_SHORT).show();
                    }

                }
        );
        builder.setNegativeButton(R.string.str_cancel, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void callApiChangeName(EditText editText, DialogInterface dialog) {
        showProcessDialog();
        PushChangeInfoManager pushChangeInfoManager = new PushChangeInfoManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    Toast.makeText(getContext(), R.string.change_name_successful, Toast.LENGTH_SHORT).show();
                    SharedPrefUtils.putString(Constant.KEY_NAME_USER, editText.getText().toString());
                    txtNameUser.setText(SharedPrefUtils.getString(Constant.KEY_NAME_USER, ""));
                    dialog.dismiss();
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        pushChangeInfoManager.pushChangeInfo(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), editText.getText().toString(), null, Config.API_CHANGE_NAME);
    }

    @Override
    public void onResume() {
        super.onResume();
        txtPass.setText(SharedPrefUtils.getString(Constant.KEY_USER_PASSWORD, ""));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = Objects.requireNonNull(result).getUri();
                imgAvatar.setImageURI(mainImageURI);
            }
        }
    }
}

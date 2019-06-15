package android.trithe.sqlapp.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.ChangePassActivity;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class AccountFragment extends Fragment {
    private ImageView imgAvatar;
    private TextView txtNameUser;
    private TextView txtEmailUser;
    private TextView txtUserEmail;
    private TextView txtPass;
    private ImageView imgEditPass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_account, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        imgAvatar = view.findViewById(R.id.imgAvatar);
        txtNameUser = view.findViewById(R.id.txtNameUser);
        txtEmailUser = view.findViewById(R.id.txtEmailUser);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);
        txtPass = view.findViewById(R.id.txtPass);
        imgEditPass = view.findViewById(R.id.imgEditPass);

        Glide.with(Objects.requireNonNull(getActivity())).load(Config.LINK_LOAD_IMAGE + SharedPrefUtils.getString(Constant.KEY_USER_IMAGE, "")).into(imgAvatar);
        txtNameUser.setText(SharedPrefUtils.getString(Constant.KEY_NAME_USER, ""));
        txtEmailUser.setText(SharedPrefUtils.getString(Constant.KEY_USER_NAME, ""));
        txtUserEmail.setText(SharedPrefUtils.getString(Constant.KEY_USER_NAME, ""));
        txtPass.setText(SharedPrefUtils.getString(Constant.KEY_USER_PASSWORD, ""));

        imgEditPass.setOnClickListener(v -> startActivity(new Intent(getContext(), ChangePassActivity.class)));
    }

    @Override
    public void onResume() {
        super.onResume();
        txtPass.setText(SharedPrefUtils.getString(Constant.KEY_USER_PASSWORD, ""));
    }
}

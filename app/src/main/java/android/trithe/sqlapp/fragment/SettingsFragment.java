package android.trithe.sqlapp.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.FeedbackActivity;
import android.trithe.sqlapp.activity.LockPassActivity;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.FeedBackAppManager;
import android.trithe.sqlapp.rest.manager.GetDataUserManager;
import android.trithe.sqlapp.rest.manager.PushTurnNotificationManager;
import android.trithe.sqlapp.rest.request.DataUserInfoRequest;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataUserResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    private Switch swNotification;
    private RelativeLayout llLockApp, rlFeedback;
    public static final int REQUEST_LOCK_PASS = 999;
    public static final int REQUEST_FEEDBACK = 1000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);
        checkNotificationUser();
        return view;
    }

    private void checkNotificationUser() {
        DataUserInfoRequest dataUserInfoRequest = new DataUserInfoRequest(SharedPrefUtils.getString(Constant.KEY_USER_NAME, ""), null, null, null, null, null, 0);
        new GetDataUserManager(new ResponseCallbackListener<GetDataUserResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataUserResponse data) {
                if (data.status.equals("200")) {
                    if (data.result.notification == 1) {
                        swNotification.setChecked(false);
                    } else {
                        swNotification.setChecked(true);
                    }
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
            }
        }).startGetDataInfo(dataUserInfoRequest, Config.API_CHECK_USER);
    }

    private void initView(View view) {
        swNotification = view.findViewById(R.id.swNotification);
        llLockApp = view.findViewById(R.id.llLockApp);
        rlFeedback = view.findViewById(R.id.rlFeedback);

        llLockApp.setOnClickListener(this);
        rlFeedback.setOnClickListener(this);
        swNotification.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FEEDBACK) {
                Toast.makeText(getContext(), R.string.send_feed_back_successfully, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llLockApp:
                Intent intents = new Intent(getContext(), LockPassActivity.class);
                startActivityForResult(intents, REQUEST_LOCK_PASS);
                break;
            case R.id.rlFeedback:
                checkFeedback();
                break;
            case R.id.swNotification:
                int notification = 0;
                if (!swNotification.isChecked()) {
                    notification = 1;
                }
                new PushTurnNotificationManager(new ResponseCallbackListener<BaseResponse>() {
                    @Override
                    public void onObjectComplete(String TAG, BaseResponse data) {
                        if (data.status.equals("200")) {
                            checkNotificationUser();
                        }
                    }

                    @Override
                    public void onResponseFailed(String TAG, String message) {
                    }
                }).setPushTokenId(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), notification);
                break;
        }
    }

    private void checkFeedback() {
        new FeedBackAppManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.feedbacked), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getContext(), FeedbackActivity.class);
                    startActivityForResult(intent, REQUEST_FEEDBACK);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        }).feedBackApp(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_CHECK_FEED_BACK);
    }
}

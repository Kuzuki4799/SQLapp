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
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.utils.Utils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Switch swNotification;
    private RelativeLayout llLockApp, rlFeedback;
    public static final int REQUEST_LOCK_PASS = 999;
    public static final int REQUEST_FEEDBACK = 1000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        swNotification = view.findViewById(R.id.swNotification);
        llLockApp = view.findViewById(R.id.llLockApp);
        rlFeedback = view.findViewById(R.id.rlFeedback);

        llLockApp.setOnClickListener(this);
        rlFeedback.setOnClickListener(this);
        swNotification.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.swNotification) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FEEDBACK) {
                Utils.showAlertDialog1(getActivity(), R.string.notification, R.string.send_feed_back_successfully);
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
        }
    }

    private void checkFeedback() {
        FeedBackAppManager feedBackAppManager = new FeedBackAppManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    Utils.showAlertDialog1(getActivity(), R.string.notification, R.string.feedbacked);
                } else {
                    Intent intent = new Intent(getContext(), FeedbackActivity.class);
                    startActivityForResult(intent, REQUEST_FEEDBACK);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        feedBackAppManager.feedBackApp(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_CHECK_FEED_BACK);
    }
}

package android.trithe.sqlapp.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.utils.Utils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.util.Objects;

public class LockPassActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ImageView btnBack;
    private Switch swLockPass, swFinger;
    private String strLockFingerprint;
    private RelativeLayout llChange;
    public static final int REQUEST_LOCK_PASS = 999;
    public static final int REQUEST_CHANGE_PASS = 998;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_pass);
        initView();
        strLockFingerprint = SharedPrefUtils.getString(Constant.KEY_CHECK_FINGER_PRINT, "");
        if (!SharedPrefUtils.getString(Constant.KEY_LOCK, "").isEmpty()) {
            swLockPass.setChecked(true);
        }
        checkEnabledSwFinger();
    }

    private void checkEnabledSwFinger() {
        if (!swLockPass.isChecked()) {
            swFinger.setEnabled(false);
            llChange.setEnabled(false);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                FingerprintManager fpManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
                if (fpManager != null) {
                    if (fpManager.isHardwareDetected()) {
                        swFinger.setEnabled(true);
                    }
                    if (!strLockFingerprint.isEmpty()) {
                        swFinger.setChecked(true);
                    } else {
                        swFinger.setChecked(false);
                    }
                }else {
                    swFinger.setEnabled(false);
                }
            }
            llChange.setEnabled(true);
        }
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        swLockPass = findViewById(R.id.swLockPass);
        llChange = findViewById(R.id.llChange);
        swFinger = findViewById(R.id.swFinger);

        swFinger.setOnCheckedChangeListener(this);
        swLockPass.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        llChange.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.swFinger) {
            if (swFinger.isChecked()) {
                SharedPrefUtils.putString(Constant.KEY_CHECK_FINGER_PRINT, Constant.NB1);
            } else {
                SharedPrefUtils.putString(Constant.KEY_CHECK_FINGER_PRINT, "");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.llChange:
                Intent intents = new Intent(this, PassLockActivity.class);
                intents.putExtra(Constant.KEY_CHECK_CHANGE, true);
                startActivityForResult(intents, REQUEST_CHANGE_PASS);
                break;
            case R.id.swLockPass:
                Intent intentSw = new Intent(this, PassLockActivity.class);
                startActivityForResult(intentSw, REQUEST_LOCK_PASS);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOCK_PASS) {
                checkSwitch();
                Utils.showAlertDialog1(LockPassActivity.this, R.string.notification, R.string.create_lock_successful);
            }

            if (requestCode == REQUEST_CHANGE_PASS) {
                checkSwitch();
                Utils.showAlertDialog1(LockPassActivity.this, R.string.notification, R.string.change_lock_successful);
            }
        }
    }

    private void checkSwitch() {
        if (!SharedPrefUtils.getString(Constant.KEY_LOCK, "").isEmpty()) {
            swLockPass.setChecked(true);
        } else {
            swLockPass.setChecked(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSwitch();
        checkEnabledSwFinger();
    }
}

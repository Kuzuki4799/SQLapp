package android.trithe.sqlapp.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

public class LockPassActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ImageView btnBack;
    private Switch swLockPass;
    private RelativeLayout llChange;
    private Switch swFinger;
    public static final int REQUEST_LOCK_PASS = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_pass);
        initView();
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
            swFinger.setEnabled(true);
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
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.llChange:
                Intent intents = new Intent(this, PassActivity.class);
                intents.putExtra(Constant.KEY_CHECK_CHANGE, true);
                startActivityForResult(intents, REQUEST_LOCK_PASS);
                break;
            case R.id.swLockPass:
                Intent intentSw = new Intent(this, PassActivity.class);
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

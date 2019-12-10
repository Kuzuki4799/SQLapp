package android.trithe.sqlapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.PushChangeInfoManager;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ChangePassActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView btnBack;
    private EditText edPassOld;
    private ImageView btnClearOld;
    private EditText edPass;
    private ImageView btnClearPass;
    private EditText edConfigPass;
    private ImageView btnClearConfig;
    private Button btnChange;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        initView();
        listener();
        checkClear(edPassOld, btnClearOld);
        checkClear(edPass, btnClearPass);
        checkClear(edConfigPass, btnClearConfig);
        checkActionDone();
    }

    private void listener() {
        btnBack.setOnClickListener(this);
        btnClearOld.setOnClickListener(this);
        btnClearPass.setOnClickListener(this);
        btnClearConfig.setOnClickListener(this);
        btnChange.setOnClickListener(this);
    }

    private void showProcessDialog() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void disProcessDialog() {
        progressBar.setVisibility(View.GONE);
    }

    private void checkActionDone() {
        edConfigPass.setOnEditorActionListener((v, actionId, event) ->
        {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                validateForm();
            }
            return false;
        });
    }

    private void validateForm() {
        if (edPassOld.getText().toString().isEmpty()) {
            Toast.makeText(ChangePassActivity.this, R.string.curent_pass_null, Toast.LENGTH_SHORT).show();
        } else if (edPass.getText().toString().length() < 6) {
            Toast.makeText(ChangePassActivity.this, R.string.length_pass_error, Toast.LENGTH_SHORT).show();
        } else if (!edPass.getText().toString().equals(edConfigPass.getText().toString())) {
            Toast.makeText(ChangePassActivity.this, R.string.same_pass_error, Toast.LENGTH_SHORT).show();
        } else {
            pushChangePassword();
        }
    }

    private void pushChangePassword() {
        showProcessDialog();
        new PushChangeInfoManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    setResult(RESULT_OK);
                    finish();
                    SharedPrefUtils.putString(Constant.KEY_USER_PASSWORD, edPass.getText().toString());
                } else {
                    Toast.makeText(ChangePassActivity.this, R.string.curent_pass_error, Toast.LENGTH_SHORT).show();
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        }).pushChangeInfo(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), edPassOld.getText().toString(), edPass.getText().toString(), Config.API_CHANGE_PASS);
    }

    private void checkClear(EditText editText, final ImageView imageView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                if (after > 0) {
                    imageView.setVisibility(View.VISIBLE);
                } else if (start == 0) {
                    imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        edPassOld = findViewById(R.id.edLockOld);
        progressBar = findViewById(R.id.progressBar);
        btnClearOld = findViewById(R.id.btnClearOld);
        edPass = findViewById(R.id.edLockPass);
        btnClearPass = findViewById(R.id.btnClearPass);
        edConfigPass = findViewById(R.id.edConfigPass);
        btnClearConfig = findViewById(R.id.btnClearConfig);
        btnChange = findViewById(R.id.btnChange);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnClearOld:
                edPassOld.setText("");
                break;
            case R.id.btnClearPass:
                edPass.setText("");
                break;
            case R.id.btnClearConfig:
                edConfigPass.setText("");
                break;
            case R.id.btnChange:
                validateForm();
                break;
        }
    }
}

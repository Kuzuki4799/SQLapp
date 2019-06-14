package android.trithe.sqlapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PassActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView btnBack;
    private EditText edLockOld;
    private ImageView btnClearOld;
    private EditText edLockPass;
    private ImageView btnClearPass;
    private EditText edConfigPass;
    private ImageView btnClearConfig;
    private Button btnLogin;
    private RelativeLayout llOld;
    private RelativeLayout llLock;
    private RelativeLayout llConfig;
    private TextView txtTitle;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);
        initView();
        bundle = getIntent().getExtras();
        checkClearSearch(edLockOld, btnClearOld);
        checkClearSearch(edLockPass, btnClearPass);
        checkClearSearch(edConfigPass, btnClearConfig);
        checkActionDone();
        checkBundle();
    }

    private void checkBundle() {
        if (bundle != null) {
            llOld.setVisibility(View.VISIBLE);
            txtTitle.setText(R.string.edit_lock);
        } else {
            if (!SharedPrefUtils.getString(Constant.KEY_LOCK, "").isEmpty()) {
                txtTitle.setText(R.string.un_lock);
                llOld.setVisibility(View.VISIBLE);
                llLock.setVisibility(View.GONE);
                llConfig.setVisibility(View.GONE);
            } else {
                txtTitle.setText(R.string.create_lock);
            }
        }
    }

    private void initView() {
        llOld = findViewById(R.id.llOld);
        llLock = findViewById(R.id.llLock);
        llConfig = findViewById(R.id.llConfig);
        btnBack = findViewById(R.id.btnBack);
        btnLogin = findViewById(R.id.btnLogin);
        txtTitle = findViewById(R.id.txtTitle);
        edLockOld = findViewById(R.id.edLockOld);
        btnClearOld = findViewById(R.id.btnClearOld);
        edLockPass = findViewById(R.id.edLockPass);
        btnClearPass = findViewById(R.id.btnClearPass);
        edConfigPass = findViewById(R.id.edConfigPass);
        btnClearConfig = findViewById(R.id.btnClearConfig);

        btnBack.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnClearOld.setOnClickListener(this);
        btnClearPass.setOnClickListener(this);
        btnClearConfig.setOnClickListener(this);
    }

    private void checkActionDone() {
        if (bundle == null) {
            if (!SharedPrefUtils.getString(Constant.KEY_LOCK, "").isEmpty()) {
                edLockOld.setOnEditorActionListener((v, actionId, event) ->
                {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        SharedPrefUtils.putString(Constant.KEY_LOCK, null);
                        finish();
                        setResult(RESULT_OK);
                    }
                    return false;
                });
            }
        }

        edConfigPass.setOnEditorActionListener((v, actionId, event) ->
        {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                validateForm();
            }
            return false;
        });
    }

    private void checkClearSearch(EditText editText, final ImageView imageView) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnClearOld:
                edLockOld.setText("");
                break;
            case R.id.btnClearPass:
                edLockPass.setText("");
                break;
            case R.id.btnClearConfig:
                edConfigPass.setText("");
                break;
            case R.id.btnLogin:
                validateForm();
                break;
        }
    }


    private void validateForm() {
        if (llOld.getVisibility() == View.VISIBLE && !edLockOld.getText().toString().equals(SharedPrefUtils.getString(Constant.KEY_LOCK, ""))) {
            Toast.makeText(PassActivity.this, R.string.curent_pass_error, Toast.LENGTH_SHORT).show();
        } else if (llLock.getVisibility() == View.VISIBLE && edLockPass.getText().toString().length() < 6) {
            Toast.makeText(PassActivity.this, R.string.length_pass_error, Toast.LENGTH_SHORT).show();
        } else if (llConfig.getVisibility() == View.VISIBLE && !edLockPass.getText().toString().equals(edConfigPass.getText().toString())) {
            Toast.makeText(PassActivity.this, R.string.same_pass_error, Toast.LENGTH_SHORT).show();
        } else {
            finish();
            setResult(RESULT_OK);
            SharedPrefUtils.putString(Constant.KEY_LOCK, edConfigPass.getText().toString());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (bundle == null) {
            if (SharedPrefUtils.getString(Constant.KEY_LOCK, "").isEmpty()) {
                SharedPrefUtils.putString(Constant.KEY_LOCK, null);
            }
            finish();
            setResult(RESULT_OK);
        }
    }
}

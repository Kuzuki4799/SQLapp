package android.trithe.sqlapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.PushForgetPassManager;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.utils.Utils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ForgetPassActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView btnBack;
    private EditText edEmail;
    private Button btnSend;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        initView();
        listener();
    }

    private void listener() {
        btnBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    private void showProcessDialog() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void disProcessDialog() {
        progressBar.setVisibility(View.GONE);
    }

    private void pushRequest() {
        showProcessDialog();
        PushForgetPassManager pushForgetPassManager = new PushForgetPassManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    Toast.makeText(getApplicationContext(), R.string.send_successfully, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    disProcessDialog();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        pushForgetPassManager.pushForgetPass(edEmail.getText().toString());
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        edEmail = findViewById(R.id.edEmail);
        btnSend = findViewById(R.id.btnSend);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnSend:
                if (Utils.isValidEmail(edEmail.getText().toString())) {
                    pushRequest();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.email_error), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

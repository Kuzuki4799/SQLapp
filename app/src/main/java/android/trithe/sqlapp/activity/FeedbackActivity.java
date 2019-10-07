package android.trithe.sqlapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.FeedBackAppManager;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView btnBack;
    private EditText edFeedback;
    private Button btnSend;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_acitivty);
        initView();
        listener();
    }

    private void listener() {
        btnBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    private void validateForm() {
        if (edFeedback.getText().toString().isEmpty()) {
            Toast.makeText(FeedbackActivity.this,
                    R.string.length_feedback_error, Toast.LENGTH_SHORT).show();
        } else {
            sendFeedback();
        }
    }

    private void sendFeedback() {
        progressBar.setVisibility(View.VISIBLE);
        FeedBackAppManager feedBackAppManager = new FeedBackAppManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    setResult(RESULT_OK);
                    finish();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                progressBar.setVisibility(View.GONE);
            }
        });
        feedBackAppManager.feedBackApp(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), edFeedback.getText().toString(), Config.API_FEED_BACK);
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        edFeedback = findViewById(R.id.edFeedback);
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
                validateForm();
                break;
        }
    }
}

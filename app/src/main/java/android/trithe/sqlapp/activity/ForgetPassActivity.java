package android.trithe.sqlapp.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.PushForgetPassManager;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.utils.Utils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ForgetPassActivity extends AppCompatActivity {
    private ImageView btnBack;
    private EditText edEmail;
    private Button btnSend;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        initView();
        pDialog = new ProgressDialog(this);
        btnBack.setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v -> {
            if (Utils.isValidEmail(edEmail.getText().toString())) {
                pushRequest();
            } else {
                Toast.makeText(getApplicationContext(), "Invalid  email address", Toast.LENGTH_SHORT).show();
            }
        });
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
    }
}

package android.trithe.sqlapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LockScreenActivity extends AppCompatActivity {
    private EditText edLockOld;
    private ImageView btnClearOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        initView();
        checkClearSearch(edLockOld, btnClearOld);
        checkActionDone();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void checkActionDone() {
        edLockOld.setOnEditorActionListener((v, actionId, event) ->
        {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (edLockOld.getText().toString().equals(SharedPrefUtils.getString(Constant.KEY_LOCK, ""))) {
                    finish();
                } else {
                    Toast.makeText(LockScreenActivity.this, R.string.curent_pass_error, Toast.LENGTH_SHORT).show();
                    edLockOld.setText("");
                }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        return false;
    }

    private void initView() {
        edLockOld = findViewById(R.id.edLockOld);
        btnClearOld = findViewById(R.id.btnClearOld);

        btnClearOld.setOnClickListener(v -> edLockOld.setText(""));
    }

}

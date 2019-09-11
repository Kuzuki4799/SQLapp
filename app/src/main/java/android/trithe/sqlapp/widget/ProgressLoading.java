package android.trithe.sqlapp.widget;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.databinding.LayoutProgressBinding;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import java.util.Objects;

public class ProgressLoading {
    private Dialog dialog;
    private LayoutProgressBinding viewBinding;

    public ProgressLoading(Context context) {
        viewBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_progress, null, false);
        dialog = new ProgressDialog(context, R.style.ProgressDialogTheme) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(viewBinding.root);
                Objects.requireNonNull(getWindow()).setLayout(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
            }
        };
        dialog.setCancelable(false);
    }

    public void dismiss() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        try {
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showOnTime(int milliseconds) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
        new Handler().postDelayed(this::dismiss, milliseconds);
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

}

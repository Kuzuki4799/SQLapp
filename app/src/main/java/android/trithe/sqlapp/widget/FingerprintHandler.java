package android.trithe.sqlapp.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.content.ContextCompat;
import android.trithe.sqlapp.R;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    private TextView paraLabel;

    public FingerprintHandler(Context context, TextView textView) {
        this.context = context;
        this.paraLabel = textView;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("An authentication error has occurred." + errString, false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Authentication failed.", false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error: " + helpString, false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Access Successful", true);
    }

    private void update(String s, boolean b) {
        paraLabel.setText(s);
        if (b) {
            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            ((Activity) context).finish();
        } else {
            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
    }
}

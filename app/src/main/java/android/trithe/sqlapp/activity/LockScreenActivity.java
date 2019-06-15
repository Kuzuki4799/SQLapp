package android.trithe.sqlapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.finger.FingerprintHandler;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class LockScreenActivity extends AppCompatActivity {
    private EditText edLockOld;
    private ImageView btnClearOld;
    private RelativeLayout rlFingerprint;
    private TextView mParaLabel;
    private String strLockFingerPrint;
    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        initView();
        strLockFingerPrint = SharedPrefUtils.getString(Constant.KEY_CHECK_FINGER_PRINT, "");
        checkClearSearch(edLockOld, btnClearOld);
        checkActionDone();
        checkFingerprint();
        if (strLockFingerPrint.isEmpty()) {
            rlFingerprint.setVisibility(View.GONE);
        }
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
        rlFingerprint = findViewById(R.id.rlFingerprint);
        mParaLabel = findViewById(R.id.paraLabel);

        btnClearOld.setOnClickListener(v -> edLockOld.setText(""));
    }

    private void checkFingerprint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            @SuppressLint("WrongConstant") FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if (!Objects.requireNonNull(fingerprintManager).isHardwareDetected()) {

                mParaLabel.setText(R.string.notdelected);
                rlFingerprint.setVisibility(View.GONE);

            } else if (ContextCompat.checkSelfPermission(LockScreenActivity.this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

                mParaLabel.setText(R.string.permision);
                rlFingerprint.setVisibility(View.VISIBLE);
            } else if (!Objects.requireNonNull(keyguardManager).isKeyguardSecure()) {

                mParaLabel.setText(R.string.nothaveone);
                rlFingerprint.setVisibility(View.VISIBLE);
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {

                mParaLabel.setText(R.string.shouldalllastest);
                rlFingerprint.setVisibility(View.VISIBLE);
            } else {

                mParaLabel.setText(R.string.placeYourScan);
                rlFingerprint.setVisibility(View.VISIBLE);
                generateKey();

                if (cipherInit()) {
                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    FingerprintHandler fingerprintHandler = new FingerprintHandler(this, mParaLabel);
                    fingerprintHandler.startAuth(fingerprintManager, cryptoObject);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {
        try {
            keyStore = KeyStore.getInstance(Constant.KEY_KEY_STORE);
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, Constant.KEY_KEY_STORE);

            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();

        } catch (KeyStoreException | IOException | CertificateException
                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e) {

            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Encryption failed", e);
        }
        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }


}

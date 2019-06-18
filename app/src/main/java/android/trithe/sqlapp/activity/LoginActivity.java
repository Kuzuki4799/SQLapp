package android.trithe.sqlapp.activity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataUserManager;
import android.trithe.sqlapp.rest.request.DataUserInfoRequest;
import android.trithe.sqlapp.rest.response.GetDataUserResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.utils.Utils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private EditText edUsername, edPassword;
    ProgressDialog pDialog;
    private GoogleSignInClient mGoogleSignInClient;
    private Button loginFacebook, loginGoogle;
    private CallbackManager callbackManager;
    int RC_SIGN_IN = 1;
    private ImageView imgBg;
    private TextView txtForget;
    private ImageView imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//              .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        pDialog = new ProgressDialog(this);
        loginFacebook.setOnClickListener(v -> loginFacebook());
        loginGoogle.setOnClickListener(v -> loginGoogle());
        txtForget.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgetPassActivity.class));
        });
        imgBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        loginFacebook = findViewById(R.id.login_facebook);
        loginGoogle = findViewById(R.id.login_google);
        imgBg = findViewById(R.id.imgBg);
        txtForget = findViewById(R.id.txtForget);
        imgBack = findViewById(R.id.imgBack);
    }

    private void loginGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void loginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(
                callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                            try {
                                SharedPrefUtils.putString(Constant.KEY_CHECK_LOGIN, Constant.FACEBOOK);
                                callApiCheckUser(object.getString("email"), object.getString("name"), object.getJSONObject("picture").getJSONObject("data").getString("url"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "name,email,picture.width(500)");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                }
        );
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, imgBg, getResources().getString(R.string.shareName));
        startActivity(intent, options.toBundle());
    }

    public void onLogin(View view) {
        String username = edUsername.getText().toString();
        String password = edPassword.getText().toString();
        DataUserInfoRequest dataUserInfoRequest = new DataUserInfoRequest("", username, password, "");
        if (!Utils.isValidEmail(username)) {
            Toast.makeText(getApplicationContext(), "Invalid  email address", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(LoginActivity.this, "Password null", Toast.LENGTH_SHORT).show();
        } else {
            showProcessDialog();
            GetDataUserManager getDataUserManager = new GetDataUserManager(new ResponseCallbackListener<GetDataUserResponse>() {
                @Override
                public void onObjectComplete(String TAG, GetDataUserResponse data) {
                    if (data.status.equals("200")) {
                        setResult(RESULT_OK);
                        finish();
                        SharedPrefUtils.putString(Constant.KEY_USER_ID, data.result.id);
                        SharedPrefUtils.putString(Constant.KEY_USER_NAME, data.result.username);
                        SharedPrefUtils.putString(Constant.KEY_USER_PASSWORD, data.result.password);
                        SharedPrefUtils.putString(Constant.KEY_NAME_USER, data.result.name);
                        SharedPrefUtils.putString(Constant.KEY_USER_IMAGE, data.result.image);
                    }
                    disProcessDialog();
                }

                @Override
                public void onResponseFailed(String TAG, String message) {
                    disProcessDialog();
                }
            });
            getDataUserManager.startGetDataInfo(dataUserInfoRequest, Config.API_LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                loginWithGoogle(account);
            } catch (ApiException ignored) {
            }
        }
    }

    private void callApiRegister(String name, String username, String image) {
        showProcessDialog();
        DataUserInfoRequest dataUserInfoRequest = new DataUserInfoRequest(name, username, "null", image);
        GetDataUserManager getDataUserManager = new GetDataUserManager(new ResponseCallbackListener<GetDataUserResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataUserResponse data) {
                if (data.status.equals("200")) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    SharedPrefUtils.putBoolean(Constant.REGISTER, true);
                    SharedPrefUtils.putString(Constant.KEY_USER_ID, data.result.id);
                    SharedPrefUtils.putString(Constant.KEY_USER_NAME, data.result.username);
                    SharedPrefUtils.putString(Constant.KEY_USER_PASSWORD, data.result.password);
                    SharedPrefUtils.putString(Constant.KEY_NAME_USER, data.result.name);
                    SharedPrefUtils.putString(Constant.KEY_USER_IMAGE, data.result.image);
                    setResult(RESULT_OK);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getDataUserManager.startGetDataInfo(dataUserInfoRequest, Config.API_REGISTER);
    }

    private void loginWithGoogle(GoogleSignInAccount account) {
        callApiCheckUser(account.getEmail(), account.getDisplayName(), String.valueOf(account.getPhotoUrl()));
        SharedPrefUtils.putString(Constant.KEY_CHECK_LOGIN, Constant.GOOGLE);
    }

    private void callApiCheckUser(String email, String name, String image) {
        showProcessDialog();
        DataUserInfoRequest dataUserInfoRequest = new DataUserInfoRequest(email, null, null, null);
        GetDataUserManager getDataUserManager = new GetDataUserManager(new ResponseCallbackListener<GetDataUserResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataUserResponse data) {
                if (data.status.equals("200")) {
                    setResult(RESULT_OK);
                    finish();
                    SharedPrefUtils.putString(Constant.KEY_USER_ID, data.result.id);
                    SharedPrefUtils.putString(Constant.KEY_USER_NAME, data.result.username);
                    SharedPrefUtils.putString(Constant.KEY_USER_PASSWORD, data.result.password);
                    SharedPrefUtils.putString(Constant.KEY_NAME_USER, data.result.name);
                    SharedPrefUtils.putString(Constant.KEY_USER_IMAGE, data.result.image);
                } else {
                    callApiRegister(name, email, image);
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getDataUserManager.startGetDataInfo(dataUserInfoRequest, Config.API_CHECK_USER);
    }
}

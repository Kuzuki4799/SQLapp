package android.trithe.sqlapp.activity;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.callback.OnHeaderItemClickListener;
import android.trithe.sqlapp.callback.OnKindItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.fragment.AboutFragment;
import android.trithe.sqlapp.fragment.AccountFragment;
import android.trithe.sqlapp.fragment.CinemaFragment;
import android.trithe.sqlapp.fragment.FavoriteFragment;
import android.trithe.sqlapp.fragment.HomeFragment;
import android.trithe.sqlapp.fragment.KindFragment;
import android.trithe.sqlapp.fragment.SavedFragment;
import android.trithe.sqlapp.fragment.SettingsFragment;
import android.trithe.sqlapp.model.Header;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataKindManager;
import android.trithe.sqlapp.rest.manager.GetDataNotificationManager;
import android.trithe.sqlapp.rest.model.KindModel;
import android.trithe.sqlapp.rest.response.GetDataKindResponse;
import android.trithe.sqlapp.rest.response.GetNotificationResponse;
import android.trithe.sqlapp.utils.NotificationUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements OnHeaderItemClickListener,
        OnKindItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    private View viewNavi;
    private Toolbar toolbar;
    private Button btnLogin;
    private boolean isLogin;
    private TextView txtName;
    private CircleImageView imgAvatar;
    private TextView textNotificationItemCount;
    public static final int REQUEST_LOGIN = 999;
    public static final int REQUEST_NOTIFICATION = 99;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private HomeFragment homeFragment = new HomeFragment();
    private CinemaFragment cinemaFragment = new CinemaFragment();
    private KindFragment kindFragment = new KindFragment();
    private AccountFragment accountFragment = new AccountFragment();
    private SavedFragment savedFragment = new SavedFragment();
    private FavoriteFragment favoriteFragment = new FavoriteFragment();
    private AboutFragment aboutFragment = new AboutFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.kuzuki);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        setUpDraw();
        loadFragment(homeFragment);
        checkRealTimeNotification();
    }

    private void checkRealTimeNotification() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    setSupportActionBar(toolbar);
                }
            }
        };
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setUpDraw() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                checkUserIsLogin();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        viewNavi = navigationView.getHeaderView(0);
        imgAvatar = viewNavi.findViewById(R.id.imgAvatar);
        txtName = viewNavi.findViewById(R.id.txtName);
        btnLogin = viewNavi.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            Intent intents = new Intent(this, LoginActivity.class);
            startActivityForResult(intents, REQUEST_LOGIN);
        });
        checkUserIsLogin();
    }

    private void checkUserIsLogin() {
        isLogin = !SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty();
        if (isLogin) {
            btnLogin.setVisibility(View.GONE);
            imgAvatar.setVisibility(View.VISIBLE);
            txtName.setVisibility(View.VISIBLE);
            Glide.with(MainActivity.this).load(Config.LINK_LOAD_IMAGE +
                    SharedPrefUtils.getString(Constant.KEY_USER_IMAGE, "")).into(imgAvatar);
            txtName.setText(SharedPrefUtils.getString(Constant.KEY_NAME_USER, ""));
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            imgAvatar.setVisibility(View.GONE);
            txtName.setVisibility(View.GONE);
        }
    }

    private void registerReceiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_home, menu);
        MenuItem menuItem = menu.findItem(R.id.action_notifi);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textNotificationItemCount = actionView.findViewById(R.id.notification_badge);
        setTextCountNotification();
        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
        return true;
    }

    private void setTextCountNotification() {
        GetDataNotificationManager getDataNotificationManager = new GetDataNotificationManager(
                new ResponseCallbackListener<GetNotificationResponse>() {
                    @Override
                    public void onObjectComplete(String TAG, GetNotificationResponse data) {
                        if (data.status.equals("200")) {
                            if (data.result.size() != 0) {
                                textNotificationItemCount.setVisibility(View.VISIBLE);
                                textNotificationItemCount.setText(String.valueOf(data.result.size()));
                            }
                        }
                    }

                    @Override
                    public void onResponseFailed(String TAG, String message) {

                    }
                });
        getDataNotificationManager.getDataNotification(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""),
                Config.API_GET_COUNT_NOTIFICATION, 0, 100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notifi:
                if (isLogin) {
                    startActivityForResult(new Intent(MainActivity.this, NotificationActivity.class), REQUEST_NOTIFICATION);
                } else {
                    Intent intents = new Intent(this, LoginActivity.class);
                    startActivityForResult(intents, REQUEST_LOGIN);
                }
                break;
            case R.id.action_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                loadFragment(homeFragment);
                break;
//            case R.id.nav_cinama:
//                loadFragment(cinemaFragment);
//                break;
            case R.id.nav_kind:
                loadFragment(kindFragment);
                break;
            case R.id.nav_saved:
                if (isLogin) {
                    loadFragment(savedFragment);
                } else {
                    Intent intents = new Intent(this, LoginActivity.class);
                    startActivityForResult(intents, REQUEST_LOGIN);
                }
                break;
            case R.id.nav_favorite:
                if (isLogin) {
                    loadFragment(favoriteFragment);
                } else {
                    Intent intents = new Intent(this, LoginActivity.class);
                    startActivityForResult(intents, REQUEST_LOGIN);
                }
                break;
            case R.id.nav_settings:
                if (isLogin) {
                    loadFragment(settingsFragment);
                } else {
                    Intent intents = new Intent(this, LoginActivity.class);
                    startActivityForResult(intents, REQUEST_LOGIN);
                }
                break;
            case R.id.nav_account:
                if (isLogin) {
                    loadFragment(accountFragment);
                } else {
                    Intent intents = new Intent(this, LoginActivity.class);
                    startActivityForResult(intents, REQUEST_LOGIN);
                }
                break;
            case R.id.nav_info:
                loadFragment(aboutFragment);
                break;
            case R.id.nav_log_out:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(setColorTextDialog(getResources().getString(R.string.sign_out)));
                builder.setMessage(setColorTextDialog(getResources().getString(R.string.ms_sign_out)));
                builder.setPositiveButton(R.string.strOk, (dialog, which) -> {
                            if (SharedPrefUtils.getString(Constant.KEY_CHECK_LOGIN, "").equals(Constant.FACEBOOK)) {
                                LoginManager.getInstance().logOut();
                            } else if (SharedPrefUtils.getString(Constant.KEY_CHECK_LOGIN, "").equals(Constant.GOOGLE)) {
                                GoogleSignInClient mGoogleSignInClient;
                                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestEmail()
                                        .build();
                                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                                mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
                                });
                            }
                            SharedPrefUtils.putString(Constant.KEY_USER_ID, null);
                            SharedPrefUtils.putString(Constant.KEY_USER_NAME, null);
                            SharedPrefUtils.putString(Constant.KEY_USER_PASSWORD, null);
                            SharedPrefUtils.putString(Constant.KEY_NAME_USER, null);
                            SharedPrefUtils.putString(Constant.KEY_USER_IMAGE, null);
                            checkUserIsLogin();
                            SharedPrefUtils.putString(Constant.KEY_CHECK_LOGIN, null);
                            setSupportActionBar(toolbar);
                            loadFragment(homeFragment);
                        }
                );
                builder.setNegativeButton(R.string.str_cancel, (dialog, which) -> dialog.cancel());
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                isLogin = true;
                checkUserIsLogin();
                setSupportActionBar(toolbar);
            }

            if (requestCode == REQUEST_NOTIFICATION) {
                checkUserIsLogin();
                setSupportActionBar(toolbar);
            }
        }
    }

    @Override
    public void onFilm(Header header) {
        GetDataKindManager getDataKindManager = new GetDataKindManager(new ResponseCallbackListener<GetDataKindResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataKindResponse data) {
                if (data.status.equals("200")) {
                    Intent intent = new Intent(MainActivity.this, DetailKindActivity.class);
                    for (int i = 0; i < data.result.size(); i++) {
                        if (header.getSectionLabel().equals(data.result.get(i).name)) {
                            intent.putExtra(Constant.ID, data.result.get(i).id);
                        }
                    }
                    startActivity(intent);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataKindManager.startGetDataKind(null, Config.API_KIND);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onKind(KindModel kindModel, CardView cardView) {
        Intent intent = new Intent(this, DetailKindActivity.class);
        intent.putExtra(Constant.ID, kindModel.id);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, cardView, getResources().getString(R.string.shareName));
        startActivity(intent, options.toBundle());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(setColorTextDialog(getResources().getString(R.string.notification)));
            builder.setMessage(setColorTextDialog(getResources().getString(R.string.ms_exit_app)));
            builder.setPositiveButton(R.string.strOk, (dialog, which) -> finish()
            );
            builder.setNegativeButton(R.string.str_cancel, (dialog, which) -> dialog.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return false;
    }

    private SpannableStringBuilder setColorTextDialog(String text) {
        SpannableStringBuilder ssBuilderTitle =
                new SpannableStringBuilder(text);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);
        ssBuilderTitle.setSpan(
                foregroundColorSpan,
                0,
                text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return ssBuilderTitle;
    }
}

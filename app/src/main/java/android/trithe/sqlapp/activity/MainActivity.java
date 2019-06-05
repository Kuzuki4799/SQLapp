package android.trithe.sqlapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.HeaderAdapter;
import android.trithe.sqlapp.adapter.SlidePaperAdapter;
import android.trithe.sqlapp.callback.OnHeaderItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.model.Header;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.manager.GetDataKindDetailManager;
import android.trithe.sqlapp.rest.manager.GetDataKindManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.model.PosterModel;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataKindResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements OnHeaderItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    private ViewPager backdrop;
    private TabLayout indicator;
    private List<PosterModel> slideList = new ArrayList<>();
    private List<Header> headerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HeaderAdapter adapter;
    private ProgressDialog pDialog;
    private CircleImageView imgAvatar;
    private TextView txtName;
    private Button btnLogin;
    private View viewNavi;
    private boolean isLogin;
    public static final int REQUEST_LOGIN = 999;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setUpDraw();
        pDialog = new ProgressDialog(this);
        slide();
        adapter = new HeaderAdapter(headerList);
        adapter.setOnClickItemPopularFilm(this);
        getFilm();
        setUpRecyclerView();
    }

    private void setUpDraw() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.kuzuki);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        viewNavi = navigationView.getHeaderView(0);
        imgAvatar = viewNavi.findViewById(R.id.imgAvatar);
        txtName = viewNavi.findViewById(R.id.txtName);
        btnLogin = viewNavi.findViewById(R.id.btnLogin);
        checkUserIsLogin();
    }

    private void checkUserIsLogin() {
        isLogin = !SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty();
        if (isLogin) {
            btnLogin.setVisibility(View.GONE);
            imgAvatar.setVisibility(View.VISIBLE);
            txtName.setVisibility(View.VISIBLE);
            Glide.with(MainActivity.this).load(Config.LINK_LOAD_IMAGE + SharedPrefUtils.getString(Constant.KEY_USER_IMAGE, "")).into(imgAvatar);
            txtName.setText(SharedPrefUtils.getString(Constant.KEY_NAME_USER, ""));
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            imgAvatar.setVisibility(View.GONE);
            txtName.setVisibility(View.GONE);
            btnLogin.setOnClickListener(v -> {
                Intent intents = new Intent(this, LoginActivity.class);
                startActivityForResult(intents, REQUEST_LOGIN);
            });
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notifi:
                break;
            case R.id.action_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_kind:
                startActivity(new Intent(MainActivity.this, KindActivity.class));
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_account:
                break;
            case R.id.nav_saved:
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        backdrop = findViewById(R.id.backdrop);
        indicator = findViewById(R.id.indicator);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void slide() {
        slideList.clear();
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    for (int i = 0; i < data.result.size(); i++) {
                        slideList.add(new PosterModel(data.result.get(i).id, data.result.get(i).name, data.result.get(i).imageCover));
                    }
                    getTimerSlide();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataFilmManager.startGetDataFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_FILM);
    }

    private void getFilm() {
        headerList.clear();
        showProcessDialog();
        GetDataKindManager getDataKindManager = new GetDataKindManager(new ResponseCallbackListener<GetDataKindResponse>() {
            @Override
            public void onObjectComplete(String TAG, final GetDataKindResponse data) {
                if (data.status.equals("200")) {
                    for (int i = 0; i < 5; i++) {
                        final int finalI = i;
                        GetDataKindDetailManager getDataKindDetailManager = new GetDataKindDetailManager(new ResponseCallbackListener<GetDataFilmResponse>() {
                            @Override
                            public void onObjectComplete(String TAG, GetDataFilmResponse data1) {
                                List<FilmModel> filmModelList = new ArrayList<>(data1.result);
                                headerList.add(new Header(data.result.get(finalI).name, filmModelList));
                                adapter.notifyDataSetChanged();
                                disProcessDialog();
                            }

                            @Override
                            public void onResponseFailed(String TAG, String message) {
                                disProcessDialog();
                            }
                        });
                        getDataKindDetailManager.startGetDataKindDetail(SharedPrefUtils.getString(Constant.KEY_USER_ID,""), data.result.get(i).id);
                    }
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataKindManager.startGetDataKind(null, Config.API_KIND);
    }

    private void getTimerSlide() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        indicator.setupWithViewPager(backdrop, true);
        SlidePaperAdapter paperAdapter = new SlidePaperAdapter(this, slideList);
        backdrop.setAdapter(paperAdapter);
    }

    @Override
    public void onFilm(final Header header) {
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

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(() -> {
                if (backdrop.getCurrentItem() < slideList.size() - 1) {
                    backdrop.setCurrentItem(backdrop.getCurrentItem() + 1);
                } else
                    backdrop.setCurrentItem(0);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserIsLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                isLogin = true;
                checkUserIsLogin();
            }
        }
    }
}

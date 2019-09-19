package android.trithe.sqlapp.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.FilmAdapter;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataCastCountryManager;
import android.trithe.sqlapp.rest.manager.GetDataCastDetailManager;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.manager.GetDataJobManager;
import android.trithe.sqlapp.rest.manager.GetDataLoveCountManager;
import android.trithe.sqlapp.rest.manager.LovedCastManager;
import android.trithe.sqlapp.rest.manager.UpdateViewCastManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.model.JobandCountryModel;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataCastDetailResponse;
import android.trithe.sqlapp.rest.response.GetDataCountryResponse;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataJobResponse;
import android.trithe.sqlapp.rest.response.GetDataLoveCountResponse;
import android.trithe.sqlapp.utils.DateUtils;
import android.trithe.sqlapp.utils.EndlessRecyclerOnScrollListener;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;
import java.util.List;

public class CastActivity extends AppCompatActivity implements View.OnClickListener {
    private String id;
    private ImageView btnBack;
    private TextView txtLikeCount;
    private ImageView imgAvatar;
    private TextView txtName;
    private ImageView imgLoved;
    private ImageView imgSearch;
    private ImageView imgCover;
    private TextView txtDate;
    private TextView txtViews;
    private TextView txtCountry;
    private TextView txtJob;
    private TextView txtInfo;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private TextView txtTitle;
    private RecyclerView recyclerViewByCast;
    private FilmAdapter adapter;
    private List<FilmModel> list = new ArrayList<>();
    private ProgressDialog pDialog;
    private Boolean checkViews = false;
    public static final int REQUEST_LOGIN = 999;
    private NativeExpressAdView nativeExpress;

    private int page = 0;
    private int per_page = 5;
    private LinearLayoutManager linearLayoutManager;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);
        pDialog = new ProgressDialog(this);
        initView();
        setUpAppBar();
        id = getIntent().getStringExtra(Constant.ID);
        adapter = new FilmAdapter(list, 0);
        setUpRecyclerView();
        getDataCast();
        getJobCast();
        getDataCountry();
        getLikeCount();
        listener();
        AdRequest adRequest = new AdRequest.Builder().build();
        nativeExpress.loadAd(adRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        adapter.setOnLoadMore(true);
        setUpRecyclerView();
        getFilm(page, per_page);
    }

    private void listener() {
        btnBack.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
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

    private void setUpRecyclerView() {
        recyclerViewByCast.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(CastActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewByCast.setLayoutManager(linearLayoutManager);
        recyclerViewByCast.setAdapter(adapter);
        recyclerViewByCast.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                new Handler().postDelayed(() -> {
                    getFilm(page, per_page);
                    Log.d("abc", page + "");
                }, 500);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setUpAppBar() {
        appbar.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener) (appBarLayout, i) -> {
            if (i == 0) {
                toolbar.setBackgroundResource(R.drawable.black_gradian_reverse);
                txtTitle.setVisibility(View.GONE);
            } else {
                toolbar.setBackgroundColor(getColor(R.color.colorPrimary));
                txtTitle.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initView() {
        nativeExpress = findViewById(R.id.nativeExpress);
        txtLikeCount = findViewById(R.id.txtLikeCount);
        imgCover = findViewById(R.id.imgCover);
        btnBack = findViewById(R.id.btnBack);
        imgAvatar = findViewById(R.id.imgAvatar);
        txtName = findViewById(R.id.txtName);
        txtDate = findViewById(R.id.txtDate);
        txtCountry = findViewById(R.id.txtCountry);
        txtJob = findViewById(R.id.txtJob);
        txtInfo = findViewById(R.id.txtInfo);
        recyclerViewByCast = findViewById(R.id.recycler_view_by_cast);
        imgLoved = findViewById(R.id.imgLoved);
        imgSearch = findViewById(R.id.imgSearch);
        txtViews = findViewById(R.id.txtViews);
        appbar = findViewById(R.id.appbar);
        txtTitle = findViewById(R.id.txtTitle);
        toolbar = findViewById(R.id.toolbar);

        imgCover.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_anim));
    }

    private void checkPushWithCheckUser(String id, String key) {
        if (!SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty()) {
            onPushLoveCast(id, key);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }
    }

    private void onPushLoveCast(final String id, String key) {
        showProcessDialog();
        LovedCastManager lovedCastManager = new LovedCastManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    getDataCast();
                    getLikeCount();
                    disProcessDialog();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        lovedCastManager.pushLovedCast(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, key);
    }

    private void getDataCast() {
        showProcessDialog();
        GetDataCastDetailManager getDataCastDetailManager = new GetDataCastDetailManager(new ResponseCallbackListener<GetDataCastDetailResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataCastDetailResponse data) {
                if (data.status.equals("200")) {
                    txtName.setText(data.result.name);
                    txtTitle.setText(data.result.name);
                    Glide.with(CastActivity.this).load(Config.LINK_LOAD_IMAGE + data.result.image).into(imgAvatar);
                    Glide.with(CastActivity.this).load(Config.LINK_LOAD_IMAGE + data.result.imageCover).into(imgCover);
                    DateUtils.parseDateFormat(txtDate, data.result.dateOfBirth);
                    txtInfo.setText(data.result.infomation);
                    checkView(data.result.views);
                    if (!checkViews) {
                        updateViewCast(String.valueOf(data.result.views + 1));
                    }
                    if (data.result.loved == 1) {
                        Glide.with(CastActivity.this).load(R.drawable.love).into(imgLoved);
                        imgLoved.setOnClickListener(v -> checkPushWithCheckUser(id, Config.API_DELETE_LOVE_CAST));
                    } else {
                        Glide.with(CastActivity.this).load(R.drawable.unlove).into(imgLoved);
                        imgLoved.setOnClickListener(v -> checkPushWithCheckUser(id, Config.API_INSERT_LOVE_CAST));
                    }
                    disProcessDialog();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getDataCastDetailManager.startGetDataCast(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id);
    }

    private void checkView(int views) {
        if (views < 2) {
            txtViews.setText(views + " View");
        } else {
            txtViews.setText(views + " Views");
        }
    }

    private void getJobCast() {
        GetDataJobManager getDataJobManager = new GetDataJobManager(new ResponseCallbackListener<GetDataJobResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataJobResponse data) {
                if (data.status.equals("200")) {
                    getJob(data.result);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataJobManager.startGetJobData(id);
    }

    private void getJob(List<JobandCountryModel> jobAndCountryModels) {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < jobAndCountryModels.size(); i++) {
            name.append(jobAndCountryModels.get(i).name).append(", ");
        }
        if (name.length() > 0) name = new StringBuilder(name.substring(0, name.length() - 2));
        txtJob.setText(name);
    }

    private void getLikeCount() {
        GetDataLoveCountManager getDataLoveCountManager = new GetDataLoveCountManager(new ResponseCallbackListener<GetDataLoveCountResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataLoveCountResponse data) {
                if (data.status.equals("200")) {
                    if (data.result.size() == 1) {
                        txtLikeCount.setText(data.result.size() + " Like");
                    } else {
                        txtLikeCount.setText(data.result.size() + " Likes");
                    }

                } else {
                    txtLikeCount.setText("Like");
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataLoveCountManager.startGetDataLoveCount(id);
    }

    private void getDataCountry() {
        GetDataCastCountryManager getDataCastCountryManager = new GetDataCastCountryManager(new ResponseCallbackListener<GetDataCountryResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataCountryResponse data) {
                if (data.status.equals("200")) {
                    txtCountry.setText(data.result.name);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataCastCountryManager.startGetDataCastCountry(id);
    }

    private void updateViewCast(String views) {
        UpdateViewCastManager updateViewCastManager = new UpdateViewCastManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    checkViews = true;
                    getDataCast();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        updateViewCastManager.startGetDataCast(id, views, Config.UPDATE_VIEWS_CAST);
    }

    private void getFilm(int page, int per_page) {
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    if (data.result.size() < 5) {
                        adapter.setOnLoadMore(false);
                    }
                    list.addAll(data.result);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.setOnLoadMore(false);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                Log.d("abc", message);
            }
        });
        getDataFilmManager.startGetDataFilm(0, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id,
                page, per_page, Config.API_GET_FILM_BY_CAST);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                getDataCast();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.imgSearch:
                Intent intent = new Intent(CastActivity.this, SearchActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString(Constant.ID, Constant.NB0);
                intent.putExtras(mBundle);
                startActivity(intent);
                break;
        }
    }
}

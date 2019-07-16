package android.trithe.sqlapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.FilmAdapter;
import android.trithe.sqlapp.adapter.UpComingAdapter;
import android.trithe.sqlapp.callback.OnChangeSetItemClickLovedListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataCinemaManager;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.manager.GetDataLoveCountCinemaManager;
import android.trithe.sqlapp.rest.manager.GetDataRatingCinemaManager;
import android.trithe.sqlapp.rest.manager.LovedCinemaManager;
import android.trithe.sqlapp.rest.manager.PushRatCinemaManager;
import android.trithe.sqlapp.rest.manager.UpdateViewCastManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetAllDataCinemaResponse;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataLoveCountCinemaResponse;
import android.trithe.sqlapp.rest.response.GetDataRatingCinemaResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.utils.Utils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CinemaDetailActivity extends AppCompatActivity implements View.OnClickListener, RatingDialogListener {
    private ImageView imgCover;
    private ImageView imgRating;
    private TextView txtReviews;
    private TextView txtRating;
    private TextView txtTitle;
    private TextView txtTheatres;
    private TextView txtUpcoming;
    private RecyclerView recyclerView;
    private List<FilmModel> listFilm = new ArrayList<>();
    private FilmAdapter adapter;
    private ImageView imgBack;
    private ImageView imgLoved;
    private ImageView imgShare;
    private TextView txtLocation;
    private ImageView imgSearch;
    private Button btnRat;
    private TextView txtDetail;
    private Boolean checkViews = false;
    private boolean isLogin;
    public static final int REQUEST_LOGIN = 999;
    private SupportMapFragment mapFragment;
    private ProgressDialog pDialog;
    private String url;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_detail);
        pDialog = new ProgressDialog(this);
        adapter = new FilmAdapter(listFilm, Integer.parseInt(getIntent().getStringExtra(Constant.ID)));
        isLogin = !SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty();
        initView();
        setUpRecyclerView();
        checkFlag();
        getInfoCinema();
    }

    private void checkFlag() {
        if (flag == 0) {
            getFilm(1);
        } else {
            getFilm(2);
        }
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CinemaDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void getFilm(int status) {
        listFilm.clear();
        showProcessDialog();
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    listFilm.addAll(data.result);
                    adapter.notifyDataSetChanged();
                    disProcessDialog();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getDataFilmManager.startGetDataFilm(status, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_FILM);
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

    private void checkRatCinema() {
        PushRatCinemaManager pushRatCinemaManager = new PushRatCinemaManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    Utils.showAlertDialog1(CinemaDetailActivity.this, R.string.rated_cinema);
                } else {
                    showDialog();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        pushRatCinemaManager.pushRatCinema(null, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), getIntent().getStringExtra(Constant.ID), Config.API_CHECK_RAT_CINEMA);
    }

    private void showDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(3)
                .setTitle("Rate this cinema")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(android.R.color.white)
                .setDescriptionTextColor(android.R.color.darker_gray)
                .setHint("Please write your comment here ...")
                .setHintTextColor(android.R.color.darker_gray)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .create(CinemaDetailActivity.this)
                .show();
    }

    private void getRatCinema() {
        GetDataRatingCinemaManager getDataRatingCinemaManager = new GetDataRatingCinemaManager(new ResponseCallbackListener<GetDataRatingCinemaResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataRatingCinemaResponse data) {
                if (data.status.equals("200")) {
                    double rat = 0.0;
                    for (int i = 0; i < data.result.size(); i++) {
                        rat += data.result.get(i).rat;
                    }
                    txtRating.setText(String.valueOf(rat / data.result.size()));
                    try {
                        checkRating(rat / data.result.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataRatingCinemaManager.startGetDataRating(getIntent().getStringExtra(Constant.ID));
    }

    private void checkRating(double rat) {
        if (rat == 5.0) {
            Glide.with(CinemaDetailActivity.this).load(R.drawable.fivestar).into(imgRating);
        } else if (rat >= 4.0) {
            Glide.with(CinemaDetailActivity.this).load(R.drawable.fourstar).into(imgRating);
        } else if (rat >= 3.0) {
            Glide.with(CinemaDetailActivity.this).load(R.drawable.threestar).into(imgRating);
        } else if (rat >= 2.0) {
            Glide.with(CinemaDetailActivity.this).load(R.drawable.twostar).into(imgRating);
        } else if (rat >= 1.0) {
            Glide.with(CinemaDetailActivity.this).load(R.drawable.onestar).into(imgRating);
        }
    }

    private void getInfoCinema() {
        GetDataCinemaManager getDataCinemaManager = new GetDataCinemaManager(new ResponseCallbackListener<GetAllDataCinemaResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataCinemaResponse data) {
                if (data.status.equals("200")) {
                    Glide.with(CinemaDetailActivity.this).load(Config.LINK_LOAD_IMAGE + data.result.get(0).image).into(imgCover);
                    txtDetail.setText(data.result.get(0).detail);
                    txtTitle.setText(data.result.get(0).name);
                    txtLocation.setText(data.result.get(0).address);
                    if (data.result.get(0).loved == 1) {
                        Glide.with(CinemaDetailActivity.this).load(R.drawable.love).into(imgLoved);
                        imgLoved.setOnClickListener(v -> onPushLoveCast(data.result.get(0).id, Config.API_DELETE_LOVE_CINEMA));
                    } else {
                        Glide.with(CinemaDetailActivity.this).load(R.drawable.unlove).into(imgLoved);
                        imgLoved.setOnClickListener(v -> onPushLoveCast(data.result.get(0).id, Config.API_INSERT_LOVE_CINEMA));
                    }
                    if (!checkViews) {
                        updateViewCinema(String.valueOf(data.result.get(0).views + 1));
                    }
                    url = data.result.get(0).website;
                    Objects.requireNonNull(mapFragment).getMapAsync(googleMap -> {
                        LatLng latLng = new LatLng(Double.valueOf(data.result.get(0).latLocation),
                                Double.valueOf(data.result.get(0).longLocation));
                        googleMap.addMarker(new MarkerOptions()
                                .position(latLng))
                                .showInfoWindow();
                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 16f, 0f, 0f)));
                    });
                    getRatCinema();
                    getCountLovedCinema();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataCinemaManager.startGetDataCinema(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), getIntent().getStringExtra(Constant.ID), Config.API_DETAIL_CINEMA);
    }

    private void onPushLoveCast(final String id, String key) {
        showProcessDialog();
        LovedCinemaManager lovedCinemaManager = new LovedCinemaManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    getInfoCinema();
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        lovedCinemaManager.pushLovedCinema(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, key);
    }

    private void getCountLovedCinema() {
        GetDataLoveCountCinemaManager getDataLoveCountCinemaManager = new GetDataLoveCountCinemaManager(new ResponseCallbackListener<GetDataLoveCountCinemaResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataLoveCountCinemaResponse data) {
                if (data.status.equals("200")) {
                    txtReviews.setText(data.result.size() + " Likes");
                } else {
                    txtReviews.setText("Like");
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataLoveCountCinemaManager.startGetDataLoveCount(getIntent().getStringExtra(Constant.ID));
    }

    private void updateViewCinema(String views) {
        UpdateViewCastManager updateViewCastManager = new UpdateViewCastManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    checkViews = true;
                    getInfoCinema();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        updateViewCastManager.startGetDataCast(getIntent().getStringExtra(Constant.ID), views, Config.API_UPDATE_VIEWS_CINEMA);
    }

    private void initView() {
        imgCover = findViewById(R.id.imgCover);
        imgRating = findViewById(R.id.imgRating);
        txtReviews = findViewById(R.id.txtReviews);
        txtRating = findViewById(R.id.txtRating);
        txtTitle = findViewById(R.id.txtTitle);
        imgBack = findViewById(R.id.imgBack);
        imgShare = findViewById(R.id.imgShare);
        imgSearch = findViewById(R.id.imgSearch);
        btnRat = findViewById(R.id.btnRat);
        txtDetail = findViewById(R.id.txtDetail);
        txtLocation = findViewById(R.id.txtLocation);
        imgLoved = findViewById(R.id.imgLoved);
        txtTheatres = findViewById(R.id.txtTheatres);
        txtUpcoming = findViewById(R.id.txtUpcoming);
        recyclerView = findViewById(R.id.recycler_view);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_detail);

        btnRat.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txtTheatres.setOnClickListener(this);
        txtUpcoming.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgShare:
                Utils.shareUrl(this, url);
                break;
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnRat:
                if (isLogin) {
                    checkRatCinema();
                } else {
                    Intent intents = new Intent(this, LoginActivity.class);
                    startActivityForResult(intents, REQUEST_LOGIN);
                }
                break;
            case R.id.txtTheatres:
                txtTheatres.setTextColor(getResources().getColor(android.R.color.white));
                txtTheatres.setTypeface(null, Typeface.BOLD);
                txtUpcoming.setTextColor(getResources().getColor(android.R.color.darker_gray));
                txtUpcoming.setTypeface(null, Typeface.NORMAL);
                flag = 0;
                checkFlag();
                break;
            case R.id.txtUpcoming:
                txtUpcoming.setTextColor(getResources().getColor(android.R.color.white));
                txtUpcoming.setTypeface(null, Typeface.BOLD);
                txtTheatres.setTextColor(getResources().getColor(android.R.color.darker_gray));
                txtTheatres.setTypeface(null, Typeface.NORMAL);
                flag = 1;
                checkFlag();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                isLogin = true;
                getInfoCinema();
                getRatCinema();
            }
        }
    }

    @Override
    public void onNegativeButtonClicked() {
    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {
        showProcessDialog();
        PushRatCinemaManager pushRatCinemaManager = new PushRatCinemaManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    getRatCinema();
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        pushRatCinemaManager.pushRatCinema(String.valueOf(i), SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), getIntent().getStringExtra(Constant.ID), Config.API_PUSH_INSERT_RAT_CINEMA);
    }
}

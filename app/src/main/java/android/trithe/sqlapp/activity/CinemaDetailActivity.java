package android.trithe.sqlapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataCinemaManager;
import android.trithe.sqlapp.rest.manager.GetDataRatingCinemaManager;
import android.trithe.sqlapp.rest.manager.PushRatCinemaManager;
import android.trithe.sqlapp.rest.manager.UpdateViewCastManager;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetAllDataCinemaResponse;
import android.trithe.sqlapp.rest.response.GetDataRatingCinemaResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.utils.Utils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.Arrays;
import java.util.Objects;


public class CinemaDetailActivity extends AppCompatActivity implements View.OnClickListener, RatingDialogListener {
    private ImageView imgCover;
    private ImageView imgRating;
    private TextView txtReviews;
    private TextView txtRating;
    private TextView txtTitle;
    private ImageView imgBack;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_detail);
        pDialog = new ProgressDialog(this);
        isLogin = !SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty();
        initView();
        getInfoCinema();
        getRatCinema();
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
                    Utils.showAlertDialog1(CinemaDetailActivity.this, R.string.rated);
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
                    checkView(data.result.get(0).views);
                    if (!checkViews) {
                        updateViewCinema(String.valueOf(data.result.get(0).views + 1));
                    }
                    Objects.requireNonNull(mapFragment).getMapAsync(googleMap -> {
                        LatLng latLng = new LatLng(Double.valueOf(data.result.get(0).latLocation),
                                Double.valueOf(data.result.get(0).longLocation));
                        googleMap.addMarker(new MarkerOptions()
                                .position(latLng))
                                .showInfoWindow();
                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 16f, 0f, 0f)));
                    });
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataCinemaManager.startGetDataCinema(getIntent().getStringExtra(Constant.ID), Config.API_DETAIL_CINEMA);
    }

    private void checkView(int views) {
        if (views < 2) {
            txtReviews.setText(views + " View");
        } else {
            txtReviews.setText(views + " Views");
        }
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
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_detail);

        btnRat.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

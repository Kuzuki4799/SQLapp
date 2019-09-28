package android.trithe.sqlapp.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.CastDetailAdapter;
import android.trithe.sqlapp.adapter.CommentFilmAdapter;
import android.trithe.sqlapp.adapter.SeriesAdapter;
import android.trithe.sqlapp.adapter.ShowTimeAdapter;
import android.trithe.sqlapp.callback.OnChangeSetItemClickLovedListener;
import android.trithe.sqlapp.callback.OnSeriesItemClickListener;
import android.trithe.sqlapp.callback.OnShowTimeCinemaItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.model.Series;
import android.trithe.sqlapp.model.ShowingFilmByDate;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.CheckSeenNotificationManager;
import android.trithe.sqlapp.rest.manager.GetDataCastListManager;
import android.trithe.sqlapp.rest.manager.GetDataCommentFilmManager;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.manager.GetDataKindManager;
import android.trithe.sqlapp.rest.manager.GetDataRatingFilmManager;
import android.trithe.sqlapp.rest.manager.GetDataSeriesFilmManager;
import android.trithe.sqlapp.rest.manager.PushRatFilmManager;
import android.trithe.sqlapp.rest.manager.PushSendCommentFilmManager;
import android.trithe.sqlapp.rest.manager.SavedFilmManager;
import android.trithe.sqlapp.rest.model.CastListModel;
import android.trithe.sqlapp.rest.model.CommentFilmModel;
import android.trithe.sqlapp.rest.model.KindModel;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetAllDataCommentFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataCastListResponse;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataKindResponse;
import android.trithe.sqlapp.rest.response.GetDataRatingFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataSeriesFilmResponse;
import android.trithe.sqlapp.utils.DateUtils;
import android.trithe.sqlapp.utils.EndlessRecyclerOnScrollListener;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.utils.Utils;
import android.trithe.sqlapp.widget.CustomJzvd.MyJzvdStd;
import android.trithe.sqlapp.widget.Jz.Jzvd;
import android.trithe.sqlapp.widget.NativeTemplateStyle;
import android.trithe.sqlapp.widget.TemplateView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.Math.round;

public class DetailFilmActivity extends AppCompatActivity implements View.OnClickListener,
        OnSeriesItemClickListener,
        OnShowTimeCinemaItemClickListener,
        OnChangeSetItemClickLovedListener, RatingDialogListener {

    private List<CastListModel> list = new ArrayList<>();
    private List<KindModel> listKind = new ArrayList<>();
    private List<Series> seriesListCheck = new ArrayList<>();
    private List<CommentFilmModel> commentFilmModels = new ArrayList<>();
    private List<ShowingFilmByDate> listShowingFilmByDates = new ArrayList<>();

    private CastDetailAdapter adapter;
    private SeriesAdapter seriesAdapter;
    private CommentFilmAdapter commentFilmAdapter;
    private ShowTimeAdapter showTimeAdapter;

    public static final int REQUEST_LOGIN = 999;

    private String id;
    private boolean isLogin;
    private EditText edSend;
    private ImageView btnSend;
    private TextView txtKindFilm;
    private MyJzvdStd videoView;

    private Button btnPlay, btnRat;
    private RelativeLayout rlVideo;
    private Toolbar toolbar;
    private ProgressDialog pDialog;
    private String url, trailer, image;
    private CircleImageView imgCurrentImage;
    private FloatingActionButton flPlay;
    private AppBarLayout appbar;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewShow;
    private RecyclerView recyclerViewCmt;
    private TextView txtTitle, txtDetail, txtTime, txtDate, txtRating, txtReviews;
    private ImageView detailImage, imgCover, imgSaved, imgRating, imgBack, imgShare, imgSearch;
    private TextView txtName;
    private String name;
    private TemplateView template;
    private String thumb;
//    private NativeExpressAdView nativeExpress;

    private int page = 0;
    private int per_page = 4;
    private LinearLayoutManager linearLayoutManager;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        pDialog = new ProgressDialog(this);
        id = getIntent().getStringExtra(Constant.ID);
        isLogin = !SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty();
        if (getIntent().getStringExtra(Constant.NOTIFICATION) != null) {
            checkSeenFilm(id);
        }
        initView();
        initData();
        setSupportActionBar(toolbar);
        setUpAppBar();
        getFilmById();
        getRatingFilm(id);
        setUpAdapter();
        getDataKindFilm();
        getCommentByFilm();
        checkActionSend();
        recyclerViewShow.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 5);
        recyclerViewShow.setLayoutManager(mLayoutManager);
        recyclerViewShow.addItemDecoration(new GridSpacingItemDecorationUtils(5, dpToPx(), true));
        recyclerViewShow.setItemAnimator(new DefaultItemAnimator());
        recyclerViewShow.setAdapter(seriesAdapter);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        nativeExpress.loadAd(adRequest);
    }

    private void setAds() {
        MobileAds.initialize(this, getString(R.string.native_ad_unit_id));
        AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.native_ad_unit_id))
                .forUnifiedNativeAd(unifiedNativeAd -> {
                    ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(DetailFilmActivity.this, R.color.black));
                    NativeTemplateStyle styles = new
                            NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                    template.setStyles(styles);
                    template.setNativeAd(unifiedNativeAd);
                    template.setVisibility(View.VISIBLE);
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setUpAppBar() {
        appbar.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener) (appBarLayout, i) -> {
            if (i == 0) {
                toolbar.setBackgroundResource(R.drawable.black_gradian_reverse);
                txtName.setVisibility(View.GONE);
            } else {
                toolbar.setBackgroundColor(getColor(R.color.colorPrimary));
                txtName.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkSeenFilm(String film_id) {
        CheckSeenNotificationManager checkSeenNotificationManager = new CheckSeenNotificationManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        checkSeenNotificationManager.getDataNotification(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), film_id);
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

    private void initView() {
//        nativeExpress = findViewById(R.id.nativeExpress);
        template = findViewById(R.id.my_template);
        detailImage = findViewById(R.id.detail_image);
        appbar = findViewById(R.id.appbar);
        txtTitle = findViewById(R.id.txtTitle);
        txtName = findViewById(R.id.txtName);
        imgCover = findViewById(R.id.imgCover);
        flPlay = findViewById(R.id.flplay);
        txtDetail = findViewById(R.id.txtDetail);
        txtTime = findViewById(R.id.txtTime);
        imgSaved = findViewById(R.id.imgSaved);
        recyclerView = findViewById(R.id.recycler_view);
        txtKindFilm = findViewById(R.id.txtKindFilm);
        txtDate = findViewById(R.id.txtDate);
        imgRating = findViewById(R.id.imgRating);
        txtRating = findViewById(R.id.txtRating);
        txtReviews = findViewById(R.id.txtReviews);
        imgBack = findViewById(R.id.imgBack);
        imgShare = findViewById(R.id.imgShare);
        imgSearch = findViewById(R.id.imgSearch);
        videoView = findViewById(R.id.videoView);
        btnPlay = findViewById(R.id.btnPlay);
        rlVideo = findViewById(R.id.rlVideo);
        imgShare = findViewById(R.id.imgShare);
        toolbar = findViewById(R.id.toolbar);
        btnRat = findViewById(R.id.btnRat);
        edSend = findViewById(R.id.edSend);
        btnSend = findViewById(R.id.btnSend);
        imgCurrentImage = findViewById(R.id.imgCurrentImage);
        recyclerViewCmt = findViewById(R.id.recycler_view_cmt);
        recyclerViewShow = findViewById(R.id.recycler_view_show);

        detailImage.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        flPlay.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        btnRat.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        adapter = new CastDetailAdapter(list);
        adapter.setOnClickItemFilm(this);
        seriesAdapter = new SeriesAdapter(DetailFilmActivity.this, seriesListCheck);
        showTimeAdapter = new ShowTimeAdapter(listShowingFilmByDates, this);
        seriesAdapter.setOnItemClickListener(this);
        commentFilmAdapter = new CommentFilmAdapter(commentFilmModels);
        imgCover.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_anim));
        flPlay.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_anim));
        Glide.with(DetailFilmActivity.this).load(Config.LINK_LOAD_IMAGE + SharedPrefUtils.getString(Constant.KEY_USER_IMAGE, "")).into(imgCurrentImage);
    }

    private void getCommentByFilm() {
        commentFilmModels.clear();
        showProcessDialog();
        GetDataCommentFilmManager getDataCommentFilmManager = new GetDataCommentFilmManager(new ResponseCallbackListener<GetAllDataCommentFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataCommentFilmResponse data) {
                if (data.status.equals("200")) {
                    commentFilmModels.addAll(data.result);
                    commentFilmAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataCommentFilmManager.startGetDataCommentFilm(id);
    }

    private void getFilmById() {
        showProcessDialog();
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    if (!data.result.get(0).movie.isEmpty()) {
                        url = Config.LOAD_VIDEO_STORAGE + data.result.get(0).movie + Config.END_PART_VIDEO_STORAGE;
                    } else {
                        if (data.result.get(0).status == 1 || data.result.get(0).status == 2) {
                            btnPlay.setVisibility(View.GONE);
                        } else {
                            getSeriesFilm();
                        }
                    }
                    trailer = Config.LOAD_VIDEO_STORAGE + data.result.get(0).trailer + Config.END_PART_VIDEO_STORAGE;
                    image = data.result.get(0).image;
                    txtTitle.setText(data.result.get(0).name);
                    txtName.setText(data.result.get(0).name);
                    name = data.result.get(0).name;
                    if (data.result.get(0).sizes > 1) {
                        txtTime.setText(data.result.get(0).time + " min / episode");
                    } else {
                        txtTime.setText(data.result.get(0).time + " min");
                    }
                    DateUtils.parseDateFormatVN(txtDate, data.result.get(0).releaseDate);
                    txtDetail.setText(data.result.get(0).detail);
                    Glide.with(DetailFilmActivity.this).load(Config.LINK_LOAD_IMAGE + data.result.get(0).imageCover).into(imgCover);
                    Glide.with(DetailFilmActivity.this).load(Config.LINK_LOAD_IMAGE + image).into(detailImage);
                    thumb = Config.LINK_LOAD_IMAGE + data.result.get(0).imageCover;
                    if (data.result.get(0).saved == 1) {
                        Glide.with(DetailFilmActivity.this).load(R.drawable.saved).into(imgSaved);
                        imgSaved.setOnClickListener(v ->
                                checkPushWithCheckUser(id, Config.API_DELETE_SAVED));
                    } else {
                        Glide.with(DetailFilmActivity.this).load(R.drawable.not_saved).into(imgSaved);
                        imgSaved.setOnClickListener(v ->
                                checkPushWithCheckUser(id, Config.API_INSERT_SAVED));
                    }
                    disProcessDialog();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getDataFilmManager.startGetDataFilm(0, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id,
                0, 1000, Config.API_GET_FILM_BY_ID);
    }

    private void getSeriesFilm() {
        GetDataSeriesFilmManager getDataSeriesFilmManager = new GetDataSeriesFilmManager(new ResponseCallbackListener<GetDataSeriesFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataSeriesFilmResponse data) {
                if (data.status.equals("200")) {
                    url = Config.LOAD_VIDEO_STORAGE + data.result.get(0).link + Config.END_PART_VIDEO_STORAGE;
                    for (int i = 0; i < data.result.size(); i++) {
                        seriesListCheck.add(new Series(data.result.get(i), false));
                    }
                    seriesListCheck.get(0).setCheck(true);
                    seriesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataSeriesFilmManager.startGetDataSeriesFilm(id);
    }

    private void checkActionSend() {
        edSend.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                validateFormData();
            }
            return false;
        });
    }

    private void validateFormData() {
        if (edSend.getText().toString().isEmpty()) {
            Toast.makeText(DetailFilmActivity.this, R.string.cmt_null, Toast.LENGTH_SHORT).show();
        } else {
            pushSendCommentFilm();
        }
    }

    private void pushSendCommentFilm() {
        showProcessDialog();
        PushSendCommentFilmManager pushSendCommentFilmManager = new PushSendCommentFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    edSend.setText("");
                    getCommentByFilm();
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        pushSendCommentFilmManager.pushSendCommentFilm(edSend.getText().toString(), SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpAdapter();
        list.clear();
        getDataCast(page, per_page);
        setAds();
    }

    private void onClickPushSaved(final String id, String key) {
        showProcessDialog();
        SavedFilmManager savedFilmManager = new SavedFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    getFilmById();
                    disProcessDialog();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        savedFilmManager.startCheckSavedFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, key);
    }

    private void checkPushWithCheckUser(String id, String key) {
        if (!SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty()) {
            onClickPushSaved(id, key);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }
    }

    private void setUpAdapter() {
        adapter.setOnLoadMore(true);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                new Handler().postDelayed(() -> {
                    getDataCast(page, per_page);
                    Log.d("abc", page + "");
                }, 500);
            }
        });

        recyclerViewCmt.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(DetailFilmActivity.this);
        recyclerViewCmt.setLayoutManager(manager);
        recyclerViewCmt.setAdapter(commentFilmAdapter);
    }

    private void getDataCast(int page, int per_page) {
        GetDataCastListManager getDataCastListManager = new GetDataCastListManager(new ResponseCallbackListener<GetDataCastListResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataCastListResponse data) {
                if (data.status.equals("200")) {
                    list.addAll(data.result);
                    adapter.notifyDataSetChanged();
                    if (data.result.size() < 4) {
                        adapter.setOnLoadMore(false);
                    }
                } else {
                    adapter.setOnLoadMore(false);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
            }
        });
        getDataCastListManager.startGetDataCast(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""),
                getIntent().getStringExtra(Constant.ID), page, per_page);
    }

    private void getRatingFilm(String id) {
        GetDataRatingFilmManager getDataRatingFilmManager = new GetDataRatingFilmManager(new ResponseCallbackListener<GetDataRatingFilmResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onObjectComplete(String TAG, GetDataRatingFilmResponse data) {
                if (data.status.equals("200")) {
                    double rat = 0.0;
                    txtReviews.setText(data.result.size() + " Reviews");
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
        getDataRatingFilmManager.startGetDataRating(id);
    }

    private void checkRating(double rat) {
        if (rat == 5.0) {
            Glide.with(DetailFilmActivity.this).load(R.drawable.fivestar).into(imgRating);
        } else if (rat >= 4.0) {
            Glide.with(DetailFilmActivity.this).load(R.drawable.fourstar).into(imgRating);
        } else if (rat >= 3.0) {
            Glide.with(DetailFilmActivity.this).load(R.drawable.threestar).into(imgRating);
        } else if (rat >= 2.0) {
            Glide.with(DetailFilmActivity.this).load(R.drawable.twostar).into(imgRating);
        } else if (rat >= 1.0) {
            Glide.with(DetailFilmActivity.this).load(R.drawable.onestar).into(imgRating);
        }
    }

    private void getDataKindFilm() {
        listKind.clear();
        GetDataKindManager getDataKindManager = new GetDataKindManager(new ResponseCallbackListener<GetDataKindResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataKindResponse data) {
                if (data.status.equals("200")) {
                    listKind.addAll(data.result);
                    getKind();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataKindManager.startGetDataKind(id, Config.API_KIND_FILM_DETAIL);
    }

    private void showDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(3)
                .setTitle("Rate this movie")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(android.R.color.white)
                .setDescriptionTextColor(android.R.color.darker_gray)
                .setHint("Please write your comment here ...")
                .setHintTextColor(android.R.color.darker_gray)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .create(DetailFilmActivity.this)
                .show();
    }

    private void getKind() {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < listKind.size(); i++) {
            name.append(" ").append(listKind.get(i).name).append(",");
        }
        if (name.length() > 0) name = new StringBuilder(name.substring(0, name.length() - 1));
        txtKindFilm.setText(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgShare:
                Utils.shareUrl(this, url);
                break;
            case R.id.imgSearch:
                startActivity(new Intent(DetailFilmActivity.this, SearchActivity.class));
                break;
            case R.id.flplay:
                Intent intentPlay = new Intent(DetailFilmActivity.this, VideoActivity.class);
                intentPlay.putExtra(Constant.VIDEO, trailer);
                intentPlay.putExtra(Constant.PREFERENCE_NAME, name);
                intentPlay.putExtra(Constant.IMAGE, thumb);
                ActivityOptions optionPlay = ActivityOptions.makeSceneTransitionAnimation(this, imgCover, getResources().getString(R.string.shareName));
                startActivity(intentPlay, optionPlay.toBundle());
                break;
            case R.id.detail_image:
                Intent intentDetail = new Intent(DetailFilmActivity.this, ShowImageActivity.class);
                intentDetail.putExtra(Constant.IMAGE, image);
                ActivityOptions imgDetail = ActivityOptions.makeSceneTransitionAnimation(DetailFilmActivity.this, detailImage, getResources().getString(R.string.shareName));
                startActivity(intentDetail, imgDetail.toBundle());
                break;
            case R.id.btnPlay:
                btnPlay.setVisibility(View.GONE);
                rlVideo.setVisibility(View.VISIBLE);
                videoView.setUp(url, name);
                Glide.with(this).load(thumb).into(videoView.thumbImageView);
                break;
            case R.id.btnRat:
                if (isLogin) {
                    checkRat();
                } else {
                    Intent intents = new Intent(this, LoginActivity.class);
                    startActivityForResult(intents, REQUEST_LOGIN);
                }
                break;
            case R.id.btnSend:
                validateFormData();
                break;
        }
    }

    private void checkRat() {
        PushRatFilmManager pushRatFilmManager = new PushRatFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    Utils.showAlertDialog1(DetailFilmActivity.this, R.string.rated);
                } else {
                    showDialog();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
            }
        });
        pushRatFilmManager.pushRatFilm(null, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""),
                id, Config.API_CHECK_RAT);
    }

    @Override
    public void onNegativeButtonClicked() {
    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {
        showProcessDialog();
        PushRatFilmManager pushRatFilmManager = new PushRatFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    getRatingFilm(getIntent().getStringExtra(Constant.ID));
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        pushRatFilmManager.pushRatFilm(String.valueOf(i), SharedPrefUtils.getString(Constant.KEY_USER_ID, ""),
                id, Config.API_PUSH_RAT);
    }

    @Override
    public void onFilm(List<Series> seriesList, Series seriesModel) {
        url = Config.LOAD_VIDEO_STORAGE + seriesModel.getList().link + Config.END_PART_VIDEO_STORAGE;
        for (int i = 0; i < seriesList.size(); i++) {
            if (!seriesList.get(i).getList().id.equals(seriesModel.getList().id)) {
                seriesList.get(i).setCheck(false);
            } else {
                seriesList.get(i).setCheck(true);
            }
        }
        seriesAdapter.notifyDataSetChanged();
        videoView.setUp(url, name);
        Glide.with(this).load(thumb).into(videoView.thumbImageView);
    }

    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void changSetData() {
        if (SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        } else {
            list.clear();
            getDataCast(page, per_page);
        }
    }

    private int dpToPx() {
        Resources r = getResources();
        return round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, r.getDisplayMetrics()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                isLogin = true;
                getFilmById();
                list.clear();
                getDataCast(page, per_page);
            }
        }
    }

    @Override
    public void onShowingTime(List<ShowingFilmByDate> list, ShowingFilmByDate showingFilmByDate) {
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getList().id.equals(showingFilmByDate.getList().id)) {
                list.get(i).setCheck(false);
            } else {
                list.get(i).setCheck(true);
            }
        }
        showTimeAdapter.notifyDataSetChanged();
        Intent intent = new Intent(DetailFilmActivity.this, BookingSeatsActivity.class);
        intent.putExtra(Constant.THEATER_ID, showingFilmByDate.getList().theater_id);
        startActivity(intent);
    }
}

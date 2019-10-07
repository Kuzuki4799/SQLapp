package android.trithe.sqlapp.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.CastDetailAdapter;
import android.trithe.sqlapp.adapter.CommentFilmAdapter;
import android.trithe.sqlapp.adapter.SeriesAdapter;
import android.trithe.sqlapp.adapter.ShowTimeAdapter;
import android.trithe.sqlapp.callback.OnChangeSetCastItemClickListener;
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
import android.trithe.sqlapp.rest.manager.GetDataFilmDetailManager;
import android.trithe.sqlapp.rest.manager.GetDataRatingFilmManager;
import android.trithe.sqlapp.rest.manager.PushRatFilmManager;
import android.trithe.sqlapp.rest.manager.PushSendCommentFilmManager;
import android.trithe.sqlapp.rest.manager.SavedFilmManager;
import android.trithe.sqlapp.rest.model.CastListModel;
import android.trithe.sqlapp.rest.model.CommentFilmModel;
import android.trithe.sqlapp.rest.model.KindModel;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetAllDataCommentFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataCastListResponse;
import android.trithe.sqlapp.rest.response.GetDataFilmDetailResponse;
import android.trithe.sqlapp.rest.response.GetDataRatingFilmResponse;
import android.trithe.sqlapp.utils.DateUtils;
import android.trithe.sqlapp.utils.EndlessRecyclerOnScrollListener;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.utils.Utils;
import android.trithe.sqlapp.widget.CustomJzvd.MyJzvdStd;
import android.trithe.sqlapp.widget.Jz.Jzvd;
import android.trithe.sqlapp.widget.NativeTemplateStyle;
import android.trithe.sqlapp.widget.TemplateView;
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
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.Math.round;

public class DetailFilmActivity extends AppCompatActivity implements View.OnClickListener,
        OnSeriesItemClickListener,
        OnShowTimeCinemaItemClickListener,
        OnChangeSetCastItemClickListener, RatingDialogListener {

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
    private String url, trailer, image;
    private CircleImageView imgCurrentImage;
    private FloatingActionButton flPlay;
    private AppBarLayout appbar;
    private RecyclerView recyclerView, recyclerViewShow, recyclerViewCmt;
    private TextView txtTitle, txtDetail, txtTime, txtDate, txtRating, txtReviews;
    private ImageView detailImage, imgCover, imgSaved, imgRating, imgBack, imgShare, imgSearch;
    private TextView txtName;
    private String name, thumb;
    private TemplateView template;
    private int page = 0;
    private int per_page = 4;
    private LinearLayoutManager linearLayoutManager;
    private boolean key_check = true;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        id = getIntent().getStringExtra(Constant.ID);
        isLogin = !SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty();
        if (getIntent().getStringExtra(Constant.NOTIFICATION) != null) {
            checkSeenFilm(id);
        }
        initView();
        initData();
        setUpAppBar();
        getFilmById();
        getCommentByFilm();
        checkActionSend();
        resetLoadMore();
        listener();
        if (!isLogin) {
            imgCurrentImage.setVisibility(View.GONE);
        }
    }

    private void resetLoadMore() {
        setUpAdapter();
        list.clear();
        getDataCast(page, per_page);
        setAds();
    }

    private void setUpManagerRecyclerView() {
        recyclerViewShow.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 5);
        recyclerViewShow.setLayoutManager(mLayoutManager);
        recyclerViewShow.addItemDecoration(new GridSpacingItemDecorationUtils(5, dpToPx(), true));
        recyclerViewShow.setItemAnimator(new DefaultItemAnimator());
        recyclerViewShow.setAdapter(seriesAdapter);
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
        setSupportActionBar(toolbar);
        appbar.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener) (appBarLayout, i) -> {
            if (i == 0) {
                toolbar.setBackgroundResource(R.drawable.black_gradian_reverse);
                txtName.setVisibility(View.GONE);
            } else {
                toolbar.setBackgroundColor(getColor(R.color.colorPrimary));
                txtName.setVisibility(View.VISIBLE);
            }
        });
        setUpManagerRecyclerView();
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

    private void initView() {
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
    }

    private void listener() {
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
        GetDataFilmDetailManager getDataFilmDetailManager = new GetDataFilmDetailManager(
                new ResponseCallbackListener<GetDataFilmDetailResponse>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onObjectComplete(String TAG, GetDataFilmDetailResponse data) {
                        if (data.status.equals("200")) {
                            key_check = data.result.saved == 0;
                            handlerDataFilm(data);
                            handlerRat(data);
                            listKind.addAll(data.result.kind);
                            getKind();
                            handlerDataUrlFilm(data);
                            handlerDataSaved(data);
                        }
                    }

                    @Override
                    public void onResponseFailed(String TAG, String message) {

                    }
                });
        getDataFilmDetailManager.startGetDataFilmDetail(
                SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id);
    }

    private void handlerDataSaved(GetDataFilmDetailResponse data) {
        if (data.result.saved == 1) {
            Glide.with(DetailFilmActivity.this).load(R.drawable.saved).into(imgSaved);
            imgSaved.setOnClickListener(v ->
                    checkPushWithCheckUser(id, Config.API_DELETE_SAVED));
        } else {
            Glide.with(DetailFilmActivity.this).load(R.drawable.not_saved).into(imgSaved);
            imgSaved.setOnClickListener(v ->
                    checkPushWithCheckUser(id, Config.API_INSERT_SAVED));
        }
    }

    private void handlerDataUrlFilm(GetDataFilmDetailResponse data) {
        if (!data.result.movie.isEmpty()) {
            url = Config.LOAD_VIDEO_STORAGE + data.result.movie + Config.END_PART_VIDEO_STORAGE;
        } else {
            if (data.result.status == 1 || data.result.status == 2) {
                btnPlay.setVisibility(View.GONE);
            } else {
                url = Config.LOAD_VIDEO_STORAGE + data.result.series.get(0).link + Config.END_PART_VIDEO_STORAGE;
                for (int i = 0; i < data.result.series.size(); i++) {
                    seriesListCheck.add(new Series(data.result.series.get(i), false));
                }
                seriesListCheck.get(0).setCheck(true);
                seriesAdapter.notifyDataSetChanged();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void handlerDataFilm(GetDataFilmDetailResponse data) {
        trailer = Config.LOAD_VIDEO_STORAGE + data.result.trailer + Config.END_PART_VIDEO_STORAGE;
        image = data.result.image;
        txtTitle.setText(data.result.name);
        txtName.setText(data.result.name);
        name = data.result.name;
        if (data.result.sizes > 1) {
            txtTime.setText(data.result.time + " min / episode");
        } else {
            txtTime.setText(data.result.time + " min");
        }
        DateUtils.parseDateFormatVN(txtDate, data.result.releaseDate);
        txtDetail.setText(data.result.detail);
        Glide.with(DetailFilmActivity.this).load(Config.LINK_LOAD_IMAGE + data.result.imageCover).into(imgCover);
        Glide.with(DetailFilmActivity.this).load(Config.LINK_LOAD_IMAGE + image).into(detailImage);
        thumb = Config.LINK_LOAD_IMAGE + data.result.imageCover;
    }

    @SuppressLint("SetTextI18n")
    private void handlerRat(GetDataFilmDetailResponse data) {
        if (data.result.rat.size() != 0) {
            double rat = 0.0;
            txtReviews.setText(data.result.rat.size() + " Reviews");
            for (int i = 0; i < data.result.rat.size(); i++) {
                rat += data.result.rat.get(i).rat;
            }
            txtRating.setText(String.valueOf(rat / data.result.rat.size()));
            try {
                checkRating(rat / data.result.rat.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            txtRating.setText("0.0");
        }
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
        PushSendCommentFilmManager pushSendCommentFilmManager = new PushSendCommentFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    edSend.setText("");
                    getCommentByFilm();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        pushSendCommentFilmManager.pushSendCommentFilm(edSend.getText().toString(),
                SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id);
    }

    private void onClickPushSaved(final String id, String key) {
        SavedFilmManager savedFilmManager = new SavedFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    key_check = key.equals(Config.API_DELETE_SAVED);
                    getFilmById();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
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
                new Handler().postDelayed(() -> getDataCast(page, per_page), 500);
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
                if(isLogin) {
                    validateFormData();
                }else {
                    Intent intents = new Intent(this, LoginActivity.class);
                    startActivityForResult(intents, REQUEST_LOGIN);
                }
                break;
        }
    }

    private void checkRat() {
        PushRatFilmManager pushRatFilmManager = new PushRatFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailFilmActivity.this);
                    builder.setTitle(setColorTextDialog(getResources().getString(R.string.notification)));
                    builder.setMessage(setColorTextDialog(getResources().getString(R.string.rated)));
                    builder.setPositiveButton(R.string.strOk, (dialog, which) -> dialog.cancel()
                    );
                    builder.show();
                } else {
                    Utils.showDialog(DetailFilmActivity.this);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
            }
        });
        pushRatFilmManager.pushRatFilm(null, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""),
                id, Config.API_CHECK_RAT);
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

    @Override
    public void onNegativeButtonClicked() {
    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {
        PushRatFilmManager pushRatFilmManager = new PushRatFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    getRatingFilm(getIntent().getStringExtra(Constant.ID));
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
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
        Intent intent = new Intent();
        intent.putExtra(Constant.KEY_CHECK, key_check);
        intent.putExtra(Constant.POSITION, getIntent().getIntExtra(Constant.POSITION, 0));
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCheckItemCast(int position, CardView cardView) {
        Intent intent = new Intent(this, CastActivity.class);
        intent.putExtra(Constant.ID, list.get(position).castId);
        intent.putExtra(Constant.POSITION, position);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, cardView, getResources().getString(R.string.app_name));
        startActivityForResult(intent, Constant.KEY_INTENT_CAST, options.toBundle());
    }

    @Override
    public void changSetDataCast(int position, String key) {
        if (SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        } else {
            if (key.equals(Config.API_DELETE_LOVE_CAST)) {
                list.get(position).setLoved(0);
            } else {
                list.get(position).setLoved(1);
            }
            adapter.notifyItemChanged(position);
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
                imgCurrentImage.setVisibility(View.VISIBLE);
                Glide.with(DetailFilmActivity.this).load(Config.LINK_LOAD_IMAGE + SharedPrefUtils.getString(Constant.KEY_USER_IMAGE, "")).into(imgCurrentImage);
                getFilmById();
                list.clear();
                getDataCast(page, per_page);
            }

            if (requestCode == Constant.KEY_INTENT_CAST) {
                if (Objects.requireNonNull(data).getBooleanExtra(Constant.KEY_CHECK, false)) {
                    list.get(data.getIntExtra(Constant.POSITION, 0)).setLoved(0);
                } else {
                    list.get(data.getIntExtra(Constant.POSITION, 0)).setLoved(1);
                }
                adapter.notifyItemChanged(data.getIntExtra(Constant.POSITION, 0));
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

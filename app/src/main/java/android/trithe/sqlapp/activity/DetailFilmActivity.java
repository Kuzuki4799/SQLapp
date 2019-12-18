package android.trithe.sqlapp.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import android.trithe.sqlapp.callback.OnChangeSetCastItemClickListener;
import android.trithe.sqlapp.callback.OnSeriesItemClickListener;
import android.trithe.sqlapp.callback.OnShowTimeCinemaItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.model.Series;
import android.trithe.sqlapp.model.ShowingFilmByDate;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.CheckSeenNotificationManager;
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
import android.trithe.sqlapp.rest.response.GetDataFilmDetailResponse;
import android.trithe.sqlapp.rest.response.GetDataRatingFilmResponse;
import android.trithe.sqlapp.utils.DateUtils;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
import android.trithe.sqlapp.utils.NotificationHelper;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.utils.Utils;
import android.trithe.sqlapp.widget.CustomJzvd.MyJzvdStd;
import android.trithe.sqlapp.widget.CustomeRecyclerView;
import android.trithe.sqlapp.widget.Jz.Jzvd;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.LinearLayout.HORIZONTAL;

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
    private Toolbar toolbar;
    private boolean isLogin;
    private EditText edSend;
    private TextView txtName;
    private ImageView btnSend;
    private String name, thumb;
    private AppBarLayout appbar;
    private MyJzvdStd videoView;
    private TextView txtKindFilm;
    private Button btnPlay, btnRat;
    private RelativeLayout rlVideo;
    private boolean key_check = true;
    private String url, trailer, image;
    private FloatingActionButton flPlay;
    private CircleImageView imgCurrentImage;
    private CustomeRecyclerView recyclerView;
    private RecyclerView recyclerViewShow, recyclerViewCmt;
    private TextView txtTitle, txtDetail, txtTime, txtDate, txtRating, txtReviews;
    private ImageView detailImage, imgCover, imgSaved, imgRating, imgBack, imgShare, imgSearch;
    private NativeExpressAdView nativeExpress;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        id = getIntent().getStringExtra(Constant.ID);
        isLogin = !SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty();
        if (getIntent().getIntExtra(Constant.NOTIFICATION, 0) != 0) {
            checkSeenFilm(id);
        }
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.inters_image_ad_unit_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        initView();
        initData();
        setUpAdapter();
        setUpAppBar();
        getFilmById();
        getCommentByFilm();
        checkActionSend();
        listener();
        AdRequest adRequest = new AdRequest.Builder().build();
        nativeExpress.loadAd(adRequest);
        if (!isLogin) {
            imgCurrentImage.setVisibility(View.GONE);
        }
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.clear();
    }

    private void setUpManagerRecyclerView() {
        recyclerViewShow.setHasFixedSize(true);
        recyclerViewShow.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerViewShow.addItemDecoration(new GridSpacingItemDecorationUtils(5, Utils.dpToPx(this, 7), true));
        recyclerViewShow.setItemAnimator(new DefaultItemAnimator());
        recyclerViewShow.setAdapter(seriesAdapter);
    }

    private void setUpAppBar() {
        setSupportActionBar(toolbar);
        appbar.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener) (appBarLayout, i) -> {
            if (i == 0) {
                toolbar.setBackgroundResource(R.drawable.black_gradian_reverse);
                txtName.setVisibility(View.GONE);
            } else {
                toolbar.setBackgroundResource(R.color.colorPrimary);
                txtName.setVisibility(View.VISIBLE);
            }
        });
        setUpManagerRecyclerView();
    }

    private void checkSeenFilm(String film_id) {
        new CheckSeenNotificationManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        }).getDataNotification(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), film_id);
    }

    private void initView() {
        nativeExpress = findViewById(R.id.nativeExpress);
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
        new GetDataCommentFilmManager(new ResponseCallbackListener<GetAllDataCommentFilmResponse>() {
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
        }).startGetDataCommentFilm(id);
    }

    private void getFilmById() {
        new GetDataFilmDetailManager(
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
                            getDataCast(data.result.cast);
                            handlerDataUrlFilm(data);
                            handlerDataSaved(data);
                        }
                    }

                    @Override
                    public void onResponseFailed(String TAG, String message) {

                    }
                }).startGetDataFilmDetail(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id);
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
                if (isLogin) {
                    validateFormData();
                } else {
                    Intent intents = new Intent(this, LoginActivity.class);
                    startActivityForResult(intents, REQUEST_LOGIN);
                }
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
        new PushSendCommentFilmManager(new ResponseCallbackListener<BaseResponse>() {
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
        }).pushSendCommentFilm(edSend.getText().toString(), SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id);
    }

    private void onClickPushSaved(final String id, String key) {
        new SavedFilmManager(new ResponseCallbackListener<BaseResponse>() {
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
        }).startCheckSavedFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, key);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        recyclerViewCmt.setHasFixedSize(true);
        recyclerViewCmt.setLayoutManager(new LinearLayoutManager(DetailFilmActivity.this));
        recyclerViewCmt.setAdapter(commentFilmAdapter);
    }

    private void getDataCast(List<CastListModel> listCast) {
        list.clear();
        list.addAll(listCast);
        adapter.notifyDataSetChanged();
        adapter.setOnLoadMore(false);
    }

    private void getRatingFilm(String id) {
        new GetDataRatingFilmManager(new ResponseCallbackListener<GetDataRatingFilmResponse>() {
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
        }).startGetDataRating(id);
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
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions optionPlay = ActivityOptions.makeSceneTransitionAnimation(this, imgCover, getResources().getString(R.string.shareName));
                    startActivity(intentPlay, optionPlay.toBundle());
                } else {
                    startActivity(intentPlay);
                }
                break;
            case R.id.detail_image:
                Intent intentDetail = new Intent(DetailFilmActivity.this, ShowImageActivity.class);
                intentDetail.putExtra(Constant.IMAGE, image);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions imgDetail = ActivityOptions.makeSceneTransitionAnimation(DetailFilmActivity.this, detailImage, getResources().getString(R.string.shareName));
                    startActivity(intentDetail, imgDetail.toBundle());
                } else {
                    startActivity(intentDetail);
                }
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
                if (isLogin) {
                    validateFormData();
                } else {
                    Intent intents = new Intent(this, LoginActivity.class);
                    startActivityForResult(intents, REQUEST_LOGIN);
                }
                break;
        }
    }

    private void checkRat() {
        new PushRatFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    Toast.makeText(DetailFilmActivity.this, getResources().getString(R.string.rated), Toast.LENGTH_LONG).show();
                } else {
                    Utils.showDialog(DetailFilmActivity.this);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
            }
        }).pushRatFilm(null, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, Config.API_CHECK_RAT);
    }

    @Override
    public void onNegativeButtonClicked() {
    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {
        new PushRatFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    getRatingFilm(getIntent().getStringExtra(Constant.ID));
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
            }
        }).pushRatFilm(String.valueOf(i), SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, Config.API_PUSH_RAT);
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

    @Override
    public void onCheckItemCast(int position, CardView cardView) {
        Intent intent = new Intent(this, CastActivity.class);
        intent.putExtra(Constant.ID, list.get(position).castId);
        intent.putExtra(Constant.POSITION, position);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                interstitialAd.loadAd(new AdRequest.Builder().build());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DetailFilmActivity.this, cardView, getResources().getString(R.string.app_name));
                    startActivityForResult(intent, Constant.KEY_INTENT_CAST, options.toBundle());
                } else {
                    startActivityForResult(intent, Constant.KEY_INTENT_CAST);
                }
            }
        });
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, cardView, getResources().getString(R.string.app_name));
                startActivityForResult(intent, Constant.KEY_INTENT_CAST, options.toBundle());
            } else {
                startActivityForResult(intent, Constant.KEY_INTENT_CAST);
            }
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                isLogin = true;
                imgCurrentImage.setVisibility(View.VISIBLE);
                Glide.with(DetailFilmActivity.this).load(Config.LINK_LOAD_IMAGE + SharedPrefUtils.getString(Constant.KEY_USER_IMAGE, "")).into(imgCurrentImage);
                getFilmById();
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

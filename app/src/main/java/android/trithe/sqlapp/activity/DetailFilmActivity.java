package android.trithe.sqlapp.activity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
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
import android.trithe.sqlapp.callback.OnCastItemClickListener;
import android.trithe.sqlapp.callback.OnSeriesItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.model.Series;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
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
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.utils.Utils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailFilmActivity extends AppCompatActivity implements View.OnClickListener, OnSeriesItemClickListener, OnCastItemClickListener, RatingDialogListener {
    private ImageView detailImage, imgCover, imgSaved, imgRating, imgBack, imgShare, imgSearch, imgFull;
    private TextView txtTitle, txtDetail, txtTime, txtDate, txtRating, txtReviews;
    private FloatingActionButton flPlay;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewShow;
    private RecyclerView recyclerViewCmt;
    private List<CastListModel> list = new ArrayList<>();
    private List<KindModel> listKind = new ArrayList<>();
    private List<Series> seriesListCheck = new ArrayList<>();
    private List<CommentFilmModel> commentFilmModels = new ArrayList<>();
    private CastDetailAdapter adapter;
    private SeriesAdapter seriesAdapter;
    private CommentFilmAdapter commentFilmAdapter;
    private TextView txtKindFilm;
    private VideoView videoView;
    private Button btnPlay, btnRat;
    private RelativeLayout rlVideo;
    private String url, trailer, image;
    private Toolbar toolbar;
    private ProgressDialog pDialog;
    public static final int REQUEST_LOGIN = 999;
    private String id;
    private boolean isLogin;
    private CircleImageView imgCurrentImage;
    private EditText edSend;
    private ImageView btnSend;
    private int position;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        pDialog = new ProgressDialog(this);
        id = getIntent().getStringExtra(Constant.ID);
        isLogin = !SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty();
        initView();
        initData();
        setSupportActionBar(toolbar);
        getFilmById();
        getDataCast();
        getRatingFilm(id);
        setUpAdapter();
        getDataKindFilm();
        getCommentByFilm();
        checkActionSend();
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
        detailImage = findViewById(R.id.detail_image);
        txtTitle = findViewById(R.id.txtTitle);
        imgCover = findViewById(R.id.imgCover);
        flPlay = findViewById(R.id.flplay);
        txtDetail = findViewById(R.id.txtDetail);
        txtTime = findViewById(R.id.txtTime);
        imgSaved = findViewById(R.id.imgSaved);
        recyclerView = findViewById(R.id.recyler_view);
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
        imgFull = findViewById(R.id.imgFull);
        imgShare = findViewById(R.id.imgShare);
        toolbar = findViewById(R.id.toolbar);
        btnRat = findViewById(R.id.btnRat);
        recyclerViewShow = findViewById(R.id.recycler_view_show);
        recyclerViewCmt = findViewById(R.id.recycler_view_cmt);
        imgCurrentImage = findViewById(R.id.imgCurrentImage);
        edSend = findViewById(R.id.edSend);
        btnSend = findViewById(R.id.btnSend);

        detailImage.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        flPlay.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgFull.setOnClickListener(this);
        btnRat.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        adapter = new CastDetailAdapter(list);
        adapter.setOnClickItemFilm(this);
        seriesAdapter = new SeriesAdapter(DetailFilmActivity.this, seriesListCheck);
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
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    if (!data.result.get(0).movie.isEmpty()) {
                        url = Config.LOAD_VIDEO_STORAGE + data.result.get(0).movie + Config.END_PART_VIDEO_STORAGE;
                    } else {
                        getSeriesFilm();
                    }
                    trailer = Config.LOAD_VIDEO_STORAGE + data.result.get(0).trailer + Config.END_PART_VIDEO_STORAGE;
                    image = data.result.get(0).image;
                    txtTitle.setText(data.result.get(0).name);
                    txtTime.setText(data.result.get(0).time + " min");
                    DateUtils.parseDateFormatVN(txtDate, data.result.get(0).releaseDate);
                    txtDetail.setText(data.result.get(0).detail);
                    Glide.with(DetailFilmActivity.this).load(Config.LINK_LOAD_IMAGE + data.result.get(0).imageCover).into(imgCover);
                    Glide.with(DetailFilmActivity.this).load(Config.LINK_LOAD_IMAGE + image).into(detailImage);

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
        getDataFilmManager.startGetDataFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, Config.API_GET_FILM_BY_ID);
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
        adapter.notifyDataSetChanged();
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
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 4);
        recyclerViewShow.setLayoutManager(mLayoutManager);
        recyclerViewShow.addItemDecoration(new GridSpacingItemDecorationUtils(4, dpToPx(), true));
        recyclerViewShow.setItemAnimator(new DefaultItemAnimator());
        recyclerViewShow.setAdapter(seriesAdapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(DetailFilmActivity.this);
        recyclerViewCmt.setLayoutManager(manager);
        recyclerViewCmt.setAdapter(commentFilmAdapter);
    }

    private void getDataCast() {
        list.clear();
        showProcessDialog();
        GetDataCastListManager getDataCastListManager = new GetDataCastListManager(new ResponseCallbackListener<GetDataCastListResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataCastListResponse data) {
                if (data.status.equals("200")) {
                    list.addAll(data.result);
                    adapter.notifyDataSetChanged();
                    disProcessDialog();
                }
            }


            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getDataCastListManager.startGetDataCast(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), getIntent().getStringExtra(Constant.ID));
    }

    private void getRatingFilm(String id) {
        GetDataRatingFilmManager getDataRatingFilmManager = new GetDataRatingFilmManager(new ResponseCallbackListener<GetDataRatingFilmResponse>() {
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
        getDataRatingFilmManager.startGetDataRatingFilm(id);
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
                Intent intent = new Intent(DetailFilmActivity.this, SearchActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DetailFilmActivity.this, imgSearch, getResources().getString(R.string.shareName));
                startActivity(intent, options.toBundle());
                break;
            case R.id.flplay:
                Intent intentPlay = new Intent(DetailFilmActivity.this, VideoActivity.class);
                intentPlay.putExtra(Constant.VIDEO, trailer);
                ActivityOptions optionPlay = ActivityOptions.makeSceneTransitionAnimation(this, imgCover, getResources().getString(R.string.shareName));
                startActivity(intentPlay, optionPlay.toBundle());
                break;
            case R.id.imgFull:
                Intent intentImgFulls = new Intent(DetailFilmActivity.this, VideoActivity.class);
                intentImgFulls.putExtra(Constant.VIDEO, url);
                SharedPrefUtils.putInt(Constant.POSITION, videoView.getCurrentPosition());
                ActivityOptions anim = ActivityOptions.makeSceneTransitionAnimation(DetailFilmActivity.this, imgFull, getResources().getString(R.string.shareName));
                startActivity(intentImgFulls, anim.toBundle());
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
                videoView.setVideoURI(Uri.parse(url));
                MediaController mediaController = new MediaController(this);
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);
                videoView.requestFocus();
                videoView.start();
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
    public void onFilm(List<Series> seriesList, Series seriesModel, int position) {
        url = Config.LOAD_VIDEO_STORAGE + seriesModel.getList().link + Config.END_PART_VIDEO_STORAGE;
        for (int i = 0; i < seriesList.size(); i++) {
            if (!seriesList.get(i).getList().id.equals(seriesModel.getList().id)) {
                seriesList.get(i).setCheck(false);
            } else {
                seriesList.get(i).setCheck(true);
            }
        }
        seriesAdapter.notifyDataSetChanged();
        videoView.setVideoURI(Uri.parse(url));
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    public void changSetData() {
        if (SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        } else {
            getDataCast();
        }
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                isLogin = true;
                getFilmById();
                getDataCast();
            }
        }
    }
}

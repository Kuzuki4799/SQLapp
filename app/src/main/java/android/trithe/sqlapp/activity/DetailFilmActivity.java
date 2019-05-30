package android.trithe.sqlapp.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.CastDetailAdapter;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataCastListManager;
import android.trithe.sqlapp.rest.manager.GetDataKindManager;
import android.trithe.sqlapp.rest.manager.GetDataRatingFilmManager;
import android.trithe.sqlapp.rest.manager.SavedFilmManager;
import android.trithe.sqlapp.rest.model.CastListModel;
import android.trithe.sqlapp.rest.model.KindModel;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataCastListResponse;
import android.trithe.sqlapp.rest.response.GetDataKindResponse;
import android.trithe.sqlapp.rest.response.GetDataRatingFilmResponse;
import android.trithe.sqlapp.utils.DateUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.utils.Utils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class DetailFilmActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView detailImage, imgCover, imgSaved, imgRating, imgBack, imgShare, imgSearch, imgFull;
    private TextView txtTitle, txtDetail, txtTime, txtDate, txtRating, txtReviews;
    private FloatingActionButton flplay;
    private RecyclerView recylerView;
    private List<CastListModel> list = new ArrayList<>();
    private List<KindModel> listKind = new ArrayList<>();
    private CastDetailAdapter adapter;
    private TextView txtKindFilm;
    private VideoView videoView;
    private Button btnPlay;
    private RelativeLayout rlVideo;
    private String url;
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        initView();
        initData();
        setSupportActionBar(toolbar);
        getDataCast();
        getRatingFilm(getIntent().getStringExtra(Constant.ID));
        checkSaved(getIntent().getStringExtra(Constant.ID));
        setUpAdapter();
        getDataKindFilm();
    }

    private void initView() {
        detailImage = findViewById(R.id.detail_image);
        txtTitle = findViewById(R.id.txtTitle);
        imgCover = findViewById(R.id.imgCover);
        flplay = findViewById(R.id.flplay);
        txtDetail = findViewById(R.id.txtDetail);
        txtTime = findViewById(R.id.txtTime);
        imgSaved = findViewById(R.id.imgSaved);
        recylerView = findViewById(R.id.recyler_view);
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

        detailImage.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        flplay.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgFull.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        adapter = new CastDetailAdapter(list);
        url = Config.LOAD_VIDEO_STORAGE + getIntent().getStringExtra(Constant.MOVIE) + Config.END_PART_VIDEO_STORAGE;
        imgCover.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_anim));
        flplay.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_anim));
        txtTitle.setText(getIntent().getStringExtra(Constant.TITLE));
        txtTime.setText(getIntent().getIntExtra(Constant.TIME, 0) + " min");
        DateUtils.parseDateFormatVN(txtDate, getIntent().getStringExtra(Constant.DATE));
        txtDetail.setText(getIntent().getStringExtra(Constant.DETAIL));
        Glide.with(this).load(Config.LINK_LOAD_IMAGE + getIntent().getStringExtra(Constant.IMAGE_COVER)).into(imgCover);
        Glide.with(this).load(Config.LINK_LOAD_IMAGE + getIntent().getStringExtra(Constant.IMAGE)).into(detailImage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void checkSaved(final String id) {
        SavedFilmManager savedFilmManager = new SavedFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    Glide.with(DetailFilmActivity.this).load(R.drawable.saved).into(imgSaved);
                    imgSaved.setOnClickListener(v -> onClickPushSaved(id, Config.API_DELETE_SAVED));
                } else {
                    Glide.with(DetailFilmActivity.this).load(R.drawable.not_saved).into(imgSaved);
                    imgSaved.setOnClickListener(v -> onClickPushSaved(id, Config.API_INSERT_SAVED));
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        savedFilmManager.startCheckSavedFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, Config.API_CHECK_SAVED);
    }

    private void onClickPushSaved(final String id, String key) {
        SavedFilmManager savedFilmManager = new SavedFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    checkSaved(id);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        savedFilmManager.startCheckSavedFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, key);
    }

    private void setUpAdapter() {
        recylerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recylerView.setLayoutManager(linearLayoutManager);
        recylerView.setAdapter(adapter);
    }

    private void getDataCast() {
        list.clear();
        GetDataCastListManager getDataCastListManager = new GetDataCastListManager(new ResponseCallbackListener<GetDataCastListResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataCastListResponse data) {
                if (data.status.equals("200")) {
                    list.addAll(data.result);
                    adapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataCastListManager.startGetDataCast(getIntent().getStringExtra(Constant.ID));
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
                    checkRating(rat);
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
        getDataKindManager.startGetDataKind(getIntent().getStringExtra(Constant.ID), Config.API_KIND_FILM_DETAIL);
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
                Intent intent = new Intent(DetailFilmActivity.this, KindActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DetailFilmActivity.this, imgSearch, getResources().getString(R.string.shareName));
                startActivity(intent, options.toBundle());
                break;
            case R.id.flplay:
                Intent intentPlay = new Intent(DetailFilmActivity.this, VideoActivity.class);
                intentPlay.putExtra(Constant.VIDEO, getIntent().getStringExtra(Constant.TRAILER));
                intentPlay.putExtra(Constant.TYPE, Constant.TYPE_TRAILER);
                ActivityOptions optionPlay = ActivityOptions.makeSceneTransitionAnimation(this, imgCover, getResources().getString(R.string.shareName));
                startActivity(intentPlay, optionPlay.toBundle());
                break;
            case R.id.imgFull:
                Intent intentImgFulls = new Intent(DetailFilmActivity.this, VideoActivity.class);
                intentImgFulls.putExtra(Constant.VIDEO, getIntent().getStringExtra(Constant.MOVIE));
                intentImgFulls.putExtra(Constant.TYPE, Constant.TYPE_FILM);
                ActivityOptions anim = ActivityOptions.makeSceneTransitionAnimation(DetailFilmActivity.this, imgFull, getResources().getString(R.string.shareName));
                startActivity(intentImgFulls, anim.toBundle());
                break;
            case R.id.detail_image:
                Intent intentDetail = new Intent(DetailFilmActivity.this, ShowImageActivity.class);
                intentDetail.putExtra(Constant.IMAGE, getIntent().getStringExtra(Constant.IMAGE));
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
        }
    }
}

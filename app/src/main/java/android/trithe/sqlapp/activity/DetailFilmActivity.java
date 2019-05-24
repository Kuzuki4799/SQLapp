package android.trithe.sqlapp.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.CastAdapter;
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
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class DetailFilmActivity extends AppCompatActivity {
    private ImageView detailImage, imgCover, imgSaved, imgRating, btnBack;
    private TextView txtTitle, txtDetail, txtTime, txtDate, txtRating, txtReviews;
    private FloatingActionButton flplay;
    private RecyclerView recylerView;
    private List<CastListModel> list = new ArrayList<>();
    private List<KindModel> listKind = new ArrayList<>();
    private CastAdapter adapter;
    private TextView txtKindFilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        initView();
        adapter = new CastAdapter(list);
        getDataCast();
        getRatingFilm(getIntent().getStringExtra(Constant.ID));
        checkSaved(getIntent().getStringExtra(Constant.ID));
        setUpAdapter();
        getDataKindFilm();
        detailImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailFilmActivity.this, ShowImageFilmActivity.class);
                intent.putExtra(Constant.IMAGE, getIntent().getStringExtra(Constant.IMAGE));
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation( DetailFilmActivity.this, detailImage, "sharedName");
                startActivity(intent, options.toBundle());
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
        btnBack = findViewById(R.id.btnBack);

        imgCover.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_anim));
        flplay.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_anim));
        txtTitle.setText(getIntent().getStringExtra(Constant.TITLE));
        txtTime.setText("Time: " + getIntent().getIntExtra(Constant.TIME, 0) + " min");
        DateUtils.parseDateFormatVN(txtDate, getIntent().getStringExtra(Constant.DATE));
        txtDetail.setText(getIntent().getStringExtra(Constant.DETAIL));
        Glide.with(this).load(Config.LINK_LOAD_IMAGE + getIntent().getStringExtra(Constant.IMAGE_COVER)).into(imgCover);
        Glide.with(this).load(Config.LINK_LOAD_IMAGE + getIntent().getStringExtra(Constant.IMAGE)).into(detailImage);
    }

    private void checkSaved(final String id) {
        SavedFilmManager savedFilmManager = new SavedFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    Glide.with(DetailFilmActivity.this).load(R.drawable.saved).into(imgSaved);
                    imgSaved.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickPushSaved(id, Config.API_DELETE_SAVED);
                            Glide.with(DetailFilmActivity.this).load(R.drawable.not_saved).into(imgSaved);
                        }
                    });
                } else {
                    Glide.with(DetailFilmActivity.this).load(R.drawable.not_saved).into(imgSaved);
                    imgSaved.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickPushSaved(id, Config.API_INSERT_SAVED);
                            Glide.with(DetailFilmActivity.this).load(R.drawable.saved).into(imgSaved);
                        }
                    });
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        savedFilmManager.startCheckSavedFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, Config.API_CHECK_SAVED);
    }

    private void onClickPushSaved(String id, String key) {
        SavedFilmManager savedFilmManager = new SavedFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {

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
        getDataCastListManager.startGetDataCast(getIntent().getStringExtra("id"));
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

    private void checkRating(double rat){
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
        getDataKindManager.startGetDataKind(getIntent().getStringExtra("id"), Config.API_KIND_FILM_DETAIL);
    }

    private void getKind() {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < listKind.size(); i++) {
            name.append(" ").append(listKind.get(i).name).append(",");
        }
        if (name.length() > 0) name = new StringBuilder(name.substring(0, name.length() - 1));
        txtKindFilm.setText("Kind: " + name);
    }
}

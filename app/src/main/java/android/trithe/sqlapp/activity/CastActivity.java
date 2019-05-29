package android.trithe.sqlapp.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.FilmAdapter;
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataCastCountryManager;
import android.trithe.sqlapp.rest.manager.GetDataCastDetailManager;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.manager.GetDataJobManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.model.JobandCountryModel;
import android.trithe.sqlapp.rest.response.GetDataCastDetailResponse;
import android.trithe.sqlapp.rest.response.GetDataCountryResponse;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataJobResponse;
import android.trithe.sqlapp.utils.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CastActivity extends AppCompatActivity implements OnFilmItemClickListener {
    private String id;
    private ImageView btnBack;
    private TextView txtLikeCount;
    private ImageView imgAvatar;
    private TextView txtName;
    private ImageView imgCover;
    private TextView txtDate;
    private TextView txtCountry;
    private TextView txtJob;
    private TextView txtInfo;
    private RecyclerView recyclerViewByCast;
    private FilmAdapter adapter;
    private List<FilmModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);
        initView();
        id = getIntent().getStringExtra("id");
        adapter = new FilmAdapter(list);
        adapter.setOnClickItemFilm(this);
        setUpRecyclerView();
        getDataCast();
        getJobCast();
        getDataCountry();
//        getLikeCount();
        getFilm();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpRecyclerView() {
        recyclerViewByCast.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CastActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewByCast.setLayoutManager(linearLayoutManager);
        recyclerViewByCast.setAdapter(adapter);
    }

    private void initView() {
//        txtLikeCount = findViewById(R.id.txtLikeCount);
        imgCover = findViewById(R.id.imgCover);
        btnBack = findViewById(R.id.btnBack);
        imgAvatar = findViewById(R.id.imgAvatar);
        txtName = findViewById(R.id.txtName);
        txtDate = findViewById(R.id.txtDate);
        txtCountry = findViewById(R.id.txtCountry);
        txtJob = findViewById(R.id.txtJob);
        txtInfo = findViewById(R.id.txtInfo);
        recyclerViewByCast = findViewById(R.id.recycler_view_by_cast);
    }

    private void getDataCast() {
        GetDataCastDetailManager getDataCastDetailManager = new GetDataCastDetailManager(new ResponseCallbackListener<GetDataCastDetailResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataCastDetailResponse data) {
                if (data.status.equals("200")) {
                    txtName.setText(data.result.name);
                    Glide.with(CastActivity.this).load(Config.LINK_LOAD_IMAGE + data.result.image).into(imgAvatar);
                    Glide.with(CastActivity.this).load(Config.LINK_LOAD_IMAGE + data.result.imageCover).into(imgCover);
                    DateUtils.parseDateFormat(txtDate, data.result.dateOfBirth);
                    txtInfo.setText(data.result.infomation);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataCastDetailManager.startGetDataCast(id);
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
        txtJob.setText("Job: " + name);
    }

//    private void getLikeCount() {
//        GetDataLoveCountManager getDataLoveCountManager = new GetDataLoveCountManager(new ResponseCallbackListener<GetDataLoveCountResponse>() {
//            @Override
//            public void onObjectComplete(String TAG, GetDataLoveCountResponse data) {
//                if (data.status.equals("200")) {
//                    txtLikeCount.setText(String.valueOf(data.result.size()));
//                } else {
//                    txtLikeCount.setText("0");
//                }
//            }
//
//            @Override
//            public void onResponseFailed(String TAG, String message) {
//
//            }
//        });
//        getDataLoveCountManager.startGetDataLoveCount(id);
//    }

    private void getDataCountry() {
        GetDataCastCountryManager getDataCastCountryManager = new GetDataCastCountryManager(new ResponseCallbackListener<GetDataCountryResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataCountryResponse data) {
                if (data.status.equals("200")) {
                    txtCountry.setText("Country: " + data.result.name);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataCastCountryManager.startGetDataCastCountry(id);
    }

    private void getFilm() {
        list.clear();
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    list.addAll(data.result);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataFilmManager.startGetDataFilm(id, Config.API_GET_FILM_BY_CAST);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onFilm(FilmModel filmModel, ImageView imageView) {
        Intent intent = new Intent(CastActivity.this, DetailFilmActivity.class);
        intent.putExtra(Constant.TITLE, filmModel.name);
        intent.putExtra(Constant.DETAIL, filmModel.detail);
        intent.putExtra(Constant.FORMAT, filmModel.format);
        intent.putExtra(Constant.ID, filmModel.id);
        intent.putExtra(Constant.DATE, filmModel.releaseDate);
        intent.putExtra(Constant.IMAGE, filmModel.image);
        intent.putExtra(Constant.IMAGE_COVER, filmModel.imageCover);
        intent.putExtra(Constant.TRAILER, filmModel.trailer);
        intent.putExtra(Constant.TIME, filmModel.time);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CastActivity.this, imageView, "sharedName");
        startActivity(intent, options.toBundle());
    }
}

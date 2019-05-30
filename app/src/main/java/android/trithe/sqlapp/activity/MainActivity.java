package android.trithe.sqlapp.activity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.HeaderAdapter;
import android.trithe.sqlapp.adapter.SlidePaperAdapter;
import android.trithe.sqlapp.callback.OnHeaderItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.model.Header;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataImageManager;
import android.trithe.sqlapp.rest.manager.GetDataKindDetailManager;
import android.trithe.sqlapp.rest.manager.GetDataKindManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.model.PosterModel;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataKindResponse;
import android.trithe.sqlapp.rest.response.GetDataPosterImageResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements OnHeaderItemClickListener {
    private ViewPager backdrop;
    private TabLayout indicator;
    private CircleImageView avatar;
    private List<PosterModel> slideList = new ArrayList<>();
    private List<Header> headerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HeaderAdapter adapter;
    private ImageView imgSearch;
    private ProgressDialog pDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        pDialog = new ProgressDialog(this);
        Glide.with(this).load(Config.LINK_LOAD_IMAGE + SharedPrefUtils.getString(Constant.KEY_USER_IMAGE, "")).into(avatar);
        slide();
        adapter = new HeaderAdapter(headerList);
        adapter.setOnClickItemPopularFilm(this);
        getFilm();
        setUpRecyclerView();
        imgSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, KindActivity.class);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, imgSearch, getResources().getString(R.string.shareName));
            startActivity(intent, options.toBundle());
        });
        avatar.setOnClickListener(v -> {
            SharedPrefUtils.putString(Constant.KEY_USER_ID, null);
            SharedPrefUtils.putString(Constant.KEY_USER_NAME, null);
            SharedPrefUtils.putString(Constant.KEY_USER_PASSWORD, null);
            SharedPrefUtils.putString(Constant.KEY_NAME_USER, null);
            SharedPrefUtils.putString(Constant.KEY_USER_IMAGE, null);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
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


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        backdrop = findViewById(R.id.backdrop);
        indicator = findViewById(R.id.indicator);
        avatar = findViewById(R.id.avatar);
        recyclerView = findViewById(R.id.recycler_view);
        imgSearch = findViewById(R.id.imgSearch);
    }


    private void slide() {
        slideList.clear();
        GetDataImageManager getDataImageManager = new GetDataImageManager(new ResponseCallbackListener<GetDataPosterImageResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataPosterImageResponse data) {
                if (data.status.equals("200")) {
                    slideList.addAll(data.result);
                    getTimerSlide();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataImageManager.startGetDataPoster();
    }

    private void getFilm() {
        headerList.clear();
        showProcessDialog();
        GetDataKindManager getDataKindManager = new GetDataKindManager(new ResponseCallbackListener<GetDataKindResponse>() {
            @Override
            public void onObjectComplete(String TAG, final GetDataKindResponse data) {
                if (data.status.equals("200")) {
                    for (int i = 0; i < data.result.size(); i++) {
                        final int finalI = i;
                        GetDataKindDetailManager getDataKindDetailManager = new GetDataKindDetailManager(new ResponseCallbackListener<GetDataFilmResponse>() {
                            @Override
                            public void onObjectComplete(String TAG, GetDataFilmResponse data1) {
                                List<FilmModel> filmModelList = new ArrayList<>(data1.result);
                                headerList.add(new Header(data.result.get(finalI).name, filmModelList));
                                adapter.notifyDataSetChanged();
                                disProcessDialog();
                            }

                            @Override
                            public void onResponseFailed(String TAG, String message) {
                                disProcessDialog();
                            }
                        });
                        getDataKindDetailManager.startGetDataKindDetail(data.result.get(i).id);
                    }
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataKindManager.startGetDataKind(null, Config.API_KIND);
    }

    private void getTimerSlide() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        indicator.setupWithViewPager(backdrop, true);
        SlidePaperAdapter paperAdapter = new SlidePaperAdapter(this, slideList);
        backdrop.setAdapter(paperAdapter);
    }

    @Override
    public void onFilm(final Header header) {
        GetDataKindManager getDataKindManager = new GetDataKindManager(new ResponseCallbackListener<GetDataKindResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataKindResponse data) {
                if (data.status.equals("200")) {
                    Intent intent = new Intent(MainActivity.this, DetailKindActivity.class);
                    for (int i = 0; i < data.result.size(); i++) {
                        if (header.getSectionLabel().equals(data.result.get(i).name)) {
                            intent.putExtra(Constant.ID, data.result.get(i).id);
                        }
                    }
                    startActivity(intent);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataKindManager.startGetDataKind(null, Config.API_KIND);
    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(() -> {
                if (backdrop.getCurrentItem() < slideList.size() - 1) {
                    backdrop.setCurrentItem(backdrop.getCurrentItem() + 1);
                } else
                    backdrop.setCurrentItem(0);
            });
        }
    }
}

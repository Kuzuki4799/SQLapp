package android.trithe.sqlapp.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.KindDetailAdapter;
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataKindDetailManager;
import android.trithe.sqlapp.rest.manager.GetDataKindManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataKindResponse;
import android.trithe.sqlapp.utils.EndlessRecyclerOnScrollListener;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailKindActivity extends AppCompatActivity implements OnFilmItemClickListener, View.OnClickListener {
    private String id;
    private ImageView imgCover, image, btnBack;
    private TextView txtTitle;
    private List<FilmModel> list = new ArrayList<>();
    private KindDetailAdapter adapter;
    private RecyclerView recyclerView;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private TextView txtName;
    private ImageView imgSearch;
    private ProgressBar progressBar;
    private int page = 0;
    private int per_page = 6;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kind);
        initView();
        id = getIntent().getStringExtra(Constant.ID);
        adapter = new KindDetailAdapter(list, this);
        getDataKind();
        setUpAppBar();
        linearLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecorationUtils(2, dpToPx(), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        resetLoadMore();
        listener();
    }

    private void listener() {
        btnBack.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        imgCover = findViewById(R.id.img_cover);
        image = findViewById(R.id.image);
        txtTitle = findViewById(R.id.txtTitle);
        btnBack = findViewById(R.id.btnBack);
        appbar = findViewById(R.id.appbar);
        toolbar = findViewById(R.id.toolbar);
        txtName = findViewById(R.id.txtName);
        imgSearch = findViewById(R.id.imgSearch);
    }

    private void setUpAppBar() {
        appbar.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener) (appBarLayout, i) -> {
            if (i == 0) {
                toolbar.setBackgroundResource(R.drawable.black_gradian_reverse);
                txtName.setVisibility(View.GONE);
            } else {
                toolbar.setBackgroundResource(R.color.colorPrimary);
                txtName.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getDataKind() {
        GetDataKindManager getDataKindManager = new GetDataKindManager(new ResponseCallbackListener<GetDataKindResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataKindResponse data) {
                if (data.status.equals("200")) {
                    txtTitle.setText(data.result.get(0).name);
                    txtName.setText(data.result.get(0).name);
                    Glide.with(DetailKindActivity.this).load(Config.LINK_LOAD_IMAGE + data.result.get(0).imageBg).into(imgCover);
                    Glide.with(DetailKindActivity.this).load(Config.LINK_LOAD_IMAGE + data.result.get(0).image).into(image);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataKindManager.startGetDataKind(id, Config.API_DATA_KIND);
    }

    private void resetLoadMore() {
        progressBar.setVisibility(View.VISIBLE);
        adapter.setOnLoadMore(true);
        list.clear();
        setUpAdapter();
        getDataFilm(page, per_page);
    }

    private void setUpAdapter() {
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                new Handler().postDelayed(() -> getDataFilm(page, per_page), 500);
            }
        });
    }

    private void getDataFilm(int page, int per_page) {
        GetDataKindDetailManager getDataKindDetailManager = new GetDataKindDetailManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    list.addAll(data.result);
                    adapter.notifyDataSetChanged();
                    if (data.result.size() < 6) {
                        adapter.setOnLoadMore(false);
                    }
                } else {
                    adapter.setOnLoadMore(false);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
            }
        });
        getDataKindDetailManager.startGetDataKindDetail(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""),
                id, page, per_page);
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.KEY_INTENT) {
                if (Objects.requireNonNull(data).getBooleanExtra(Constant.KEY_CHECK, false)) {
                    list.get(data.getIntExtra(Constant.POSITION, 0)).setSaved(0);
                } else {
                    list.get(data.getIntExtra(Constant.POSITION, 0)).setSaved(1);
                }
                adapter.notifyItemChanged(data.getIntExtra(Constant.POSITION, 0));
            }
        }
    }

    @Override
    public void onCheckItemFilm(int position, CardView cardView) {
        Intent intent = new Intent(this, DetailFilmActivity.class);
        intent.putExtra(Constant.ID, list.get(position).id);
        intent.putExtra(Constant.POSITION, position);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, cardView, getResources().getString(R.string.app_name));
            startActivityForResult(intent, Constant.KEY_INTENT, options.toBundle());
        } else {
            startActivityForResult(intent, Constant.KEY_INTENT);
        }
    }

    @Override
    public void changSetDataFilm(int position, String key) {
        if (key.equals(Config.API_DELETE_SAVED)) {
            list.get(position).setSaved(0);
        } else {
            list.get(position).setSaved(1);
        }
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.imgSearch:
                startActivity(new Intent(DetailKindActivity.this, SearchActivity.class));
                break;
        }
    }
}

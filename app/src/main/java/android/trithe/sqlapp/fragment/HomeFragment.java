package android.trithe.sqlapp.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.DetailKindActivity;
import android.trithe.sqlapp.adapter.FilmAdapter;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.manager.GetDataKindDetailManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.model.PosterModel;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.utils.EndlessRecyclerOnScrollListener;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.widget.CustomSliderView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private List<PosterModel> slideList = new ArrayList<>();
    private SliderLayout slider;
    private SwipeRefreshLayout swRecyclerViewHome;
    private TextView btnMoreTheatre;
    private RecyclerView recyclerViewTheatre;
    private TextView btnMoreAdventure;
    private RecyclerView recyclerViewAdventure;
    private TextView btnMoreAction;
    private RecyclerView recyclerViewAction;
    private TextView btnMoreFiction;
    private RecyclerView recyclerViewFiction;
    private TextView btnMoreTv;
    private RecyclerView recyclerViewTv;
    private FilmAdapter adapterTheatre;
    private FilmAdapter adapterAction;
    private FilmAdapter adapterAdventure;
    private FilmAdapter adapterFiction;
    private FilmAdapter adapterTv;
    private ProgressBar progressBarTheatre;
    private ProgressBar progressBarAdventure;
    private ProgressBar progressBarAction;
    private ProgressBar progressBarFiction;
    private ProgressBar progressBarTv;
    private List<FilmModel> listTheatre = new ArrayList<>();
    private List<FilmModel> listAction = new ArrayList<>();
    private List<FilmModel> listAdventure = new ArrayList<>();
    private List<FilmModel> listFiction = new ArrayList<>();
    private List<FilmModel> listTv = new ArrayList<>();
    private int page = 0;
    private int per_page = 6;
    private LinearLayoutManager linearLayoutManager;
    private NativeExpressAdView nativeExpress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        setUpSlider();
        slide();
        adapterAction = new FilmAdapter(listAction, 0);
        adapterAdventure = new FilmAdapter(listAdventure, 0);
        adapterFiction = new FilmAdapter(listFiction, 0);
        adapterTheatre = new FilmAdapter(listTheatre, 0);
        adapterTv = new FilmAdapter(listTv, 0);
        AdRequest adRequest = new AdRequest.Builder().build();
        nativeExpress.loadAd(adRequest);
        swRecyclerViewHome.setOnRefreshListener(this::resetLoadMore);
        listener();
        return view;
    }

    private void listener() {
        btnMoreAction.setOnClickListener(this);
        btnMoreAdventure.setOnClickListener(this);
        btnMoreFiction.setOnClickListener(this);
        btnMoreTheatre.setOnClickListener(this);
        btnMoreTv.setOnClickListener(this);
    }

    private void resetLoadMore() {
        listTheatre.clear();
        listAction.clear();
        listAdventure.clear();
        listFiction.clear();
        listTv.clear();
        adapterTv.setOnLoadMore(true);
        adapterTheatre.setOnLoadMore(true);
        adapterFiction.setOnLoadMore(true);
        adapterAdventure.setOnLoadMore(true);
        adapterAction.setOnLoadMore(true);
        progressBarAction.setVisibility(View.VISIBLE);
        progressBarAdventure.setVisibility(View.VISIBLE);
        progressBarFiction.setVisibility(View.VISIBLE);
        progressBarTheatre.setVisibility(View.VISIBLE);
        progressBarTv.setVisibility(View.VISIBLE);
        setUpRecyclerView(listAction, "1", adapterAction, progressBarAction, recyclerViewAction);
        setUpRecyclerView(listAdventure, "8", adapterAdventure, progressBarAdventure, recyclerViewAdventure);
        setUpRecyclerView(listFiction, "5", adapterFiction, progressBarAction, recyclerViewFiction);
        setUpRecyclerView(listTheatre, "9", adapterTheatre, progressBarTheatre, recyclerViewTheatre);
        setUpRecyclerView(listTv, "10", adapterTv, progressBarTv, recyclerViewTv);
        getDataFilm(listAction, "1", adapterAction, progressBarAction, page, per_page);
        getDataFilm(listAdventure, "8", adapterAdventure, progressBarAdventure, page, per_page);
        getDataFilm(listFiction, "5", adapterFiction, progressBarFiction, page, per_page);
        getDataFilm(listTheatre, "9", adapterTheatre, progressBarTheatre, page, per_page);
        getDataFilm(listTv, "10", adapterTv, progressBarTv, page, per_page);
    }

    @Override
    public void onResume() {
        super.onResume();
        resetLoadMore();
    }

    private void setUpSlider() {
        slider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setDuration(TimeUnit.SECONDS.toMillis(5));
    }

    private void getDataFilm(List<FilmModel> list, String id, FilmAdapter adapter, ProgressBar progressBar, int page, int per_page) {
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
                swRecyclerViewHome.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                swRecyclerViewHome.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        });
        getDataKindDetailManager.startGetDataKindDetail(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""),
                id, page, per_page);
    }

    private void setUpRecyclerView(List<FilmModel> list, String id, FilmAdapter adapter, ProgressBar progressBar, RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                new Handler().postDelayed(() -> {
                    getDataFilm(list, id, adapter, progressBar, page, per_page);
                    Log.d("abc", page + "");
                }, 500);
            }
        });
    }

    private void initView(View view) {
        slider = view.findViewById(R.id.slider);
        nativeExpress = view.findViewById(R.id.nativeExpress);
        swRecyclerViewHome = view.findViewById(R.id.swRecyclerViewHome);
        progressBarTheatre = view.findViewById(R.id.progress_bar_Theatre);
        progressBarAdventure = view.findViewById(R.id.progress_bar_Adventure);
        progressBarAction = view.findViewById(R.id.progress_bar_Action);
        progressBarFiction = view.findViewById(R.id.progress_bar_Fiction);
        progressBarTv = view.findViewById(R.id.progress_bar_Tv);
        btnMoreTheatre = view.findViewById(R.id.btn_more_theatre);
        recyclerViewTheatre = view.findViewById(R.id.recyclerViewTheatre);
        btnMoreAdventure = view.findViewById(R.id.btn_more_adventure);
        recyclerViewAdventure = view.findViewById(R.id.recyclerViewAdventure);
        btnMoreAction = view.findViewById(R.id.btn_more_action);
        recyclerViewAction = view.findViewById(R.id.recyclerViewAction);
        btnMoreFiction = view.findViewById(R.id.btn_more_fiction);
        recyclerViewFiction = view.findViewById(R.id.recyclerViewFiction);
        btnMoreTv = view.findViewById(R.id.btn_more_tv);
        recyclerViewTv = view.findViewById(R.id.recyclerViewTv);
    }

    private void slide() {
        slideList.clear();
        slider.removeAllSliders();
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    for (int i = 0; i < 10; i++) {
                        slideList.add(new PosterModel(data.result.get(i).id, data.result.get(i).name, data.result.get(i).imageCover));
                        CustomSliderView textSliderView = new CustomSliderView(getContext());
                        textSliderView
                                .description(slideList.get(i).title)
                                .image(Config.LINK_LOAD_IMAGE + slideList.get(i).image)
                                .setScaleType(BaseSliderView.ScaleType.CenterCrop);
                        slider.addSlider(textSliderView);
                    }
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataFilmManager.startGetDataFilm(3, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, 0, 10, Config.API_FILM);
    }

    private void intentDetailKindFilm(String id) {
        Intent intent = new Intent(getActivity(), DetailKindActivity.class);
        intent.putExtra(Constant.ID, id);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_more_action:
                intentDetailKindFilm("1");
                break;
            case R.id.btn_more_adventure:
                intentDetailKindFilm("8");
                break;
            case R.id.btn_more_fiction:
                intentDetailKindFilm("5");
                break;
            case R.id.btn_more_theatre:
                intentDetailKindFilm("9");
                break;
            case R.id.btn_more_tv:
                intentDetailKindFilm("10");
                break;
        }
    }
}
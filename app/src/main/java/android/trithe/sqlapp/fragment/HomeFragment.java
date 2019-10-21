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
import android.trithe.sqlapp.widget.CustomeRecyclerView;
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
    private SliderLayout slider;
    private SwipeRefreshLayout swRecyclerViewHome;
    private TextView btnMoreTheatre, btnMoreAdventure, btnMoreAction, btnMoreFiction, btnMoreTv;
    private CustomeRecyclerView recyclerViewTheatre, recyclerViewAdventure, recyclerViewAction, recyclerViewFiction, recyclerViewTv;
    private FilmAdapter adapterTheatre, adapterAction, adapterAdventure, adapterFiction, adapterTv;
    private ProgressBar progressBarTheatre, progressBarAdventure, progressBarAction, progressBarFiction, progressBarTv;
    private List<FilmModel> listTheatre = new ArrayList<>();
    private List<FilmModel> listAction = new ArrayList<>();
    private List<FilmModel> listAdventure = new ArrayList<>();
    private List<FilmModel> listFiction = new ArrayList<>();
    private List<FilmModel> listTv = new ArrayList<>();
    private List<PosterModel> slideList = new ArrayList<>();
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
        initAdapter();
        AdRequest adRequest = new AdRequest.Builder().build();
        nativeExpress.loadAd(adRequest);
        swRecyclerViewHome.setOnRefreshListener(this::resetLoadMore);
        listener();
        resetLoadMore();
        return view;
    }

    private void initAdapter() {
        adapterAction = new FilmAdapter(listAction, 0);
        adapterAdventure = new FilmAdapter(listAdventure, 0);
        adapterFiction = new FilmAdapter(listFiction, 0);
        adapterTheatre = new FilmAdapter(listTheatre, 0);
        adapterTv = new FilmAdapter(listTv, 0);
    }

    private void listener() {
        btnMoreAction.setOnClickListener(this);
        btnMoreAdventure.setOnClickListener(this);
        btnMoreFiction.setOnClickListener(this);
        btnMoreTheatre.setOnClickListener(this);
        btnMoreTv.setOnClickListener(this);
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

    private void resetLoadMore() {
        slide();
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
        setUpRecyclerView(listAction, Constant.ID_FILM_ACTION, adapterAction, progressBarAction, recyclerViewAction);
        setUpRecyclerView(listAdventure, Constant.ID_FILM_ADVENTURE, adapterAdventure, progressBarAdventure, recyclerViewAdventure);
        setUpRecyclerView(listFiction, Constant.ID_FILM_FICTION, adapterFiction, progressBarAction, recyclerViewFiction);
        setUpRecyclerView(listTheatre, Constant.ID_FILM_THEATRE, adapterTheatre, progressBarTheatre, recyclerViewTheatre);
        setUpRecyclerView(listTv, Constant.ID_FILM_TV, adapterTv, progressBarTv, recyclerViewTv);
        getDataFilm(listAction, Constant.ID_FILM_ACTION, adapterAction, progressBarAction, page, per_page);
        getDataFilm(listAdventure, Constant.ID_FILM_ADVENTURE, adapterAdventure, progressBarAdventure, page, per_page);
        getDataFilm(listFiction, Constant.ID_FILM_FICTION, adapterFiction, progressBarFiction, page, per_page);
        getDataFilm(listTheatre, Constant.ID_FILM_THEATRE, adapterTheatre, progressBarTheatre, page, per_page);
        getDataFilm(listTv, Constant.ID_FILM_TV, adapterTv, progressBarTv, page, per_page);
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
                new Handler().postDelayed(() -> getDataFilm(list, id, adapter, progressBar, page, per_page), 500);
            }
        });
    }

    private void slide() {
        slideList.clear();
        slider.removeAllSliders();
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    for (int i = 0; i < 15; i++) {
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
        getDataFilmManager.startGetDataFilm(3, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, 0, 15, Config.API_FILM);
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
                intentDetailKindFilm(Constant.ID_FILM_ACTION);
                break;
            case R.id.btn_more_adventure:
                intentDetailKindFilm(Constant.ID_FILM_ADVENTURE);
                break;
            case R.id.btn_more_fiction:
                intentDetailKindFilm(Constant.ID_FILM_FICTION);
                break;
            case R.id.btn_more_theatre:
                intentDetailKindFilm(Constant.ID_FILM_THEATRE);
                break;
            case R.id.btn_more_tv:
                intentDetailKindFilm(Constant.ID_FILM_TV);
                break;
        }
    }
}
package android.trithe.sqlapp.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.HeaderAdapter;
import android.trithe.sqlapp.callback.OnHeaderItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.model.Header;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.manager.GetDataKindDetailManager;
import android.trithe.sqlapp.rest.manager.GetDataKindManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.model.PosterModel;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataKindResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.widget.CustomSliderView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {
    private List<PosterModel> slideList = new ArrayList<>();
    private SliderLayout slider;
    private List<Header> headerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HeaderAdapter adapter;
    private SwipeRefreshLayout swRecyclerViewHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        adapter = new HeaderAdapter(headerList);
        adapter.setOnClickItemPopularFilm((OnHeaderItemClickListener) getContext());
        slide();
        getFilm();
        setUpRecyclerView();
        setUpSlider();
        swRecyclerViewHome.setOnRefreshListener(this::getFilm);
        return view;
    }

    private void setUpSlider() {
        slider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setDuration(TimeUnit.SECONDS.toMillis(5));
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        slider = view.findViewById(R.id.slider);
        swRecyclerViewHome = view.findViewById(R.id.swRecyclerViewHome);
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

    private void getFilm() {
        headerList.clear();
        swRecyclerViewHome.setRefreshing(true);
        GetDataKindManager getDataKindManager = new GetDataKindManager(new ResponseCallbackListener<GetDataKindResponse>() {
            @Override
            public void onObjectComplete(String TAG, final GetDataKindResponse data) {
                if (data.status.equals("200")) {
                    for (int i = 0; i < 5; i++) {
                        final int finalI = i;
                        GetDataKindDetailManager getDataKindDetailManager = new GetDataKindDetailManager(new ResponseCallbackListener<GetDataFilmResponse>() {
                            @Override
                            public void onObjectComplete(String TAG, GetDataFilmResponse data1) {
                                List<FilmModel> filmModelList = new ArrayList<>(data1.result);
                                headerList.add(new Header(data.result.get(finalI).name, filmModelList));
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onResponseFailed(String TAG, String message) {
                                swRecyclerViewHome.setRefreshing(false);
                            }
                        });
                        getDataKindDetailManager.startGetDataKindDetail(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""),
                                data.result.get(i).id, 0, 20);
                    }
                }
                swRecyclerViewHome.setRefreshing(false);
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                swRecyclerViewHome.setRefreshing(false);
            }
        });
        getDataKindManager.startGetDataKind(null, Config.API_KIND);
    }
}
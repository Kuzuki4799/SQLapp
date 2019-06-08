package android.trithe.sqlapp.fragment;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.manager.GetDataKindDetailManager;
import android.trithe.sqlapp.rest.manager.GetDataKindManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.model.PosterModel;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataKindResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {
    private ViewPager backdrop;
    private TabLayout indicator;
    private List<PosterModel> slideList = new ArrayList<>();
    private List<Header> headerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HeaderAdapter adapter;
    private ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        pDialog = new ProgressDialog(getContext());
        slide();
        adapter = new HeaderAdapter(headerList);
        adapter.setOnClickItemPopularFilm((OnHeaderItemClickListener) getContext());
        getFilm();
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initView(View view) {
        backdrop = view.findViewById(R.id.backdrop);
        indicator = view.findViewById(R.id.indicator);
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    private void slide() {
        slideList.clear();
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    for (int i = 0; i < data.result.size(); i++) {
                        slideList.add(new PosterModel(data.result.get(i).id, data.result.get(i).name, data.result.get(i).imageCover));
                    }
                    getTimerSlide();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataFilmManager.startGetDataFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_FILM);
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

    private void getFilm() {
        headerList.clear();
        showProcessDialog();
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
                                disProcessDialog();
                            }

                            @Override
                            public void onResponseFailed(String TAG, String message) {
                                disProcessDialog();
                            }
                        });
                        getDataKindDetailManager.startGetDataKindDetail(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), data.result.get(i).id);
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
        SlidePaperAdapter paperAdapter = new SlidePaperAdapter(getContext(), slideList);
        backdrop.setAdapter(paperAdapter);
    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            try {
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                    if (backdrop.getCurrentItem() < slideList.size() - 1) {
                        backdrop.setCurrentItem(backdrop.getCurrentItem() + 1);
                    } else
                        backdrop.setCurrentItem(0);
                });
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
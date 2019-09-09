package android.trithe.sqlapp.fragment;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.KindDetailAdapter;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.widget.PullToRefresh.MyPullToRefresh;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {
    private List<FilmModel> listFilm = new ArrayList<>();
    private KindDetailAdapter detailAdapter;
    private RecyclerView recyclerView;
    private TextView txtNoData;
    private MyPullToRefresh swRecyclerViewSaved;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_saved, container, false);
        initView(view);
        detailAdapter = new KindDetailAdapter(listFilm, this::getDataKind);
        setUpAdapter();
        swRecyclerViewSaved.setOnRefreshBegin(recyclerView,
                new MyPullToRefresh.PullToRefreshHeader(getActivity()), this::getDataKind);
        return view;
    }

    private void setUpAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecorationUtils(2, dpToPx(), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(detailAdapter);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        swRecyclerViewSaved = view.findViewById(R.id.swRecyclerViewSaved);
        txtNoData = view.findViewById(R.id.txtNoData);
    }

    private void getDataKind() {
        listFilm.clear();
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    listFilm.addAll(data.result);
                    detailAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(detailAdapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                }
                swRecyclerViewSaved.refreshComplete();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
            }
        });
        getDataFilmManager.startGetDataFilm(0, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_GET_FILM_SAVED);
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataKind();
    }
}

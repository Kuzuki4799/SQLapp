package android.trithe.sqlapp.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.KindFilmAdapter;
import android.trithe.sqlapp.callback.OnKindItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataKindManager;
import android.trithe.sqlapp.rest.model.KindModel;
import android.trithe.sqlapp.rest.response.GetDataKindResponse;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
import android.trithe.sqlapp.utils.Utils;
import android.trithe.sqlapp.widget.PullToRefresh.MyPullToRefresh;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KindFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<KindModel> list = new ArrayList<>();
    private KindFilmAdapter adapter;
    private MyPullToRefresh swRefreshRecyclerView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_kind, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        swRefreshRecyclerView = view.findViewById(R.id.swRefreshRecyclerView);
        adapter = new KindFilmAdapter(list);
        adapter.setOnItemClickListener((OnKindItemClickListener) getContext());
        setUpAdapter();
        getDataKind();
        swRefreshRecyclerView.setOnRefreshBegin(recyclerView,
                new MyPullToRefresh.PullToRefreshHeader(getActivity()), this::getDataKind);
        return view;
    }

    private void setUpAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecorationUtils(2, Utils.dpToPx(Objects.requireNonNull(getActivity()), 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void getDataKind() {
        list.clear();
        new GetDataKindManager(new ResponseCallbackListener<GetDataKindResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataKindResponse data) {
                if (data.status.equals("200")) {
                    list.addAll(data.result);
                    recyclerView.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
                swRefreshRecyclerView.refreshComplete();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                progressBar.setVisibility(View.GONE);
            }
        }).startGetDataKind(null, Config.API_KIND);
    }
}

package android.trithe.sqlapp.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.CastActivity;
import android.trithe.sqlapp.adapter.FavoriteAdapter;
import android.trithe.sqlapp.callback.OnRemoveItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetAllDataCastManager;
import android.trithe.sqlapp.rest.model.CastModel;
import android.trithe.sqlapp.rest.response.GetAllDataCastResponse;
import android.trithe.sqlapp.utils.EndlessRecyclerOnScrollListener;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.utils.Utils;
import android.trithe.sqlapp.widget.PullToRefresh.MyPullToRefresh;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class FavoriteFragment extends Fragment {
    private TextView txtNoData;
    private RecyclerView recyclerView;
    private List<CastModel> listCast = new ArrayList<>();
    private FavoriteAdapter favoriteAdapter;
    private MyPullToRefresh swRecyclerViewFavorite;
    private int page = 0;
    private int per_page = 6;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        initView(view);
        initAdapter();
        linearLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(
                new GridSpacingItemDecorationUtils(2, Utils.dpToPx(Objects.requireNonNull(getActivity()), 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        resetLoadMore();
        swRecyclerViewFavorite.setOnRefreshBegin(recyclerView,
                new MyPullToRefresh.PullToRefreshHeader(getActivity()), this::resetLoadMore);
        return view;
    }

    private void initAdapter() {
        favoriteAdapter = new FavoriteAdapter(listCast, new OnRemoveItemClickListener() {
            @Override
            public void onCheckItem(int position, CardView cardView) {
                Intent intent = new Intent(getContext(), CastActivity.class);
                intent.putExtra(Constant.ID, listCast.get(position).id);
                intent.putExtra(Constant.POSITION, position);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), cardView,
                            getResources().getString(R.string.app_name));
                    startActivityForResult(intent, Constant.KEY_INTENT, options.toBundle());
                } else {
                    startActivityForResult(intent, Constant.KEY_INTENT);
                }
            }

            @Override
            public void onRemoveItem(int position) {
                listCast.remove(position);
                favoriteAdapter.notifyDataSetChanged();
                if (listCast.size() == 0) {
                    txtNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void resetLoadMore() {
        progressBar.setVisibility(View.VISIBLE);
        favoriteAdapter.setOnLoadMore(true);
        setUpAdapter();
        listCast.clear();
        getAllDataCast(page, per_page);
    }

    private void setUpAdapter() {
        recyclerView.setAdapter(favoriteAdapter);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                new Handler().postDelayed(() ->
                        getAllDataCast(page, per_page), 500);
            }
        });
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        txtNoData = view.findViewById(R.id.txtNoData);
        progressBar = view.findViewById(R.id.progress_bar);
        swRecyclerViewFavorite = view.findViewById(R.id.swRecyclerViewFavorite);
    }

    private void getAllDataCast(int page, int per_page) {
        new GetAllDataCastManager(new ResponseCallbackListener<GetAllDataCastResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataCastResponse data) {
                if (data.status.equals("200")) {
                    listCast.addAll(data.result);
                    if (data.result.size() < 6) {
                        favoriteAdapter.setOnLoadMore(false);
                    }
                } else if (data.status.equals("400")) {
                    recyclerView.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                } else {
                    favoriteAdapter.setOnLoadMore(false);
                }
                favoriteAdapter.notifyDataSetChanged();
                swRecyclerViewFavorite.refreshComplete();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                progressBar.setVisibility(View.GONE);
            }
        }).startGetDataCast(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, page, per_page,
                Config.API_GET_ALL_CAST_BY_LOVED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.KEY_INTENT) {
                if (Objects.requireNonNull(data).getBooleanExtra(Constant.KEY_CHECK, false)) {
                    listCast.remove(data.getIntExtra(Constant.POSITION, 0));
                    favoriteAdapter.notifyDataSetChanged();
                    if (listCast.size() == 0) {
                        txtNoData.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
}

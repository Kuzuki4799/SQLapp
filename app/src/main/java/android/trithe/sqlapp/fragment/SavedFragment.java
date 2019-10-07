package android.trithe.sqlapp.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.DetailFilmActivity;
import android.trithe.sqlapp.adapter.BookMarkAdapter;
import android.trithe.sqlapp.callback.OnRemoveItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.utils.EndlessRecyclerOnScrollListener;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.widget.PullToRefresh.MyPullToRefresh;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class SavedFragment extends Fragment {
    private List<FilmModel> listFilm = new ArrayList<>();
    private BookMarkAdapter detailAdapter;
    private RecyclerView recyclerView;
    private TextView txtNoData;
    private MyPullToRefresh swRecyclerViewSaved;
    private int page = 0;
    private int per_page = 6;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_saved, container, false);
        initView(view);
        initAdapter();
        linearLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecorationUtils(2, dpToPx(), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        resetLoadMore();
        swRecyclerViewSaved.setOnRefreshBegin(recyclerView,
                new MyPullToRefresh.PullToRefreshHeader(getActivity()), this::resetLoadMore);
        return view;
    }

    private void initAdapter() {
        detailAdapter = new BookMarkAdapter(listFilm, new OnRemoveItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckItem(int position, CardView cardView) {
                Intent intent = new Intent(getContext(), DetailFilmActivity.class);
                intent.putExtra(Constant.ID, listFilm.get(position).id);
                intent.putExtra(Constant.POSITION, position);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), cardView,
                        getResources().getString(R.string.app_name));
                startActivityForResult(intent, Constant.KEY_INTENT, options.toBundle());
            }

            @Override
            public void onRemoveItem(int position) {
                listFilm.remove(position);
                detailAdapter.notifyDataSetChanged();
                if (listFilm.size() == 0) {
                    txtNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void resetLoadMore() {
        progressBar.setVisibility(View.VISIBLE);
        detailAdapter.setOnLoadMore(true);
        setUpAdapter();
        listFilm.clear();
        getDataKind(page, per_page);
    }

    private void setUpAdapter() {
        recyclerView.setAdapter(detailAdapter);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                new Handler().postDelayed(() -> getDataKind(page, per_page), 500);
            }
        });
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        swRecyclerViewSaved = view.findViewById(R.id.swRecyclerViewSaved);
        txtNoData = view.findViewById(R.id.txtNoData);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    private void getDataKind(int page, int per_page) {
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    listFilm.addAll(data.result);
                    detailAdapter.notifyDataSetChanged();
                    if (data.result.size() < 6) {
                        detailAdapter.setOnLoadMore(false);
                    }
                } else if (data.status.equals("400")) {
                    recyclerView.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                } else {
                    detailAdapter.setOnLoadMore(false);
                }
                progressBar.setVisibility(View.GONE);
                swRecyclerViewSaved.refreshComplete();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                progressBar.setVisibility(View.GONE);
            }
        });
        getDataFilmManager.startGetDataFilm(0, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null,
                page, per_page, Config.API_GET_FILM_SAVED);
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.KEY_INTENT) {
                if (Objects.requireNonNull(data).getBooleanExtra(Constant.KEY_CHECK, false)) {
                    listFilm.remove(data.getIntExtra(Constant.POSITION, 0));
                    detailAdapter.notifyDataSetChanged();
                    if (listFilm.size() == 0) {
                        txtNoData.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
}

package android.trithe.sqlapp.fragment;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.CastAdapter;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetAllDataCastManager;
import android.trithe.sqlapp.rest.model.CastDetailModel;
import android.trithe.sqlapp.rest.response.GetAllDataCastResponse;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private TextView txtNoData;
    private RecyclerView recyclerView;
    private List<CastDetailModel> listCast = new ArrayList<>();
    private CastAdapter castAdapter;
    private SwipeRefreshLayout swRecyclerViewFavorite;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        initView(view);
        castAdapter = new CastAdapter(listCast, this::getAllDataCast);
        setUpAdapter();
        swRecyclerViewFavorite.setOnRefreshListener(this::getAllDataCast);
        return view;
    }

    private void setUpAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecorationUtils(2, dpToPx(), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(castAdapter);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        txtNoData = view.findViewById(R.id.txtNoData);
        swRecyclerViewFavorite = view.findViewById(R.id.swRecyclerViewFavorite);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllDataCast();
    }

    private void getAllDataCast() {
        listCast.clear();
        swRecyclerViewFavorite.setRefreshing(true);
        GetAllDataCastManager getAllDataCastManager = new GetAllDataCastManager(new ResponseCallbackListener<GetAllDataCastResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataCastResponse data) {
                if (data.status.equals("200")) {
                    listCast.addAll(data.result);
                    castAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(castAdapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                }
                swRecyclerViewFavorite.setRefreshing(false);
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
            }
        });
        getAllDataCastManager.startGetDataCast(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_GET_ALL_CAST_BY_LOVED);
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
    }
}

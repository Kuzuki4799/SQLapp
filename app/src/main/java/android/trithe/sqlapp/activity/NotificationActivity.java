package android.trithe.sqlapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.NotificationAdapter;
import android.trithe.sqlapp.callback.OnNotificationItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataNotificationManager;
import android.trithe.sqlapp.rest.model.NotificationModel;
import android.trithe.sqlapp.rest.response.GetNotificationResponse;
import android.trithe.sqlapp.utils.EndlessRecyclerOnScrollListener;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.widget.PullToRefresh.MyPullToRefresh;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements OnNotificationItemClickListener {
    private List<NotificationModel> list = new ArrayList<>();
    private RecyclerView recyclerViewNotification;
    private NotificationAdapter adapter;
    private MyPullToRefresh swRecyclerViewNotification;
    private TextView txtNoData;
    private ImageView btnBack;
    private int page = 0;
    private int per_page = 7;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        adapter = new NotificationAdapter(list, this);
        initView();
        setUpAdapter();
        swRecyclerViewNotification.setOnRefreshBegin(recyclerViewNotification,
                new MyPullToRefresh.PullToRefreshHeader(NotificationActivity.this), () -> {
                    adapter.setOnLoadMore(true);
                    setUpAdapter();
                    list.clear();
                    getDataNotification(page, per_page);
                });
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpAdapter();
        list.clear();
        getDataNotification(page, per_page);
    }

    private void setUpAdapter() {
        recyclerViewNotification.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerViewNotification.setLayoutManager(linearLayoutManager);
        recyclerViewNotification.setAdapter(adapter);
        recyclerViewNotification.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                new Handler().postDelayed(() -> {
                    getDataNotification(page, per_page);
                    Log.d("abc", page + "");
                }, 500);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NotificationActivity.this, MainActivity.class));
        finish();
    }

    private void initView() {
        recyclerViewNotification = findViewById(R.id.recycler_view_notification);
        swRecyclerViewNotification = findViewById(R.id.swRecyclerViewNotification);
        txtNoData = findViewById(R.id.txtNoData);
        btnBack = findViewById(R.id.btnBack);
    }

    private void getDataNotification(int page, int per_page) {
        GetDataNotificationManager getDataNotificationManager = new GetDataNotificationManager(new ResponseCallbackListener<GetNotificationResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetNotificationResponse data) {
                if (data.status.equals("200")) {
                    list.addAll(data.result);
                    adapter.notifyDataSetChanged();
                    txtNoData.setVisibility(View.GONE);
                } else if (data.status.equals("400")) {
                    txtNoData.setVisibility(View.VISIBLE);
                } else {
                    adapter.setOnLoadMore(false);
                }
                swRecyclerViewNotification.refreshComplete();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                adapter.setOnLoadMore(false);
            }
        });
        getDataNotificationManager.getDataNotification(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""),
                Config.API_NOTIFICATION, page, per_page);
    }

    @Override
    public void onClickItem(NotificationModel dataModel) {
        Intent intent = new Intent(NotificationActivity.this, DetailFilmActivity.class);
        intent.putExtra(Constant.ID, dataModel.id);
        intent.putExtra(Constant.NOTIFICATION, dataModel.id);
        startActivity(intent);
    }
}

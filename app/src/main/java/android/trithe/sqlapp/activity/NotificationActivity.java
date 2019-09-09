package android.trithe.sqlapp.activity;

import android.content.Intent;
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
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.widget.PullToRefresh.MyPullToRefresh;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        adapter = new NotificationAdapter(list, this);
        initView();
        setUpAdapter();
        swRecyclerViewNotification.setOnRefreshBegin(recyclerViewNotification,
                new MyPullToRefresh.PullToRefreshHeader(NotificationActivity.this), this::getDataNotification);
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataNotification();
    }

    private void setUpAdapter() {
        recyclerViewNotification.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerViewNotification.setLayoutManager(linearLayoutManager);
        recyclerViewNotification.setAdapter(adapter);
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

    private void getDataNotification() {
        list.clear();
        GetDataNotificationManager getDataNotificationManager = new GetDataNotificationManager(new ResponseCallbackListener<GetNotificationResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetNotificationResponse data) {
                if (data.status.equals("200")) {
                    list.addAll(data.result);
                    adapter.notifyDataSetChanged();
                } else {
                    txtNoData.setVisibility(View.VISIBLE);
                }
                swRecyclerViewNotification.refreshComplete();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataNotificationManager.getDataNotification(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), Config.API_NOTIFICATION);
    }

    @Override
    public void onClickItem(NotificationModel dataModel) {
        getDataNotification();
        Intent intent = new Intent(NotificationActivity.this, DetailFilmActivity.class);
        intent.putExtra(Constant.ID, dataModel.id);
        intent.putExtra(Constant.NOTIFICATION, dataModel.id);
        startActivity(intent);
    }
}

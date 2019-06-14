package android.trithe.sqlapp.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
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
import android.trithe.sqlapp.rest.manager.CheckSeenNotificationManager;
import android.trithe.sqlapp.rest.manager.GetDataNotificationManager;
import android.trithe.sqlapp.rest.model.NotificationModel;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetNotificationResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements OnNotificationItemClickListener {
    private ProgressDialog pDialog;
    private List<NotificationModel> list = new ArrayList<>();
    private RecyclerView recyclerViewNotification;
    private NotificationAdapter adapter;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        pDialog = new ProgressDialog(this);
        adapter = new NotificationAdapter(list, this);
        initView();
        setUpAdapter();
        getDataNotification();
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void setUpAdapter() {
        recyclerViewNotification.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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
        btnBack = findViewById(R.id.btnBack);
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

    private void getDataNotification() {
        list.clear();
        showProcessDialog();
        GetDataNotificationManager getDataNotificationManager = new GetDataNotificationManager(new ResponseCallbackListener<GetNotificationResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetNotificationResponse data) {
                if (data.status.equals("200")) {
                    list.addAll(data.result);
                    adapter.notifyDataSetChanged();
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataNotificationManager.getDataNotification(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), Config.API_NOTIFICATION);
    }

    @Override
    public void onClickItem(NotificationModel dataModel, String key) {
        CheckSeenNotificationManager checkSeenNotificationManager = new CheckSeenNotificationManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    getDataNotification();
                    Intent intent = new Intent(NotificationActivity.this, DetailFilmActivity.class);
                    intent.putExtra(Constant.ID, dataModel.id);
                    startActivity(intent);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        checkSeenNotificationManager.getDataNotification(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), dataModel.id, key);
    }
}

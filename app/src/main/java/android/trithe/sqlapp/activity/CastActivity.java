package android.trithe.sqlapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataCastDetailManager;
import android.trithe.sqlapp.rest.manager.GetDataJobManager;
import android.trithe.sqlapp.rest.manager.GetDataKindDetailManager;
import android.trithe.sqlapp.rest.response.GetDataCastDetailResponse;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataJobResponse;
import android.widget.Toast;

public class CastActivity extends AppCompatActivity {
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);
        id = getIntent().getStringExtra("id");
        getDataCast();
        getJobCast();
    }

    private void getDataCast() {
        GetDataCastDetailManager getDataCastDetailManager = new GetDataCastDetailManager(new ResponseCallbackListener<GetDataCastDetailResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataCastDetailResponse data) {
                if (data.status.equals("200")) {
                    Toast.makeText(CastActivity.this, data.result.name, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataCastDetailManager.startGetDataCast(id);
    }

    private void getJobCast() {
        GetDataJobManager getDataJobManager = new GetDataJobManager(new ResponseCallbackListener<GetDataJobResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataJobResponse data) {
                if (data.status.equals("200")) {
                    Toast.makeText(CastActivity.this, data.result.get(0).name, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataJobManager.startGetJobData(id);
    }
}

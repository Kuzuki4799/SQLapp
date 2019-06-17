package android.trithe.sqlapp.activity;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.KindDetailAdapter;
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataKindDetailManager;
import android.trithe.sqlapp.rest.manager.GetDataKindManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataKindResponse;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class DetailKindActivity extends AppCompatActivity implements OnFilmItemClickListener {
    private String id;
    private ImageView imgCover, image, btnBack;
    private TextView txtTitle;
    private List<FilmModel> list = new ArrayList<>();
    private KindDetailAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kind);
        pDialog = new ProgressDialog(this);
        initView();
        id = getIntent().getStringExtra(Constant.ID);
        adapter = new KindDetailAdapter(list, this);
        getDataKind();
        getDataFilm();
        setUpAdapter();
        btnBack.setOnClickListener(v -> onBackPressed());
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

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        imgCover = findViewById(R.id.img_cover);
        image = findViewById(R.id.image);
        txtTitle = findViewById(R.id.txtTitle);
        btnBack = findViewById(R.id.btnBack);
    }

    private void getDataKind() {
        GetDataKindManager getDataKindManager = new GetDataKindManager(new ResponseCallbackListener<GetDataKindResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataKindResponse data) {
                if (data.status.equals("200")) {
                    txtTitle.setText(data.result.get(0).name);
                    Glide.with(DetailKindActivity.this).load(Config.LINK_LOAD_IMAGE + data.result.get(0).imageBg).into(imgCover);
                    Glide.with(DetailKindActivity.this).load(Config.LINK_LOAD_IMAGE + data.result.get(0).image).into(image);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataKindManager.startGetDataKind(id, Config.API_DATA_KIND);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void setUpAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecorationUtils(2, dpToPx(), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void getDataFilm() {
        list.clear();
        showProcessDialog();
        GetDataKindDetailManager getDataKindDetailManager = new GetDataKindDetailManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    list.addAll(data.result);
                    adapter.notifyDataSetChanged();
                    disProcessDialog();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getDataKindDetailManager.startGetDataKindDetail(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id);
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
    }

    @Override
    public void changSetDataFilm() {
        getDataFilm();
    }
}

package android.trithe.sqlapp.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.CastAdapter;
import android.trithe.sqlapp.adapter.KindDetailAdapter;
import android.trithe.sqlapp.adapter.KindFilmAdapter;
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.callback.OnKindItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetAllDataCastManager;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.manager.GetDataKindManager;
import android.trithe.sqlapp.rest.model.CastDetailModel;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.model.KindModel;
import android.trithe.sqlapp.rest.response.GetAllDataCastResponse;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.rest.response.GetDataKindResponse;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class KindActivity extends AppCompatActivity implements View.OnClickListener, OnFilmItemClickListener, OnKindItemClickListener {
    private RecyclerView recylerView;
    private List<KindModel> list = new ArrayList<>();
    private List<CastDetailModel> listCast = new ArrayList<>();
    private List<FilmModel> listFilm = new ArrayList<>();
    private KindFilmAdapter adapter;
    private KindDetailAdapter detailAdapter;
    private CastAdapter castAdapter;
    private ImageView btnBack;
    private EditText edSearch;
    private ImageView btnClear;
    private ImageView btnSearch;
    private Button btnMovie, btnCast;
    private String key_check;
    private ProgressDialog pDialog;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kind);
        initView();
        pDialog = new ProgressDialog(this);
        key_check = Constant.NB0;
        adapter = new KindFilmAdapter(list);
        castAdapter = new CastAdapter(listCast);
        detailAdapter = new KindDetailAdapter(listFilm);
        adapter.setOnItemClickListener(this);
        detailAdapter.setOnClickItemPopularFilm(this);
        setUpAdapter();
        checkBundle();
        checkClearSearch(edSearch, btnClear);
        checkFocus(edSearch, btnClear);
        checkActionSearch();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkBundle() {
        showProcessDialog();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            getDataKind();
        } else {
            edSearch.setText("");
            key_check = Constant.NB1;
            btnCast.setBackground(getDrawable(R.drawable.border_text));
            btnMovie.setBackground((getDrawable(R.drawable.input)));
            getAllDataCast();
        }
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


    private void getDataKind() {
        list.clear();
        GetDataKindManager getDataKindManager = new GetDataKindManager(new ResponseCallbackListener<GetDataKindResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataKindResponse data) {
                if (data.status.equals("200")) {
                    list.addAll(data.result);
                    adapter.notifyDataSetChanged();
                    recylerView.setAdapter(adapter);
                    disProcessDialog();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getDataKindManager.startGetDataKind(null, Config.API_KIND);
    }

    private void getAllDataCast() {
        listCast.clear();
        GetAllDataCastManager getAllDataCastManager = new GetAllDataCastManager(new ResponseCallbackListener<GetAllDataCastResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataCastResponse data) {
                if (data.status.equals("200")) {
                    listCast.addAll(data.result);
                    castAdapter.notifyDataSetChanged();
                    recylerView.setAdapter(castAdapter);
                    disProcessDialog();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getAllDataCastManager.startGetDataCast(null, Config.API_GET_ALL_CAST);
    }

    private void getDataSearchFilm() {
        listFilm.clear();
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    listFilm.addAll(data.result);
                    detailAdapter.notifyDataSetChanged();
                    recylerView.setAdapter(detailAdapter);
                    disProcessDialog();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getDataFilmManager.startGetDataFilm(edSearch.getText().toString(), Config.API_SEARCH_FILM);
    }

    private void getDataCastSearch() {
        listCast.clear();
        GetAllDataCastManager getAllDataCastManager = new GetAllDataCastManager(new ResponseCallbackListener<GetAllDataCastResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataCastResponse data) {
                if (data.status.equals("200")) {
                    listCast.addAll(data.result);
                    castAdapter.notifyDataSetChanged();
                    recylerView.setAdapter(castAdapter);
                    disProcessDialog();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getAllDataCastManager.startGetDataCast(edSearch.getText().toString(), Config.API_SEARCH_CAST);
    }

    private void setUpAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recylerView.setLayoutManager(mLayoutManager);
        recylerView.addItemDecoration(new GridSpacingItemDecoration(dpToPx()));
        recylerView.setItemAnimator(new DefaultItemAnimator());
        recylerView.setAdapter(adapter);
    }

    private void checkClearSearch(EditText editText, final ImageView imageView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                if (after > 0) {
                    imageView.setVisibility(View.VISIBLE);
                } else if (start == 0) {
                    imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void checkFocus(final EditText editText, final ImageView imageView) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!editText.getText().toString().isEmpty()) {
                imageView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkActionSearch() {
        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                setActionSearch();
            }
            return false;
        });
    }

    class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        GridSpacingItemDecoration(int spacing) {
            this.spanCount = 2;
            this.spacing = spacing;
            this.includeEdge = true;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
    }

    private void initView() {
        recylerView = findViewById(R.id.recyler_view);
        btnBack = findViewById(R.id.btnBack);
        edSearch = findViewById(R.id.edSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnClear = findViewById(R.id.btnClear);
        btnMovie = findViewById(R.id.btnMovie);
        btnCast = findViewById(R.id.btnCast);

        btnBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnCast.setOnClickListener(this);
        btnMovie.setOnClickListener(this);
    }

    private void setActionSearch() {
        showProcessDialog();
        if (!edSearch.getText().toString().isEmpty()) {
            if (key_check.equals(Constant.NB0)) {
                getDataSearchFilm();
            } else {
                getDataCastSearch();
            }
        }
    }

    private void checkData() {
        if (key_check.equals(Constant.NB0)) {
            getDataKind();
        } else {
            getAllDataCast();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnSearch:
                setActionSearch();
                break;
            case R.id.btnClear:
                edSearch.setText("");
                checkData();
                break;
            case R.id.btnMovie:
                showProcessDialog();
                edSearch.setText("");
                key_check = Constant.NB0;
                btnCast.setBackground((getDrawable(R.drawable.input)));
                btnMovie.setBackground((getDrawable(R.drawable.border_text)));
                getDataKind();
                break;
            case R.id.btnCast:
                showProcessDialog();
                edSearch.setText("");
                key_check = Constant.NB1;
                btnCast.setBackground(getDrawable(R.drawable.border_text));
                btnMovie.setBackground((getDrawable(R.drawable.input)));
                getAllDataCast();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        castAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onFilm(FilmModel filmModel, ImageView imageView) {
        Intent intent = new Intent(this, DetailFilmActivity.class);
        intent.putExtra(Constant.TITLE, filmModel.name);
        intent.putExtra(Constant.DETAIL, filmModel.detail);
        intent.putExtra(Constant.FORMAT, filmModel.format);
        intent.putExtra(Constant.ID, filmModel.id);
        intent.putExtra(Constant.DATE, filmModel.releaseDate);
        intent.putExtra(Constant.IMAGE, filmModel.image);
        intent.putExtra(Constant.IMAGE_COVER, filmModel.imageCover);
        intent.putExtra(Constant.MOVIE, filmModel.movie);
        intent.putExtra(Constant.TRAILER, filmModel.trailer);
        intent.putExtra(Constant.TIME, filmModel.time);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, imageView, getResources().getString(R.string.shareName));
        startActivity(intent, options.toBundle());
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onKind(KindModel kindModel, ImageView imageView) {
        Intent intent = new Intent(this, DetailKindActivity.class);
        intent.putExtra(Constant.ID, kindModel.id);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(KindActivity.this, imageView, getResources().getString(R.string.shareName));
        startActivity(intent, options.toBundle());
    }
}

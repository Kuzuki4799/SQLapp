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
import android.trithe.sqlapp.callback.OnCastItemClickListener;
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.callback.OnKindItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetAllDataCastManager;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.model.CastDetailModel;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.model.KindModel;
import android.trithe.sqlapp.rest.response.GetAllDataCastResponse;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, OnFilmItemClickListener, OnKindItemClickListener, OnCastItemClickListener {
    private RecyclerView recyclerView;
    private List<CastDetailModel> listCast = new ArrayList<>();
    private List<FilmModel> listFilm = new ArrayList<>();
    private KindDetailAdapter detailAdapter;
    private CastAdapter castAdapter;
    private ImageView btnBack;
    private EditText edSearch;
    private ImageView btnClear;
    private ImageView btnSearch;
    private Button btnMovie, btnCast;
    private String key_check;
    private ProgressDialog pDialog;
    private TextView txtNoMovie;
    public static final int REQUEST_LOGIN = 999;
    Bundle bundle;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        key_check = Constant.NB0;
        bundle = getIntent().getExtras();
        pDialog = new ProgressDialog(this);
        castAdapter = new CastAdapter(listCast, this);
        detailAdapter = new KindDetailAdapter(listFilm, this);
        setUpAdapter();
        checkBundle();
        checkActionSearch();
        checkClearSearch(edSearch, btnClear);
        checkFocus(edSearch, btnClear);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkBundle() {
        if (bundle != null) {
            edSearch.setText("");
            key_check = Constant.NB1;
            btnCast.setBackground(getDrawable(R.drawable.border_text));
            btnMovie.setBackground((getDrawable(R.drawable.input)));
        }
        checkKeyCheck();
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
        listFilm.clear();
        showProcessDialog();
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    listFilm.addAll(data.result);
                    detailAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(detailAdapter);
                    txtNoMovie.setVisibility(View.GONE);
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataFilmManager.startGetDataFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_FILM);
    }

    private void getAllDataCast() {
        listCast.clear();
        showProcessDialog();
        GetAllDataCastManager getAllDataCastManager = new GetAllDataCastManager(new ResponseCallbackListener<GetAllDataCastResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataCastResponse data) {
                if (data.status.equals("200")) {
                    listCast.addAll(data.result);
                    castAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(castAdapter);
                    txtNoMovie.setVisibility(View.GONE);
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getAllDataCastManager.startGetDataCast(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_GET_ALL_CAST);
    }

    private void getDataSearchFilm() {
        listFilm.clear();
        showProcessDialog();
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    txtNoMovie.setVisibility(View.GONE);
                    listFilm.addAll(data.result);
                    detailAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(detailAdapter);
                } else {
                    txtNoMovie.setText(R.string.search_not);
                    txtNoMovie.setVisibility(View.VISIBLE);
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getDataFilmManager.startGetDataFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), edSearch.getText().toString(), Config.API_SEARCH_FILM);
    }

    private void getDataCastSearch() {
        listCast.clear();
        showProcessDialog();
        GetAllDataCastManager getAllDataCastManager = new GetAllDataCastManager(new ResponseCallbackListener<GetAllDataCastResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataCastResponse data) {
                if (data.status.equals("200")) {
                    txtNoMovie.setVisibility(View.GONE);
                    listCast.addAll(data.result);
                    castAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(castAdapter);
                } else {
                    txtNoMovie.setText(R.string.not_have_cast);
                    txtNoMovie.setVisibility(View.VISIBLE);
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getAllDataCastManager.startGetDataCast(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), edSearch.getText().toString(), Config.API_SEARCH_CAST);
    }

    private void setUpAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(dpToPx()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(detailAdapter);
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
                checkKeyCheck();
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
        recyclerView = findViewById(R.id.recyler_view);
        btnBack = findViewById(R.id.btnBack);
        edSearch = findViewById(R.id.edSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnClear = findViewById(R.id.btnClear);
        btnMovie = findViewById(R.id.btnMovie);
        btnCast = findViewById(R.id.btnCast);
        txtNoMovie = findViewById(R.id.txtNoMovie);

        btnBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnCast.setOnClickListener(this);
        btnMovie.setOnClickListener(this);
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
                checkKeyCheck();
                break;
            case R.id.btnClear:
                edSearch.setText("");
                checkData();
                break;
            case R.id.btnMovie:
                edSearch.setText("");
                key_check = Constant.NB0;
                btnCast.setBackground((getDrawable(R.drawable.input)));
                btnMovie.setBackground((getDrawable(R.drawable.border_text)));
                checkKeyCheck();
                break;
            case R.id.btnCast:
                edSearch.setText("");
                key_check = Constant.NB1;
                btnCast.setBackground(getDrawable(R.drawable.border_text));
                btnMovie.setBackground((getDrawable(R.drawable.input)));
                checkKeyCheck();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        checkKeyCheck();
    }

    private void checkKeyCheck() {
        if (key_check.equals(Constant.NB0)) {
            if (!edSearch.getText().toString().isEmpty()) {
                getDataSearchFilm();
            } else {
                getDataKind();
            }
        } else {
            if (edSearch.getText().toString().isEmpty()) {
                getAllDataCast();
            } else {
                getDataCastSearch();
            }
        }
    }

    @Override
    public void changSetData() {
        if (SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty()) {
            Intent intents = new Intent(this, LoginActivity.class);
            startActivityForResult(intents, REQUEST_LOGIN);
        } else {
            checkKeyCheck();
        }
    }

    @Override
    public void changSetDataFilm() {
        if (SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty()) {
            Intent intents = new Intent(this, LoginActivity.class);
            startActivityForResult(intents, REQUEST_LOGIN);
        } else {
            checkKeyCheck();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onKind(KindModel kindModel, ImageView imageView) {
        Intent intent = new Intent(this, DetailKindActivity.class);
        intent.putExtra(Constant.ID, kindModel.id);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SearchActivity.this, imageView, getResources().getString(R.string.shareName));
        startActivity(intent, options.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                changSetData();
            }
        }
    }
}

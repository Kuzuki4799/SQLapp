package android.trithe.sqlapp.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.CastAdapter;
import android.trithe.sqlapp.adapter.KindDetailAdapter;
import android.trithe.sqlapp.callback.OnChangeSetCastItemClickListener;
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.callback.OnKindItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetAllDataCastManager;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.model.CastModel;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.model.KindModel;
import android.trithe.sqlapp.rest.response.GetAllDataCastResponse;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.utils.EndlessRecyclerOnScrollListener;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.widget.PullToRefresh.MyPullToRefresh;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener,
        OnFilmItemClickListener, OnKindItemClickListener, OnChangeSetCastItemClickListener {
    private RecyclerView recyclerView;
    private List<CastModel> listCast = new ArrayList<>();
    private List<FilmModel> listFilm = new ArrayList<>();
    private KindDetailAdapter detailAdapter;
    private CastAdapter castAdapter;
    private ImageView btnBack;
    private EditText edSearch;
    private ImageView btnClear;
    private CircleImageView btnSearch;
    private Button btnMovie, btnCast;
    private String key_check;
    private TextView txtNoMovie;
    public static final int REQUEST_LOGIN = 999;
    private MyPullToRefresh swRefreshRecyclerView;
    private Bundle bundle;
    private ProgressBar progressBar;
    private int page = 0;
    private int per_page = 6;
    private LinearLayoutManager linearLayoutManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        listener();
        key_check = Constant.NB0;
        bundle = getIntent().getExtras();
        castAdapter = new CastAdapter(listCast, this);
        detailAdapter = new KindDetailAdapter(listFilm, this);
        linearLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecorationUtils(2, dpToPx(), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        checkActionSearch();
        checkClearSearch(edSearch, btnClear);
        checkFocus(edSearch, btnClear);
        setUpAdapter();
        resetLoadMore();
        swRefreshRecyclerView.setOnRefreshBegin(recyclerView,
                new MyPullToRefresh.PullToRefreshHeader(this), this::resetLoadMore);
    }

    private void listener() {
        btnBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnCast.setOnClickListener(this);
        btnMovie.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void resetLoadMore() {
        progressBar.setVisibility(View.VISIBLE);
        castAdapter.setOnLoadMore(true);
        detailAdapter.setOnLoadMore(true);
        listCast.clear();
        listFilm.clear();
        checkBundle();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkBundle() {
        if (bundle != null) {
            key_check = Constant.NB1;
            setUpAdapter();
            edSearch.setText("");
            btnCast.setBackground(getDrawable(R.drawable.border_text));
            btnMovie.setBackground((getDrawable(R.drawable.input)));
            checkKeyCheck(page, per_page);
        } else {
            setUpAdapter();
            checkKeyCheck(page, per_page);
        }
    }

    private void getDataKind(int page, int per_page) {
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    listFilm.addAll(data.result);
                    detailAdapter.notifyDataSetChanged();
                    txtNoMovie.setVisibility(View.GONE);
                    if (data.result.size() < 6) {
                        detailAdapter.setOnLoadMore(false);
                    }
                } else {
                    detailAdapter.setOnLoadMore(false);
                }
                swRefreshRecyclerView.refreshComplete();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                progressBar.setVisibility(View.GONE);
            }
        });
        getDataFilmManager.startGetDataFilm(0, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null,
                page, per_page, Config.API_FILM);
    }

    private void getDataSearchFilm(int page, int per_page) {
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    txtNoMovie.setVisibility(View.GONE);
                    listFilm.addAll(data.result);
                    detailAdapter.notifyDataSetChanged();
                    if (data.result.size() < 6) {
                        detailAdapter.setOnLoadMore(false);
                    }
                } else if (data.status.equals("400")) {
                    txtNoMovie.setText(R.string.search_not);
                    txtNoMovie.setVisibility(View.VISIBLE);
                } else {
                    detailAdapter.setOnLoadMore(false);
                }
                progressBar.setVisibility(View.GONE);
                swRefreshRecyclerView.refreshComplete();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                progressBar.setVisibility(View.GONE);
            }
        });
        getDataFilmManager.startGetDataFilm(0, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""),
                edSearch.getText().toString(), page, per_page, Config.API_SEARCH_FILM);
    }

    private void getAllDataCast(int page, int per_page) {
        GetAllDataCastManager getAllDataCastManager = new GetAllDataCastManager(new ResponseCallbackListener<GetAllDataCastResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataCastResponse data) {
                if (data.status.equals("200")) {
                    listCast.addAll(data.result);
                    castAdapter.notifyDataSetChanged();
                    txtNoMovie.setVisibility(View.GONE);
                    if (data.result.size() < 6) {
                        castAdapter.setOnLoadMore(false);
                    }
                } else {
                    castAdapter.setOnLoadMore(false);
                }
                swRefreshRecyclerView.refreshComplete();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                progressBar.setVisibility(View.GONE);
            }
        });
        getAllDataCastManager.startGetDataCast(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null,
                page, per_page, Config.API_GET_ALL_CAST);
    }

    private void getDataCastSearch(int page, int per_page) {
        GetAllDataCastManager getAllDataCastManager = new GetAllDataCastManager(new ResponseCallbackListener<GetAllDataCastResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataCastResponse data) {
                if (data.status.equals("200")) {
                    txtNoMovie.setVisibility(View.GONE);
                    listCast.addAll(data.result);
                    castAdapter.notifyDataSetChanged();
                    if (data.result.size() < 6) {
                        castAdapter.setOnLoadMore(false);
                    }
                } else if (data.status.equals("400")) {
                    txtNoMovie.setText(R.string.not_have_cast);
                    txtNoMovie.setVisibility(View.VISIBLE);
                } else {
                    castAdapter.setOnLoadMore(false);
                }
                swRefreshRecyclerView.refreshComplete();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                progressBar.setVisibility(View.GONE);
            }
        });
        getAllDataCastManager.startGetDataCast(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""),
                edSearch.getText().toString(), page, per_page, Config.API_SEARCH_CAST);
    }

    private void setUpAdapter() {
        if (key_check.equals(Constant.NB0)) {
            recyclerView.setAdapter(detailAdapter);
            recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    new Handler().postDelayed(() -> {
                        if (edSearch.getText().toString().isEmpty()) {
                            getDataKind(page, per_page);
                        } else {
                            getDataSearchFilm(page, per_page);
                        }
                    }, 500);
                }
            });
        } else {
            recyclerView.setAdapter(castAdapter);
            recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    new Handler().postDelayed(() -> {
                        if (edSearch.getText().toString().isEmpty()) {
                            getAllDataCast(page, per_page);
                        } else {
                            getDataCastSearch(page, per_page);
                        }
                    }, 500);
                }
            });
        }
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
                checkKeyCheck(page, per_page);
            }
            return false;
        });
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
    }

    private void initView() {
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_view);
        btnBack = findViewById(R.id.btnBack);
        edSearch = findViewById(R.id.edSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnClear = findViewById(R.id.btnClear);
        btnMovie = findViewById(R.id.btnMovie);
        btnCast = findViewById(R.id.btnCast);
        txtNoMovie = findViewById(R.id.txtNoMovie);
        swRefreshRecyclerView = findViewById(R.id.swRefreshRecyclerViewSearch);
    }

    private void checkData() {
        if (key_check.equals(Constant.NB0)) {
            detailAdapter.setOnLoadMore(true);
            listFilm.clear();
            getDataKind(page, per_page);
        } else {
            castAdapter.setOnLoadMore(true);
            listCast.clear();
            getAllDataCast(page, per_page);
        }
    }

    private void checkDataFilm() {
        if (key_check.equals(Constant.NB0)) {
            detailAdapter.setOnLoadMore(true);
            listFilm.clear();
            getDataSearchFilm(page, per_page);
        } else {
            castAdapter.setOnLoadMore(true);
            listCast.clear();
            getDataCastSearch(page, per_page);
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
                setUpAdapter();
                checkDataFilm();
                break;
            case R.id.btnClear:
                edSearch.setText("");
                setUpAdapter();
                checkData();
                break;
            case R.id.btnMovie:
                bundle = null;
                edSearch.setText("");
                key_check = Constant.NB0;
                setUpAdapter();
                btnCast.setBackground((getDrawable(R.drawable.input)));
                btnMovie.setBackground((getDrawable(R.drawable.border_text)));
                checkKeyCheck(page, per_page);
                break;
            case R.id.btnCast:
                bundle = null;
                edSearch.setText("");
                key_check = Constant.NB1;
                setUpAdapter();
                btnCast.setBackground(getDrawable(R.drawable.border_text));
                btnMovie.setBackground((getDrawable(R.drawable.input)));
                checkKeyCheck(page, per_page);
                break;
        }
    }

    private void checkKeyCheck(int page, int per_page) {
        if (key_check.equals(Constant.NB0)) {
            detailAdapter.setOnLoadMore(true);
            if (!edSearch.getText().toString().isEmpty()) {
                listFilm.clear();
                getDataSearchFilm(page, per_page);
            } else {
                listFilm.clear();
                getDataKind(page, per_page);
            }
        } else {
            castAdapter.setOnLoadMore(true);
            if (edSearch.getText().toString().isEmpty()) {
                listCast.clear();
                getAllDataCast(page, per_page);
            } else {
                listCast.clear();
                getDataCastSearch(page, per_page);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCheckItemCast(int position, CardView cardView) {
        Intent intent = new Intent(this, CastActivity.class);
        intent.putExtra(Constant.ID, listCast.get(position).id);
        intent.putExtra(Constant.POSITION, position);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, cardView, getResources().getString(R.string.app_name));
        startActivityForResult(intent, Constant.KEY_INTENT_CAST, options.toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void changSetDataCast(int position, String key) {
        if (SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty()) {
            Intent intents = new Intent(this, LoginActivity.class);
            startActivityForResult(intents, REQUEST_LOGIN);
        } else {
            if (key.equals(Config.API_DELETE_LOVE_CAST)) {
                listCast.get(position).setLoved(0);
            } else {
                listCast.get(position).setLoved(1);
            }
            castAdapter.notifyItemChanged(position);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCheckItemFilm(int position, CardView cardView) {
        Intent intent = new Intent(this, DetailFilmActivity.class);
        intent.putExtra(Constant.ID, listFilm.get(position).id);
        intent.putExtra(Constant.POSITION, position);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, cardView,
                getResources().getString(R.string.app_name));
        startActivityForResult(intent, Constant.KEY_INTENT, options.toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void changSetDataFilm(int position, String key) {
        if (SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty()) {
            Intent intents = new Intent(this, LoginActivity.class);
            startActivityForResult(intents, REQUEST_LOGIN);
        } else {
            if (key.equals(Config.API_DELETE_SAVED)) {
                listFilm.get(position).setSaved(0);
            } else {
                listFilm.get(position).setSaved(1);
            }
            detailAdapter.notifyItemChanged(position);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onKind(KindModel kindModel, CardView cardView) {
        Intent intent = new Intent(this, DetailKindActivity.class);
        intent.putExtra(Constant.ID, kindModel.id);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SearchActivity.this, cardView, getResources().getString(R.string.shareName));
        startActivityForResult(intent, Constant.KEY_INTENT, options.toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                resetLoadMore();
            }
            if (requestCode == Constant.KEY_INTENT) {
                if (Objects.requireNonNull(data).getBooleanExtra(Constant.KEY_CHECK, false)) {
                    listFilm.get(data.getIntExtra(Constant.POSITION, 0)).setSaved(0);
                } else {
                    listFilm.get(data.getIntExtra(Constant.POSITION, 0)).setSaved(1);
                }
                detailAdapter.notifyItemChanged(data.getIntExtra(Constant.POSITION, 0));
            }

            if (requestCode == Constant.KEY_INTENT_CAST) {
                if (Objects.requireNonNull(data).getBooleanExtra(Constant.KEY_CHECK, false)) {
                    listCast.get(data.getIntExtra(Constant.POSITION, 0)).setLoved(0);
                } else {
                    listCast.get(data.getIntExtra(Constant.POSITION, 0)).setLoved(1);
                }
                castAdapter.notifyItemChanged(data.getIntExtra(Constant.POSITION, 0));
            }
        }
    }
}

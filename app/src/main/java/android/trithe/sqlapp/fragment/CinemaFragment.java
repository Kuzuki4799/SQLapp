package android.trithe.sqlapp.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.trithe.sqlapp.adapter.CinemaAdapter;
import android.trithe.sqlapp.adapter.HorizontalPagerCinemaAdapter;
import android.trithe.sqlapp.adapter.UpComingAdapter;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataCinemaManager;
import android.trithe.sqlapp.rest.manager.GetDataFilmCinemaManager;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.model.CinemaModel;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.model.ShowingFilmCinemaModel;
import android.trithe.sqlapp.rest.response.GetAllDataCinemaResponse;
import android.trithe.sqlapp.rest.response.GetAllDataFilmCinemaResponse;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.trithe.sqlapp.R;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CinemaFragment extends Fragment implements View.OnClickListener {
    private Button btnInTheatres;
    private Button btnUpComing;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewCinema;
    private List<ShowingFilmCinemaModel> listShowingFilmCinema = new ArrayList<>();
    private List<FilmModel> listUpComing = new ArrayList<>();
    private List<CinemaModel> listCinema = new ArrayList<>();
    private UpComingAdapter upComingAdapter;
    private CinemaAdapter cinemaAdapter;
    private ProgressDialog pDialog;
    private RelativeLayout llMap;
    private HorizontalInfiniteCycleViewPager cycleViewPaper;
    private HorizontalPagerCinemaAdapter horizontalPagerCinemaAdapter;
    private CircleImageView imgCinemaMap;
    private SwipeRefreshLayout swRecyclerViewCinema;
    private int flag;
    private LinearLayoutManager mLayoutManagerCinema;
    private SnapHelper snapHelper;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 5555;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cinema, container, false);
        initView(view);
        flag = 0;
        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewCinema);
        horizontalPagerCinemaAdapter = new HorizontalPagerCinemaAdapter(listShowingFilmCinema, getContext());
        pDialog = new ProgressDialog(getContext());
        setUpAdapter();
        checkFlag();
        if (!checkPermission(getContext(), PERMISSIONS)) {
            requestPermission();
        }
        swRecyclerViewCinema.setOnRefreshListener(this::checkFlag);
        return view;
    }

    private boolean checkPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context != null && permissions != null) {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
            return false;
        } else return true;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), PERMISSIONS, REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_SOME_FEATURES_PERMISSIONS) {
            for (int i = 0; i < PERMISSIONS.length; i++) {
                if (grantResults.length > 0 && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermission();
                        }
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermission();
                        }
                        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.RECORD_AUDIO)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermission();
                        }
                    }
                }
            }
        }
    }

    private void setUpAdapter() {
        upComingAdapter = new UpComingAdapter(listUpComing, this::getDataKind);
        cinemaAdapter = new CinemaAdapter(listCinema);

        mLayoutManagerCinema = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCinema.setLayoutManager(mLayoutManagerCinema);
        recyclerViewCinema.setAdapter(cinemaAdapter);
    }

    private int getSnapPosition(SnapHelper snapHelper) {
        return mLayoutManagerCinema.getPosition(Objects.requireNonNull(snapHelper.findSnapView(mLayoutManagerCinema)));
    }

    private void handlerScale() {
        new Handler().postDelayed(() -> {
            RecyclerView.ViewHolder viewHolder = recyclerViewCinema.findViewHolderForAdapterPosition(0);
            assert viewHolder != null;
            TextView txtCinema = viewHolder.itemView.findViewById(R.id.txtCinema);
            txtCinema.animate().scaleY(1.1f).scaleX(1.1f).setDuration(350).setInterpolator(new AccelerateInterpolator()).start();
            getDataPremise(Integer.parseInt(listCinema.get(getSnapPosition(snapHelper)).id));
        }, 2000);

        recyclerViewCinema.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View v = snapHelper.findSnapView(mLayoutManagerCinema);
                int pos = mLayoutManagerCinema.getPosition(v);
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(pos);
                assert viewHolder != null;
                TextView txtCinema = viewHolder.itemView.findViewById(R.id.txtCinema);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    txtCinema.animate().setDuration(350).scaleX(1.1f).scaleY(1.1f).setInterpolator(new AccelerateInterpolator()).start();
                    getDataPremise(Integer.parseInt(listCinema.get(getSnapPosition(snapHelper)).id));
                } else {
                    txtCinema.animate().setDuration(350).scaleX(0.9f).scaleY(0.9f).setInterpolator(new AccelerateInterpolator()).start();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
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

    private void getDataPremise(int cinema_id) {
        listShowingFilmCinema.clear();
        showProcessDialog();
        horizontalPagerCinemaAdapter = new HorizontalPagerCinemaAdapter(listShowingFilmCinema, getContext());
        cycleViewPaper.setAdapter(horizontalPagerCinemaAdapter);
        GetDataFilmCinemaManager getDataFilmCinemaManager = new GetDataFilmCinemaManager(new ResponseCallbackListener<GetAllDataFilmCinemaResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataFilmCinemaResponse data) {
                if (data.status.equals("200")) {
                    listShowingFilmCinema.addAll(data.result);
                    horizontalPagerCinemaAdapter.notifyDataSetChanged();
                    cycleViewPaper.notifyDataSetChanged();
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getDataFilmCinemaManager.getFilmByDateCinema(cinema_id, SharedPrefUtils.getInt(Constant.KEY_USER_ID, 0));
    }

    private void getDataCinema() {
        listCinema.clear();
        showProcessDialog();
        GetDataCinemaManager getDataCinemaManager = new GetDataCinemaManager(new ResponseCallbackListener<GetAllDataCinemaResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataCinemaResponse data) {
                if (data.status.equals("200")) {
                    listCinema.addAll(data.result);
                    cinemaAdapter.notifyDataSetChanged();
                    handlerScale();
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
                disProcessDialog();
            }
        });
        getDataCinemaManager.startGetDataCinema(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_GET_CINEMA);
    }

    private void getDataKind() {
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(upComingAdapter);
        listUpComing.clear();
        swRecyclerViewCinema.setRefreshing(true);
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    listUpComing.addAll(data.result);
                    upComingAdapter.notifyDataSetChanged();
                }
                swRecyclerViewCinema.setRefreshing(false);
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataFilmManager.startGetDataFilm(2, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_FILM);
    }

    private void initView(View view) {
        swRecyclerViewCinema = view.findViewById(R.id.swRecyclerViewCinema);
        btnInTheatres = view.findViewById(R.id.btnInTheatres);
        btnUpComing = view.findViewById(R.id.btnUpComing);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerViewCinema = view.findViewById(R.id.recycler_view_cinema);
        llMap = view.findViewById(R.id.llMap);
        imgCinemaMap = view.findViewById(R.id.imgCinemaMap);
        cycleViewPaper = view.findViewById(R.id.cycleViewPaper);

        btnUpComing.setOnClickListener(this);
        btnInTheatres.setOnClickListener(this);
        imgCinemaMap.setOnClickListener(this);
    }

    private void checkFlag() {
        if (flag == 0) {
            cycleViewPaper.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            llMap.setVisibility(View.VISIBLE);
            getDataCinema();
        } else {
            cycleViewPaper.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            llMap.setVisibility(View.GONE);
            getDataKind();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInTheatres:
                flag = 0;
                getDataCinema();
                btnUpComing.setBackground((Objects.requireNonNull(getContext()).getDrawable(R.drawable.input)));
                btnInTheatres.setBackground((getContext().getDrawable(R.drawable.border_text)));
                cycleViewPaper.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                llMap.setVisibility(View.VISIBLE);
                break;
            case R.id.btnUpComing:
                flag = 1;
                getDataKind();
                btnUpComing.setBackground(Objects.requireNonNull(getContext()).getDrawable(R.drawable.border_text));
                btnInTheatres.setBackground((Objects.requireNonNull(getContext()).getDrawable(R.drawable.input)));
                cycleViewPaper.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                llMap.setVisibility(View.GONE);
                break;
            case R.id.imgCinemaMap:
//                startActivity(new Intent(getContext(), MapCinemaActivity.class));
                MapFragment mapFragment = new MapFragment();
                loadFragment(mapFragment);
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

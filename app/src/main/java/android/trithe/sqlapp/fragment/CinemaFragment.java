package android.trithe.sqlapp.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.activity.CinemaDetailActivity;
import android.trithe.sqlapp.activity.MapCinemaActivity;
import android.trithe.sqlapp.adapter.HorizontalPagerCinemaAdapter;
import android.trithe.sqlapp.adapter.UpComingAdapter;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataCinemaManager;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.response.GetAllDataCinemaResponse;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.trithe.sqlapp.R;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private List<FilmModel> listFilm = new ArrayList<>();
    private List<FilmModel> listUpComing = new ArrayList<>();
    private UpComingAdapter upComingAdapter;
    private ProgressDialog pDialog;
    private HorizontalInfiniteCycleViewPager cycleViewPaper;
    private HorizontalPagerCinemaAdapter horizontalPagerCinemaAdapter;
    private LinearLayout llMap;
    private CircleImageView imgCinemaMap;
    private TextView txtCinema;
    private ImageView btnNext, btnBack;
    private int flag, count;

    private static final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 5555;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cinema, container, false);
        initView(view);
        flag = 0;
        pDialog = new ProgressDialog(getContext());
        upComingAdapter = new UpComingAdapter(listUpComing, this::getDataKind);
        horizontalPagerCinemaAdapter = new HorizontalPagerCinemaAdapter(listFilm, getContext(), this::getDataPremise);
        setUpAdapter();
        checkFlag();
        getDataCinema();
        if (!checkPermission(getContext(), PERMISSIONS)) {
            requestPermission();
        }
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
                    }
                }
            }
        }
    }

    private void setUpAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(upComingAdapter);

        cycleViewPaper.setAdapter(horizontalPagerCinemaAdapter);
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

    private void getDataPremise() {
        listFilm.clear();
        showProcessDialog();
        horizontalPagerCinemaAdapter = new HorizontalPagerCinemaAdapter(listFilm, getContext(), this::getDataPremise);
        cycleViewPaper.setAdapter(horizontalPagerCinemaAdapter);
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    listFilm.addAll(data.result);
                    horizontalPagerCinemaAdapter.notifyDataSetChanged();
                    cycleViewPaper.notifyDataSetChanged();
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {
            }
        });
        getDataFilmManager.startGetDataFilm(1, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_FILM);
    }

    private void getDataCinema() {
        GetDataCinemaManager getDataCinemaManager = new GetDataCinemaManager(new ResponseCallbackListener<GetAllDataCinemaResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataCinemaResponse data) {
                if (data.status.equals("200")) {
                    count = 0;
                    txtCinema.setText(data.result.get(count).name);
                    if (count == 0) {
                        btnBack.setVisibility(View.GONE);
                    } else {
                        btnBack.setVisibility(View.VISIBLE);
                    }
                    btnNext.setOnClickListener(v -> {
                        count = count + 1;
                        btnBack.setVisibility(View.VISIBLE);
                        txtCinema.setText(data.result.get(count).name);
                    });
                    btnBack.setOnClickListener(v -> {
                        count = count - 1;
                        txtCinema.setText(data.result.get(count).name);
                        if (count == 0) {
                            btnBack.setVisibility(View.GONE);
                        }
                    });
                    txtCinema.setOnClickListener(v -> {
                        Intent intent = new Intent(getContext(), CinemaDetailActivity.class);
                        intent.putExtra(Constant.ID, data.result.get(count).id);
                        startActivity(intent);
                    });
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataCinemaManager.startGetDataCinema(null, Config.API_GET_CINEMA);
    }


    private void getDataKind() {
        listUpComing.clear();
        showProcessDialog();
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    listUpComing.addAll(data.result);
                    upComingAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(upComingAdapter);
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataFilmManager.startGetDataFilm(2, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_FILM);
    }

    private void initView(View view) {
        btnInTheatres = view.findViewById(R.id.btnInTheatres);
        btnUpComing = view.findViewById(R.id.btnUpComing);
        recyclerView = view.findViewById(R.id.recycler_view);
        cycleViewPaper = view.findViewById(R.id.cycleViewPaper);
        llMap = view.findViewById(R.id.llMap);
        imgCinemaMap = view.findViewById(R.id.imgCinemaMap);
        txtCinema = view.findViewById(R.id.txtCinema);
        btnNext = view.findViewById(R.id.btnNext);
        btnBack = view.findViewById(R.id.btnBack);

        btnUpComing.setOnClickListener(this);
        btnInTheatres.setOnClickListener(this);
        imgCinemaMap.setOnClickListener(this);
    }

    private void checkFlag() {
        if (flag == 0) {
            getDataPremise();
            cycleViewPaper.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            llMap.setVisibility(View.VISIBLE);
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
                getDataPremise();
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
                startActivity(new Intent(getContext(), MapCinemaActivity.class));
                break;
        }
    }
}

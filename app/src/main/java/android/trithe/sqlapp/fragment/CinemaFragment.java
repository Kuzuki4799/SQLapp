package android.trithe.sqlapp.fragment;


import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.UpComingAdapter;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.trithe.sqlapp.R;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CinemaFragment extends Fragment implements View.OnClickListener {
    private Button btnInTheatres;
    private Button btnUpComing;
    private RecyclerView recyclerView;
    private List<FilmModel> listFilm = new ArrayList<>();
    private UpComingAdapter upComingAdapter;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cinema, container, false);
        initView(view);
        pDialog = new ProgressDialog(getContext());
        upComingAdapter = new UpComingAdapter(listFilm, this::getDataKind);
        setUpAdapter();
        return view;
    }

    private void setUpAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(upComingAdapter);
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
                    upComingAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(upComingAdapter);
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataFilmManager.startGetDataFilm(1, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_FILM);
    }

    private void initView(View view) {
        btnInTheatres = view.findViewById(R.id.btnInTheatres);
        btnUpComing = view.findViewById(R.id.btnUpComing);
        recyclerView = view.findViewById(R.id.recycler_view);

        btnUpComing.setOnClickListener(this);
        btnInTheatres.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInTheatres:
                btnUpComing.setBackground((Objects.requireNonNull(getContext()).getDrawable(R.drawable.input)));
                btnInTheatres.setBackground((getContext().getDrawable(R.drawable.border_text)));
                break;
            case R.id.btnUpComing:
                getDataKind();
                btnUpComing.setBackground(Objects.requireNonNull(getContext()).getDrawable(R.drawable.border_text));
                btnInTheatres.setBackground((Objects.requireNonNull(getContext()).getDrawable(R.drawable.input)));
                break;
        }
    }
}

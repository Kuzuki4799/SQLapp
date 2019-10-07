package android.trithe.sqlapp.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.trithe.sqlapp.activity.MapCinemaActivity;
import android.trithe.sqlapp.activity.SearchLocationActivity;
import android.trithe.sqlapp.adapter.CinemaPlaceAdapter;
import android.trithe.sqlapp.callback.AppConfig;
import android.trithe.sqlapp.callback.OnChangeSetCastItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataCinemaManager;
import android.trithe.sqlapp.rest.model.CinemaModel;
import android.trithe.sqlapp.rest.response.GetAllDataCinemaResponse;
import android.trithe.sqlapp.utils.CollectionUtil;
import android.trithe.sqlapp.utils.GoogleMapUtil;
import android.trithe.sqlapp.utils.MapUtil;
import android.trithe.sqlapp.utils.MarkerType;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.trithe.sqlapp.widget.GPSTracker;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.trithe.sqlapp.R;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private RelativeLayout rlMain;
    private RelativeLayout background;
    private RelativeLayout rlSearch;
    private ImageView imgSearch;
    private EditText edtSearch;
    private ImageView imgVoice;
    private ImageView btnCurrentLocation;
    private ImageView btnNavigation;
    private GoogleMap mMap;
    private RecyclerView recyclerView;
    private List<CinemaModel> cinemaModelList = new ArrayList<>();
    private CinemaPlaceAdapter cinemaPlaceAdapter;
    private SupportMapFragment mapFragment;
    private GPSTracker gpsTracker;
    private ProgressDialog pDialog;
    private LinearLayoutManager linearLayoutManager;
    private Double lat;
    private Double longs;
    private Marker marker;
    private Polyline polyline;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        intView(view);
        pDialog = new ProgressDialog(getContext());
        gpsTracker = new GPSTracker(getContext());
        lat = gpsTracker.getLocation().getLatitude();
        longs = gpsTracker.getLocation().getLongitude();
        cinemaPlaceAdapter = new CinemaPlaceAdapter(cinemaModelList, new OnChangeSetCastItemClickListener() {
            @Override
            public void onCheckItemCast(int position, CardView cardView) {

            }

            @Override
            public void changSetDataCast(int position, String key) {
                getDataCinema();
            }
        });
        setUpMap();
        setUpAdapter();
        getDataCinema();
        return view;
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

    private void getDataCinema() {
        cinemaModelList.clear();
        showProcessDialog();
        GetDataCinemaManager getDataCinemaManager = new GetDataCinemaManager(new ResponseCallbackListener<GetAllDataCinemaResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataCinemaResponse data) {
                if (data.status.equals("200")) {
                    cinemaModelList.addAll(data.result);
                    cinemaPlaceAdapter.notifyDataSetChanged();
                }
                disProcessDialog();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataCinemaManager.startGetDataCinema(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_GET_CINEMA);
    }

    private void setUpAdapter() {
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cinemaPlaceAdapter);
        handlerScale(linearLayoutManager);
    }

    private void handlerScale(LinearLayoutManager linearLayoutManager) {
        final SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        new Handler().postDelayed(() -> {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0);
            assert viewHolder != null;
            LinearLayout rl1 = viewHolder.itemView.findViewById(R.id.llMap);
            rl1.animate().scaleY(1).scaleX(1).setDuration(350).setInterpolator(new AccelerateInterpolator()).start();
            LatLng newLatLng = new LatLng(Double.valueOf(cinemaModelList.get(getSnapPosition(snapHelper)).latLocation)
                    , Double.valueOf(cinemaModelList.get(getSnapPosition(snapHelper)).longLocation));
            MarkerOptions markerOptions = new MarkerOptions().position(newLatLng).title(cinemaModelList.get(getSnapPosition(snapHelper)).name);
            marker = mMap.addMarker(markerOptions);

//            if (polyline != null) {
//                polyline.remove();
//            }
//            PolylineOptions polylineOptions = new PolylineOptions();
//            polylineOptions.color(Color.RED);
//            polylineOptions.width(3);
//            List<LatLng> lst = new ArrayList<>();
//            lst.add(0, new LatLng(lat, longs));
//            lst.add(1, newLatLng);
//            polylineOptions.addAll(lst);
//            polyline = mMap.addPolyline(polylineOptions);
//            marker.showInfoWindow();
            GoogleMapUtil.addPolyline(getContext(), mMap, new LatLng(lat, longs), newLatLng);
        }, 1000);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View v = snapHelper.findSnapView(linearLayoutManager);
                int pos = linearLayoutManager.getPosition(v);
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(pos);
                assert viewHolder != null;
                LinearLayout rl1 = viewHolder.itemView.findViewById(R.id.llMap);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    rl1.animate().setDuration(350).scaleX(1).scaleY(1).setInterpolator(new AccelerateInterpolator()).start();
                    if (marker != null) {
                        marker.remove();
                    }
                    LatLng newLatLng = new LatLng(Double.valueOf(cinemaModelList.get(getSnapPosition(snapHelper)).latLocation)
                            , Double.valueOf(cinemaModelList.get(getSnapPosition(snapHelper)).longLocation));
                    MarkerOptions markerOptions = new MarkerOptions().position(newLatLng).title(cinemaModelList.get(getSnapPosition(snapHelper)).name);
                    marker = mMap.addMarker(markerOptions);
//                    if (polyline != null) {
//                        polyline.remove();
//                    }
//                    PolylineOptions polylineOptions = new PolylineOptions();
//                    polylineOptions.color(Color.RED);
//                    polylineOptions.width(3);
//                    List<LatLng> lst = new ArrayList<>();
//                    lst.add(0, new LatLng(lat, longs));
//                    lst.add(1, newLatLng);
//                    polylineOptions.addAll(lst);
//                    polyline = mMap.addPolyline(polylineOptions);
                    GoogleMapUtil.addPolyline(getContext(), mMap, new LatLng(lat, longs), newLatLng);
                } else {
                    rl1.animate().setDuration(350).scaleX(0.9f).scaleY(0.9f).setInterpolator(new AccelerateInterpolator()).start();
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    private int getSnapPosition(SnapHelper snapHelper) {
        return linearLayoutManager.getPosition(Objects.requireNonNull(snapHelper.findSnapView(linearLayoutManager)));
    }

    private void setUpMap() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void intView(View view) {
        rlMain = view.findViewById(R.id.rlMain);
        recyclerView = view.findViewById(R.id.recycler_view);
        background = view.findViewById(R.id.background);
        rlSearch = view.findViewById(R.id.rlSearch);
        imgSearch = view.findViewById(R.id.imgSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        imgVoice = view.findViewById(R.id.imgVoice);
        btnCurrentLocation = view.findViewById(R.id.btnCenter);
        btnNavigation = view.findViewById(R.id.btnNavigation);

        edtSearch.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LatLng latLng = new LatLng(lat, longs);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11.5f));
        mMap.setMyLocationEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edtSearch:
                gotoSearchLocation();
                break;
            case R.id.btnCenter:
                if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                LatLng latLng = new LatLng(lat, longs);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11.5f));
                mMap.setMyLocationEnabled(true);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void gotoSearchLocation() {
        Intent intent = new Intent(getActivity(), SearchLocationActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), rlSearch, "rlSearch");
        getActivity().getWindow().setEnterTransition(null);
        startActivity(intent, options.toBundle());
    }
}

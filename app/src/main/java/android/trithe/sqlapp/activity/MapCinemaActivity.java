package android.trithe.sqlapp.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.CinemaPlaceAdapter;
import android.trithe.sqlapp.callback.OnChangeSetCastItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataCinemaManager;
import android.trithe.sqlapp.rest.model.CinemaModel;
import android.trithe.sqlapp.rest.response.GetAllDataCinemaResponse;
import android.trithe.sqlapp.utils.GoogleMapUtil;
import android.trithe.sqlapp.utils.MapUtil;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapCinemaActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, View.OnClickListener, GoogleMap.OnMyLocationChangeListener {
    private GoogleMap mMap;
    private ImageView imgBack;
    private RecyclerView recyclerView;
    private List<CinemaModel> cinemaModelList = new ArrayList<>();
    private CinemaPlaceAdapter cinemaPlaceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_cinema);
        cinemaPlaceAdapter = new CinemaPlaceAdapter(cinemaModelList, new OnChangeSetCastItemClickListener() {
            @Override
            public void onCheckItemCast(int position, CardView cardView) {

            }

            @Override
            public void changSetDataCast(int position, String key) {

            }
        });
        initView();
        setUpMap();
        setUpAdapter();
        imgBack.setOnClickListener(v -> finish());
    }

    private void setUpAdapter() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MapCinemaActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cinemaPlaceAdapter);
    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void getLocationCinema(String name, String location, LatLng latLng) {
        List<Address> list;
        Geocoder geocoder = new Geocoder(this);
        try {
            list = geocoder.getFromLocationName(location, 1);
            MarkerOptions options = new MarkerOptions().position(new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude())).title(name);
            mMap.addMarker(options).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12f));

            GoogleMapUtil.polyLine(MapCinemaActivity.this, mMap, latLng, new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude()));
            String distance = MapUtil.formatDistance(MapCinemaActivity.this,
                    GoogleMapUtil.getDistance(latLng, new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude())));
            Toast.makeText(MapCinemaActivity.this, distance, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getDataCinema(LatLng latLng) {
        cinemaModelList.clear();
        GetDataCinemaManager getDataCinemaManager = new GetDataCinemaManager(new ResponseCallbackListener<GetAllDataCinemaResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetAllDataCinemaResponse data) {
                if (data.status.equals("200")) {
                    cinemaModelList.addAll(data.result);
                    cinemaPlaceAdapter.notifyDataSetChanged();
                    getLocationCinema(data.result.get(0).name, data.result.get(0).address, latLng);
                    Toast.makeText(MapCinemaActivity.this, data.result.get(0).name, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataCinemaManager.startGetDataCinema(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), null, Config.API_GET_CINEMA);
    }

    private void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationChangeListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBack) {
            finish();
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
        getDataCinema(latLng);
    }
}

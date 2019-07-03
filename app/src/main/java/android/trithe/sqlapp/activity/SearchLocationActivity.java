package android.trithe.sqlapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.PlaceAutocompleteAdapter;
import android.trithe.sqlapp.aplication.MapApplicationData;
import android.trithe.sqlapp.aplication.MyApplication;
import android.trithe.sqlapp.widget.CustomEditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class SearchLocationActivity extends AppCompatActivity implements View.OnClickListener {
    private PlaceAutocompleteAdapter autocompleteAdapter;
    private CustomEditText edtSearch;
    private ImageView imgVoice;
    private ImageView btnRemove;
    private ListView rvPlace;
    private TextView tvNoResult;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(0,
                    MapApplicationData.longitude == null ? 0 : MapApplicationData.longitude),
            new LatLng(MapApplicationData.latitude == null ? 0 : MapApplicationData.latitude,
                    MapApplicationData.longitude == null ? 0 : MapApplicationData.longitude));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        initView();
        setDataAutocompletePlace();
        edtSearchTextChanged();
    }

    private void edtSearchTextChanged() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (after > 0) {
                    btnRemove.setVisibility(View.VISIBLE);
                    imgVoice.setVisibility(View.GONE);
                } else if (start == 0) {
                    btnRemove.setVisibility(View.GONE);
                    imgVoice.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    autocompleteAdapter.getFilter().filter(s.toString());
                }
            }
        });
    }

    private void setDataAutocompletePlace() {
        autocompleteAdapter = new PlaceAutocompleteAdapter(this,
                MyApplication.with(this).getGeoDataClient(), BOUNDS_GREATER_SYDNEY,
                new AutocompleteFilter.Builder()
                        .setCountry("VN")
                        .setCountry("US")
                        .setCountry("UK")
                        .build());
        rvPlace.setAdapter(autocompleteAdapter);
    }

    private void initView() {
        edtSearch = findViewById(R.id.edtSearch);
        imgVoice = findViewById(R.id.imgVoice);
        btnRemove = findViewById(R.id.btnRemove);
        rvPlace = findViewById(R.id.rvPlace);
        tvNoResult = findViewById(R.id.tvNoResult);

        btnRemove.setOnClickListener(this);
        imgVoice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRemove:
                edtSearch.setText("");
                break;
            case R.id.imgVoice:
                break;
        }
    }
}

package android.trithe.sqlapp.activity;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.SeatAdapter;
import android.trithe.sqlapp.callback.OnSeatItemClickListener;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.model.Seats;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataSeatCinemaManager;
import android.trithe.sqlapp.rest.manager.GetDataTheaterCinemaManager;
import android.trithe.sqlapp.rest.response.GetDataSeatResponse;
import android.trithe.sqlapp.rest.response.GetDataTheaterResponse;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.round;

public class BookingSeatsActivity extends AppCompatActivity implements View.OnClickListener, OnSeatItemClickListener {
    private ImageView btnBack;
    private RecyclerView recyclerViewSeat;
    private Button btnBooking;
    private TextView txtSeats;
    private TextView txtMoney;
    private List<Seats> list = new ArrayList<>();
    private SeatAdapter seatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_seats);
        initView();
        setUpAdapter();
        //TODO NHÉ
    }

    private void setUpAdapter() {
        GetDataTheaterCinemaManager getDataTheaterCinemaManager = new GetDataTheaterCinemaManager(new ResponseCallbackListener<GetDataTheaterResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataTheaterResponse data) {
                if (data.status.equals("200")) {
                    seatAdapter = new SeatAdapter(BookingSeatsActivity.this, list, BookingSeatsActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(BookingSeatsActivity.this, data.result.get(0).wide);
                    recyclerViewSeat.setLayoutManager(mLayoutManager);
                    recyclerViewSeat.addItemDecoration(new GridSpacingItemDecorationUtils(data.result.get(0).wide, dpToPx(), true));
                    recyclerViewSeat.setItemAnimator(new DefaultItemAnimator());
                    Toast.makeText(BookingSeatsActivity.this, String.valueOf(data.result.get(0).count), Toast.LENGTH_SHORT).show();
                    recyclerViewSeat.setAdapter(seatAdapter);
                    getDataSeat();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataTheaterCinemaManager.getDataTheaterCinema(getIntent().getIntExtra(Constant.THEATER_ID, 0));
    }

    private void getDataSeat() {
        GetDataSeatCinemaManager getDataSeatCinemaManager = new GetDataSeatCinemaManager(new ResponseCallbackListener<GetDataSeatResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataSeatResponse data) {
                if (data.status.equals("200")) {
                    for (int i = 0; i < data.result.size(); i++) {
                        list.add(new Seats(data.result.get(i), false));
                    }
                    seatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataSeatCinemaManager.getDataSeatCinema(getIntent().getIntExtra(Constant.THEATER_ID, 0));
    }

    private int dpToPx() {
        Resources r = getResources();
        return round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        recyclerViewSeat = findViewById(R.id.recycler_view_seat);
        btnBooking = findViewById(R.id.btnBooking);
        txtSeats = findViewById(R.id.txtSeats);
        txtMoney = findViewById(R.id.txtMoney);

        btnBack.setOnClickListener(this);
        btnBooking.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnBooking:
                Toast.makeText(BookingSeatsActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBookingSeat(List<Seats> list) {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            name.append(" ").append(list.get(i).getList().name).append(",");
        }
        if (name.length() > 0) name = new StringBuilder(name.substring(0, name.length() - 1));
        txtSeats.setText(name);
    }
}

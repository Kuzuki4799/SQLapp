package android.trithe.sqlapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.SeatAdapter;
import android.trithe.sqlapp.callback.OnSeatItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.model.Seats;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataSeatCinemaManager;
import android.trithe.sqlapp.rest.manager.GetDataTheaterCinemaManager;
import android.trithe.sqlapp.rest.response.GetDataSeatResponse;
import android.trithe.sqlapp.rest.response.GetDataTheaterResponse;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.round;

public class BookingSeatsActivity extends AppCompatActivity implements View.OnClickListener, OnSeatItemClickListener {
    private ImageView btnBack;
    private RecyclerView recyclerViewSeat;
    private Button btnBooking;
    private TextView txtSeats;
    private TextView txtMoney;
    private List<Seats> list = new ArrayList<>();
    private SeatAdapter seatAdapter;
    private static final int REQUEST_CODE_PAYMENT = 1;
    // PayPal configuration
    private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(Config.PAYPAL_ENVIRONMENT).clientId(
                    Config.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_seats);
        initView();
        setUpAdapter();
        //TODO NHÉ
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        startService(intent);
        btnBooking.setOnClickListener(v -> launchPayPalPayment());
    }

    private void launchPayPalPayment() {
        PayPalPayment thingsToBuy =
                new PayPalPayment(new BigDecimal(txtMoney.getText().toString().replace("$", "")), "USD", "Payment", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(BookingSeatsActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.e("abc", confirm.toJSONObject().toString(4));
                        Log.e("abc", confirm.getPayment().toJSONObject()
                                .toString(4));

                        String paymentId = confirm.toJSONObject()
                                .getJSONObject("response").getString("id");

                        String payment_client = confirm.getPayment()
                                .toJSONObject().toString();

                        Log.e("abc", "paymentId: " + paymentId
                                + ", payment_json: " + payment_client);

                    } catch (JSONException e) {
                        Log.e("abc", "an extremely unlikely failure occurred: ",
                                e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("abc", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("abc",
                        "An invalid Payment or PayPalConfiguration was submitted.");
            }
        }
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
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            name.append(" ").append(list.get(i).getList().name).append(",");
            sum += list.get(i).getList().score * 70;
        }
        if (name.length() > 0) name = new StringBuilder(name.substring(0, name.length() - 1));
        txtSeats.setText(name);
        txtMoney.setText(sum + "$");
    }
}

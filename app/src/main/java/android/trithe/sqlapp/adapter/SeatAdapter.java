package android.trithe.sqlapp.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.callback.OnSeatItemClickListener;
import android.trithe.sqlapp.model.Seats;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatHolder> {
    private List<Seats> list;
    private Context context;
    private OnSeatItemClickListener onSeatItemClickListener;

    public SeatAdapter(Context context, List<Seats> seriesModelList, OnSeatItemClickListener onSeatItemClickListener) {
        this.list = seriesModelList;
        this.context = context;
        this.onSeatItemClickListener = onSeatItemClickListener;
    }

    class SeatHolder extends RecyclerView.ViewHolder {
        private Button btnSeries;

        public SeatHolder(@NonNull View itemView) {
            super(itemView);
            btnSeries = itemView.findViewById(R.id.btnSeries);
            context = itemView.getContext();
        }
    }

    @NonNull
    @Override
    public SeatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_seats, viewGroup, false);
        return new SeatHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull SeatHolder seatHolder, int position) {
        final Seats seriesModel = list.get(position);
//        seatHolder.btnSeries.setText(seriesModel.getList().name);
        if (seriesModel.getList().status == 1) {
            seatHolder.btnSeries.setBackground(context.getDrawable(R.drawable.buttonkeyboad_gray));
        } else if (seriesModel.getList().status == 2) {
            seatHolder.btnSeries.setBackground(context.getDrawable(R.drawable.buttonkeyboad_conner));
        }
        List<Seats> newList = new ArrayList<>();
        seatHolder.btnSeries.setOnClickListener(v -> {
            seriesModel.setCheck(true);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isCheck()) {
                    newList.add(seriesModel);
                }
            }
            seatHolder.btnSeries.setBackground(context.getDrawable(R.drawable.buttonkeyboad_conner));
            onSeatItemClickListener.onBookingSeat(newList);
        });
    }

    public int getItemCount() {
        return list.size();
    }
}

package android.trithe.sqlapp.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.callback.OnSeriesItemClickListener;
import android.trithe.sqlapp.model.Series;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesHolder> {
    private List<Series> list;
    private Context context;
    private OnSeriesItemClickListener onItemClickListener;

    public SeriesAdapter(Context context, List<Series> seriesModelList) {
        this.list = seriesModelList;
        this.context = context;
    }

    public void setOnItemClickListener(OnSeriesItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class SeriesHolder extends RecyclerView.ViewHolder {
        private Button btnSeries;

        public static final int LAYOUT_ID = R.layout.item_series;

        public SeriesHolder(@NonNull View itemView) {
            super(itemView);
            btnSeries = itemView.findViewById(R.id.btnSeries);
            context = itemView.getContext();
        }
    }

    @NonNull
    @Override
    public SeriesAdapter.SeriesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_series, viewGroup, false);
        return new SeriesHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull SeriesHolder seriesHolder, int position) {
        final Series seriesModel = list.get(position);
        seriesHolder.btnSeries.setText(seriesModel.getList().name);
        if (seriesModel.isCheck()) {
            seriesHolder.btnSeries.setBackground(context.getDrawable(R.drawable.buttonkeyboad_conner));
        }else {
            seriesHolder.btnSeries.setBackground(context.getDrawable(R.drawable.buttonkeyboad_black));
        }
        seriesHolder.btnSeries.setOnClickListener(v -> {
            seriesHolder.btnSeries.setBackground(context.getDrawable(R.drawable.buttonkeyboad_conner));
            onItemClickListener.onFilm(list,seriesModel, position);
        });
    }

    public int getItemCount() {
        return list.size();
    }

}

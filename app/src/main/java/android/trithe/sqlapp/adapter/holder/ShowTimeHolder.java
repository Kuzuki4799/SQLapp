package android.trithe.sqlapp.adapter.holder;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.callback.OnShowTimeCinemaItemClickListener;
import android.trithe.sqlapp.model.ShowingFilmByDate;
import android.trithe.sqlapp.utils.DateUtils;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class ShowTimeHolder extends RecyclerView.ViewHolder {
    private TextView txtShowingTime;
    public static final int LAYOUT_ID = R.layout.item_showing_time;
    private Context context;

    public ShowTimeHolder(@NonNull View itemView) {
        super(itemView);
        txtShowingTime = itemView.findViewById(R.id.txtShowingTime);
        context = itemView.getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupData(List<ShowingFilmByDate> list, ShowingFilmByDate dataModel, OnShowTimeCinemaItemClickListener onShowTimeCinemaItemClickListener) {
        DateUtils.parseDateFormatTime(txtShowingTime, dataModel.getList().time);
        if (dataModel.isCheck()) {
            txtShowingTime.setBackground(context.getDrawable(R.drawable.border_select));
        } else {
            txtShowingTime.setBackground(context.getDrawable(R.drawable.border_not_select));
        }
        txtShowingTime.setOnClickListener(v -> {
            txtShowingTime.setBackground(context.getDrawable(R.drawable.border_select));
            onShowTimeCinemaItemClickListener.onShowingTime(list, dataModel);
        });
    }
}

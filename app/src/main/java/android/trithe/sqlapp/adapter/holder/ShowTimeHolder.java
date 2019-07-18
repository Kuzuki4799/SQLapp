package android.trithe.sqlapp.adapter.holder;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.callback.OnShowTimeCinemaItemClickListener;
import android.trithe.sqlapp.model.ShowingFilmByDate;
import android.trithe.sqlapp.utils.DateUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ShowTimeHolder extends RecyclerView.ViewHolder {
    private TextView txtShowingTime;
    private LinearLayout ll;
    private TextView txtShowingPrice;
    public static final int LAYOUT_ID = R.layout.item_showing_time;
    private Context context;

    public ShowTimeHolder(@NonNull View itemView) {
        super(itemView);
        ll = itemView.findViewById(R.id.ll);
        txtShowingTime = itemView.findViewById(R.id.txtShowingTime);
        txtShowingPrice = itemView.findViewById(R.id.txtShowingPrice);
        context = itemView.getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupData(List<ShowingFilmByDate> list, ShowingFilmByDate dataModel, OnShowTimeCinemaItemClickListener onShowTimeCinemaItemClickListener) {
        DateUtils.parseDateFormatTime(txtShowingTime, dataModel.getList().time);
        txtShowingPrice.setText("$" + dataModel.getList().status);
        if (dataModel.isCheck()) {
            ll.setBackground(context.getDrawable(R.drawable.border_select));
            txtShowingTime.setTextColor(context.getResources().getColor(android.R.color.white));
            txtShowingTime.setTypeface(null, Typeface.BOLD);
        } else {
            txtShowingTime.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            txtShowingTime.setTypeface(null, Typeface.NORMAL);
            ll.setBackground(context.getDrawable(R.drawable.border_not_select));
        }
        ll.setOnClickListener(v -> {
            txtShowingTime.setTextColor(context.getResources().getColor(android.R.color.white));
            txtShowingTime.setTypeface(null, Typeface.BOLD);
            ll.setBackground(context.getDrawable(R.drawable.border_select));
            onShowTimeCinemaItemClickListener.onShowingTime(list, dataModel);
        });
    }
}

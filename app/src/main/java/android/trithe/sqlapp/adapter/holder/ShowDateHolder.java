package android.trithe.sqlapp.adapter.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.rest.model.ShowingDateModel;
import android.trithe.sqlapp.utils.DateUtils;
import android.view.View;
import android.widget.TextView;

public class ShowDateHolder extends RecyclerView.ViewHolder {
    private TextView txtDay;
    public static final int LAYOUT_ID = R.layout.item_day;

    public ShowDateHolder(@NonNull View itemView) {
        super(itemView);
        txtDay = itemView.findViewById(R.id.txtDay);
    }

    public void setupData(final ShowingDateModel dataModel) {
        DateUtils.parseDateFormatUShowing(txtDay, dataModel.date);
    }
}

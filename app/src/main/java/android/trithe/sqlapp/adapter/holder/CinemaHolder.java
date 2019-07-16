package android.trithe.sqlapp.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.CinemaDetailActivity;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.model.CinemaModel;
import android.view.View;
import android.widget.TextView;

public class CinemaHolder extends RecyclerView.ViewHolder {
    private TextView txtCinema;
    private Context context;
    public static final int LAYOUT_ID = R.layout.item_cinema_fragment;

    public CinemaHolder(@NonNull View itemView) {
        super(itemView);
        txtCinema = itemView.findViewById(R.id.txtCinema);
        context = itemView.getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupData(final CinemaModel dataModel) {
        txtCinema.setText(dataModel.name);
        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context,CinemaDetailActivity.class);
            intent.putExtra(Constant.ID,dataModel.id);
            context.startActivity(intent);
        });
    }
}

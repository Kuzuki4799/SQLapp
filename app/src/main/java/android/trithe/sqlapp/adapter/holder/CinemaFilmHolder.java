package android.trithe.sqlapp.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.CinemaDetailActivity;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.model.CinemaModel;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CinemaFilmHolder extends RecyclerView.ViewHolder {
    private ImageView imgAvatar;
    private TextView txtTitle;

    private Context context;
    public static final int LAYOUT_ID = R.layout.item_cinema_film;

    public CinemaFilmHolder(@NonNull View itemView) {
        super(itemView);
        imgAvatar = itemView.findViewById(R.id.imgAvatar);
        txtTitle = itemView.findViewById(R.id.txtTitle);
        context = itemView.getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupData(final CinemaModel dataModel) {
        txtTitle.setText(dataModel.name);
        Glide.with(context).load(Config.LINK_LOAD_IMAGE+dataModel.image).into(imgAvatar);
        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CinemaDetailActivity.class);
            intent.putExtra(Constant.ID, dataModel.id);
            context.startActivity(intent);
        });
    }
}

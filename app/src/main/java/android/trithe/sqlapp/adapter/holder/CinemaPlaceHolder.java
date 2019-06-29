package android.trithe.sqlapp.adapter.holder;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.DetailFilmActivity;
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.model.CinemaModel;
import android.trithe.sqlapp.rest.model.KindModel;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CinemaPlaceHolder extends RecyclerView.ViewHolder {
    private ImageView thumbnail;
    private ImageView imgSaved;
    private TextView txtTitle;
    private TextView txtLocation;

    private Context context;

    private List<KindModel> listKind = new ArrayList<>();

    public static final int LAYOUT_ID = R.layout.item_cinema_map;

    public CinemaPlaceHolder(@NonNull View itemView) {
        super(itemView);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        imgSaved = itemView.findViewById(R.id.imgSaved);
        txtTitle = itemView.findViewById(R.id.txtTitle);
        txtLocation = itemView.findViewById(R.id.txtLocation);
        context = itemView.getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupData(final CinemaModel dataModel) {
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + dataModel.image).into(thumbnail);
        txtTitle.setText(dataModel.name);
        txtLocation.setText(dataModel.address);
    }
}

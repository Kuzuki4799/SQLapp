package android.trithe.sqlapp.adapter.holder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.DetailFilmActivity;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class FilmHolder extends RecyclerView.ViewHolder {
    private ImageView thumbnail;
    private TextView title;
    private TextView txtFormat;
    private TextView txtSeries;
    private Context context;
    private CardView cardView;

    public static final int LAYOUT_ID = R.layout.item_film;

    public FilmHolder(@NonNull View itemView) {
        super(itemView);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.title);
        txtSeries = itemView.findViewById(R.id.txtSeries);
        cardView = itemView.findViewById(R.id.card_view);
        txtFormat = itemView.findViewById(R.id.txtFormat);
        context = itemView.getContext();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupData(final FilmModel dataModel, int key) {
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + dataModel.image).into(thumbnail);
        title.setText(dataModel.name);
        if (dataModel.sizes > 1) {
            txtSeries.setVisibility(View.VISIBLE);
            txtFormat.setVisibility(View.GONE);
            txtSeries.setText(dataModel.series.size() + "/" + dataModel.sizes);
        } else {
            txtSeries.setVisibility(View.GONE);
            txtFormat.setText(dataModel.format);
            txtFormat.setVisibility(View.VISIBLE);
        }

        thumbnail.setOnClickListener(v ->
                {
                    Intent intent = new Intent(context, DetailFilmActivity.class);
                    intent.putExtra(Constant.ID, dataModel.id);
                    intent.putExtra(Constant.KEY_CINEMA_ID, key);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                            cardView, context.getResources().getString(R.string.app_name));
                    ((Activity) context).getWindow().setEnterTransition(null);
                    context.startActivity(intent, options.toBundle());
                }
        );
    }
}

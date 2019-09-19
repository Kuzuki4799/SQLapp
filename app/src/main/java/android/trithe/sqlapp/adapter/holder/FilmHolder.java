package android.trithe.sqlapp.adapter.holder;

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
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataSeriesFilmManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.response.GetDataSeriesFilmResponse;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupData(final FilmModel dataModel, int key) {
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + dataModel.image).into(thumbnail);
        title.setText(dataModel.name);
        txtFormat.setText(dataModel.format);
        if (dataModel.sizes > 1) {
            txtSeries.setVisibility(View.VISIBLE);
            getSeriesFilm(dataModel.id, txtSeries, dataModel.sizes);
        }else {
            txtSeries.setVisibility(View.GONE);
        }
        if (key == 0) {
            thumbnail.setOnClickListener(v ->
                    {
                        Intent intent = new Intent(context, DetailFilmActivity.class);
                        intent.putExtra(Constant.ID, dataModel.id);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, cardView, context.getResources().getString(R.string.app_name));
                        ((Activity) context).getWindow().setEnterTransition(null);
                        context.startActivity(intent, options.toBundle());
                    }
            );
        } else {
            thumbnail.setOnClickListener(v ->
                    {
                        Intent intent = new Intent(context, DetailFilmActivity.class);
                        intent.putExtra(Constant.ID, dataModel.id);
                        intent.putExtra(Constant.KEY_CINEMA_ID, key);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, cardView, context.getResources().getString(R.string.app_name));
                        ((Activity) context).getWindow().setEnterTransition(null);
                        context.startActivity(intent, options.toBundle());
                    }
            );
        }
    }

    private void getSeriesFilm(String id, TextView textView, int sizes) {
        GetDataSeriesFilmManager getDataSeriesFilmManager = new GetDataSeriesFilmManager(new ResponseCallbackListener<GetDataSeriesFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataSeriesFilmResponse data) {
                if (data.status.equals("200")) {
                    textView.setText(data.result.size() + "/" + sizes);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataSeriesFilmManager.startGetDataSeriesFilm(id);
    }
}

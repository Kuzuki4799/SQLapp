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
    private TextView time;
    private TextView txtFormat;
    private TextView txtSeries;
    private Context context;

    public static final int LAYOUT_ID = R.layout.item_film;

    public FilmHolder(@NonNull View itemView) {
        super(itemView);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.title);
        time = itemView.findViewById(R.id.time);
        txtSeries = itemView.findViewById(R.id.txtSeries);
        txtFormat = itemView.findViewById(R.id.txtFormat);
        context = itemView.getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupData(final FilmModel dataModel) {
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + dataModel.image).into(thumbnail);
        title.setText(dataModel.name);
        time.setText(dataModel.time + " min");
        txtFormat.setText(dataModel.format);
        if (dataModel.sizes > 1) {
            txtSeries.setVisibility(View.VISIBLE);
            getSeriesFilm(dataModel.id, txtSeries, dataModel.sizes);
        }
        title.setText(dataModel.name);
        thumbnail.setOnClickListener(v ->
                {
                    Intent intent = new Intent(context, DetailFilmActivity.class);
                    intent.putExtra(Constant.ID, dataModel.id);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, thumbnail, context.getResources().getString(R.string.shareName));
                    context.startActivity(intent, options.toBundle());
                }
        );
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

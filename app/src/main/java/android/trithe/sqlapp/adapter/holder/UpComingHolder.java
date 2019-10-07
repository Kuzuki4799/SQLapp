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
import android.trithe.sqlapp.rest.manager.GetDataKindManager;
import android.trithe.sqlapp.rest.manager.GetDataRatingFilmManager;
import android.trithe.sqlapp.rest.manager.SavedFilmManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.model.KindModel;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataKindResponse;
import android.trithe.sqlapp.rest.response.GetDataRatingFilmResponse;
import android.trithe.sqlapp.utils.DateUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class UpComingHolder extends RecyclerView.ViewHolder {
    private TextView txtTime;
    private ImageView thumbnail;
    private ImageView imgSaved;
    private TextView txtTitle;
    private TextView txtKind;
    private CardView cardView;
    private TextView txtRating;
    private Context context;

    private List<KindModel> listKind = new ArrayList<>();

    public static final int LAYOUT_ID = R.layout.item_up_coming;

    public UpComingHolder(@NonNull View itemView) {
        super(itemView);
        txtTime = itemView.findViewById(R.id.txtTime);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        imgSaved = itemView.findViewById(R.id.imgSaved);
        txtTitle = itemView.findViewById(R.id.txtTitle);
        cardView = itemView.findViewById(R.id.card_view);
        txtKind = itemView.findViewById(R.id.txtKind);
        txtRating = itemView.findViewById(R.id.txtRating);
        context = itemView.getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupData(final FilmModel dataModel, OnFilmItemClickListener onItemClickListener) {
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + dataModel.image).into(thumbnail);
        txtTitle.setText(dataModel.name);
        thumbnail.setOnClickListener(v ->
                {
                    Intent intent = new Intent(context, DetailFilmActivity.class);
                    intent.putExtra(Constant.ID, dataModel.id);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, cardView, context.getResources().getString(R.string.app_name));
                    context.startActivity(intent, options.toBundle());
                }
        );

        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailFilmActivity.class);
            intent.putExtra(Constant.ID, dataModel.id);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, cardView, context.getResources().getString(R.string.app_name));
            context.startActivity(intent, options.toBundle());
        });

        DateUtils.parseDateFormatUS(txtTime, dataModel.releaseDate);

        if (dataModel.saved == 1) {
            Glide.with(context).load(R.drawable.saved).into(imgSaved);
            imgSaved.setOnClickListener(v -> onClickPushSaved(dataModel.id, Config.API_DELETE_SAVED, onItemClickListener));
        } else {
            Glide.with(context).load(R.drawable.not_saved).into(imgSaved);
            imgSaved.setOnClickListener(v -> onClickPushSaved(dataModel.id, Config.API_INSERT_SAVED, onItemClickListener));
        }
        getRatingFilm(dataModel.id);
        getDataKindFilm(dataModel.id);
    }

    private void getDataKindFilm(String id) {
        listKind.clear();
        GetDataKindManager getDataKindManager = new GetDataKindManager(new ResponseCallbackListener<GetDataKindResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataKindResponse data) {
                if (data.status.equals("200")) {
                    listKind.addAll(data.result);
                    getKind();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataKindManager.startGetDataKind(id, Config.API_KIND_FILM_DETAIL);
    }

    private void getKind() {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < listKind.size(); i++) {
            name.append(listKind.get(i).name).append(", ");
        }
        if (name.length() > 0) name = new StringBuilder(name.substring(0, name.length() - 2));
        txtKind.setText(name);
    }

    private void getRatingFilm(String id) {
        GetDataRatingFilmManager getDataRatingFilmManager = new GetDataRatingFilmManager(new ResponseCallbackListener<GetDataRatingFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataRatingFilmResponse data) {
                if (data.status.equals("200")) {
                    double rat = 0.0;
                    for (int i = 0; i < data.result.size(); i++) {
                        rat += data.result.get(i).rat;
                    }
                    txtRating.setText(String.valueOf(rat / data.result.size()));
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataRatingFilmManager.startGetDataRating(id);
    }

    private void onClickPushSaved(String id, String key, OnFilmItemClickListener onItemClickListener) {
        SavedFilmManager savedFilmManager = new SavedFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    onItemClickListener.changSetDataFilm(getAdapterPosition(), key);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        savedFilmManager.startCheckSavedFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, key);
    }
}

package android.trithe.sqlapp.adapter.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.SavedFilmManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class KindDetailHolder extends RecyclerView.ViewHolder {
    private ImageView thumbnail;
    private TextView title;
    private TextView time;
    private TextView txtFormat;
    private ImageView imgSaved;
    private Context context;

    public static final int LAYOUT_ID = R.layout.item_kind_detail;

    public KindDetailHolder(@NonNull View itemView) {
        super(itemView);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.title);
        time = itemView.findViewById(R.id.time);
        txtFormat = itemView.findViewById(R.id.txtFormat);
        imgSaved = itemView.findViewById(R.id.imgSaved);
        context = itemView.getContext();
    }

    public void setupData(final FilmModel dataModel, OnFilmItemClickListener onItemClickListener) {
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + dataModel.image).into(thumbnail);
        title.setText(dataModel.name);
        time.setText(dataModel.time + " min");
        txtFormat.setText(dataModel.format);
        title.setText(dataModel.name);
        thumbnail.setOnClickListener(v -> onItemClickListener.onFilm(dataModel, thumbnail));
        checkSaved(dataModel.saved,dataModel.id, onItemClickListener);
    }

    private void checkSaved(int saved, String id, OnFilmItemClickListener onItemClickListener){
        if(saved == 1){
            Glide.with(context).load(R.drawable.saved).into(imgSaved);
            imgSaved.setOnClickListener(v -> {
                onClickPushSaved(id, Config.API_DELETE_SAVED, onItemClickListener);
            });
        }else {
            Glide.with(context).load(R.drawable.not_saved).into(imgSaved);
            imgSaved.setOnClickListener(v -> {
                onClickPushSaved(id, Config.API_INSERT_SAVED, onItemClickListener);
            });
        }
    }

    private void onClickPushSaved(String id, String key, OnFilmItemClickListener onItemClickListener) {
        SavedFilmManager savedFilmManager = new SavedFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                   onItemClickListener.changSetData();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        savedFilmManager.startCheckSavedFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, key);
    }
}

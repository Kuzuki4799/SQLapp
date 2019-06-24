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
import android.trithe.sqlapp.activity.CastActivity;
import android.trithe.sqlapp.callback.OnCastItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.LovedCastManager;
import android.trithe.sqlapp.rest.model.CastListModel;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CastDetailHolder extends RecyclerView.ViewHolder {
    private ImageView thumbnail;
    private ImageView imgLove;
    private TextView title;
    private TextView figure;
    private Context context;

    public static final int LAYOUT_ID = R.layout.item_cast_detail;

    public CastDetailHolder(@NonNull View itemView) {
        super(itemView);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        imgLove = itemView.findViewById(R.id.imgLove);
        title = itemView.findViewById(R.id.txtName);
        figure = itemView.findViewById(R.id.txtFigure);
        context = itemView.getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupData(final CastListModel dataModel, OnCastItemClickListener onCastItemClickListener) {
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + dataModel.image).into(thumbnail);
        title.setText(dataModel.name);
        figure.setText(dataModel.figure);
        title.setText(dataModel.name);
        thumbnail.setOnClickListener(v -> {
            Intent intent = new Intent(context, CastActivity.class);
            intent.putExtra(Constant.ID, dataModel.castId);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, thumbnail, context.getResources().getString(R.string.shareName));
            context.startActivity(intent, options.toBundle());
        });
        if (dataModel.loved == 1) {
            Glide.with(context).load(R.drawable.love).into(imgLove);
            imgLove.setOnClickListener(v -> onPushLoveCast(dataModel.castId, Config.API_DELETE_LOVE_CAST, onCastItemClickListener));
        } else {
            Glide.with(context).load(R.drawable.unlove).into(imgLove);
            imgLove.setOnClickListener(v -> onPushLoveCast(dataModel.castId, Config.API_INSERT_LOVE_CAST, onCastItemClickListener));
        }
    }

    private void onPushLoveCast(final String id, String key, OnCastItemClickListener onCastItemClickListener) {
        LovedCastManager lovedCastManager = new LovedCastManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    onCastItemClickListener.changSetData();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        lovedCastManager.startCheckSavedFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, key);
    }
}

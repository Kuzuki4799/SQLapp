package android.trithe.sqlapp.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.CastActivity;
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

public class CastHolder extends RecyclerView.ViewHolder {
    private ImageView thumbnail;
    private ImageView imgLove;
    private TextView title;
    private TextView figure;
    private Context context;

    public static final int LAYOUT_ID = R.layout.item_cast;

    public CastHolder(@NonNull View itemView) {
        super(itemView);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        imgLove = itemView.findViewById(R.id.imgLove);
        title = itemView.findViewById(R.id.txtName);
        figure = itemView.findViewById(R.id.txtFigure);
        context = itemView.getContext();
    }

    public void setupData(final CastListModel dataModel) {
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + dataModel.image).into(thumbnail);
        title.setText(dataModel.name);
        figure.setText(dataModel.figure);
        title.setText(dataModel.name);
        checkLoveCast(dataModel.castId);
        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CastActivity.class);
                intent.putExtra("id", dataModel.castId);
                context.startActivity(intent);
            }
        });
    }

    private void checkLoveCast(final String id) {
        LovedCastManager lovedCastManager = new LovedCastManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, final BaseResponse data) {
                if (data.status.equals("200")) {
                    Glide.with(context).load(R.drawable.love).into(imgLove);
                    imgLove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onPushLoveCast(id, Config.API_DELETE_LOVE_CAST);
                            Glide.with(context).load(R.drawable.unlove).into(imgLove);
                        }
                    });
                } else {
                    Glide.with(context).load(R.drawable.unlove).into(imgLove);
                    imgLove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onPushLoveCast(id, Config.API_INSERT_LOVE_CAST);
                            Glide.with(context).load(R.drawable.love).into(imgLove);
                        }
                    });
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        lovedCastManager.startCheckSavedFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, Config.API_LOVE_CAST);
    }

    private void onPushLoveCast(String id, String key) {
        LovedCastManager lovedCastManager = new LovedCastManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {

            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        lovedCastManager.startCheckSavedFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, key);
    }
}

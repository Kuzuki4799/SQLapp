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
import android.trithe.sqlapp.activity.LoginActivity;
import android.trithe.sqlapp.callback.OnRemoveItemClickListener;
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

public class BookMarkHolder extends RecyclerView.ViewHolder {
    private ImageView thumbnail;
    private TextView title;
    private TextView txtFormat;
    private CardView cardView;
    private ImageView imgSaved;
    private TextView txtSeries;
    private Context context;
    private boolean isLogin;

    public static final int LAYOUT_ID = R.layout.item_kind_detail;

    public BookMarkHolder(@NonNull View itemView) {
        super(itemView);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.title);
        txtFormat = itemView.findViewById(R.id.txtFormat);
        imgSaved = itemView.findViewById(R.id.imgSaved);
        txtSeries = itemView.findViewById(R.id.txtSeries);
        cardView = itemView.findViewById(R.id.card_view);
        context = itemView.getContext();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupData(final FilmModel dataModel, OnRemoveItemClickListener onItemClickListener) {
        isLogin = !SharedPrefUtils.getString(Constant.KEY_USER_ID, "").isEmpty();
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
        thumbnail.setOnClickListener(v -> {
            onItemClickListener.onCheckItem(getAdapterPosition(), cardView);
        });
        Glide.with(context).load(R.drawable.saved).into(imgSaved);
        imgSaved.setOnClickListener(v -> onClickPushSaved(dataModel.id, getAdapterPosition(), onItemClickListener));
    }

    private void onClickPushSaved(String id, int position, OnRemoveItemClickListener onItemClickListener) {
        if (isLogin) {
            SavedFilmManager savedFilmManager = new SavedFilmManager(new ResponseCallbackListener<BaseResponse>() {
                @Override
                public void onObjectComplete(String TAG, BaseResponse data) {
                    if (data.status.equals("200")) {
                        onItemClickListener.onRemoveItem(position);
                    }
                }

                @Override
                public void onResponseFailed(String TAG, String message) {

                }
            });
            savedFilmManager.startCheckSavedFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, Config.API_DELETE_SAVED);
        } else {
            Intent intents = new Intent(context, LoginActivity.class);
            context.startActivity(intents);
        }
    }
}

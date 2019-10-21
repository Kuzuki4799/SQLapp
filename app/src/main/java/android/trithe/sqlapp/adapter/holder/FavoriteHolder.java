package android.trithe.sqlapp.adapter.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.callback.OnRemoveItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.LovedCastManager;
import android.trithe.sqlapp.rest.model.CastModel;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class FavoriteHolder extends RecyclerView.ViewHolder {
    private ImageView thumbnail;
    private ImageView imgLove;
    private TextView title;
    private Context context;
    private CardView cardView;

    public static final int LAYOUT_ID = R.layout.item_cast;

    public FavoriteHolder(@NonNull View itemView) {
        super(itemView);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        imgLove = itemView.findViewById(R.id.imgLove);
        title = itemView.findViewById(R.id.txtName);
        cardView = itemView.findViewById(R.id.card_view);
        context = itemView.getContext();
    }

    public void setupData(final CastModel dataModel, OnRemoveItemClickListener onChangeSetItemClickLovedListener) {
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + dataModel.image).into(thumbnail);
        title.setText(dataModel.name);
        title.setText(dataModel.name);
        thumbnail.setOnClickListener(v -> onChangeSetItemClickLovedListener.onCheckItem(getAdapterPosition(), cardView));
        Glide.with(context).load(R.drawable.love).into(imgLove);
        imgLove.setOnClickListener(v -> onPushLoveCast(dataModel.id, onChangeSetItemClickLovedListener));
    }

    private void onPushLoveCast(final String id, OnRemoveItemClickListener onChangeSetItemClickLovedListener) {
        LovedCastManager lovedCastManager = new LovedCastManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    onChangeSetItemClickLovedListener.onRemoveItem(getAdapterPosition());
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        lovedCastManager.pushLovedCast(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, Config.API_DELETE_LOVE_CAST);
    }
}

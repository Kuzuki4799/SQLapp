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
import android.trithe.sqlapp.activity.CinemaDetailActivity;
import android.trithe.sqlapp.callback.OnChangeSetItemClickLovedListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataRatingCinemaManager;
import android.trithe.sqlapp.rest.manager.LovedCinemaManager;
import android.trithe.sqlapp.rest.model.CinemaModel;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataRatingCinemaResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CinemaPlaceHolder extends RecyclerView.ViewHolder {
    private ImageView imgMain;
    private ImageView imgLoved;
    private TextView txtName;
    private ImageView imgRatting;
    private TextView txtRating;
    private Context context;
    public static final int LAYOUT_ID = R.layout.item_cinema_new;

    public CinemaPlaceHolder(@NonNull View itemView) {
        super(itemView);
        imgMain = itemView.findViewById(R.id.imgMain);
        imgLoved = itemView.findViewById(R.id.imgLoved);
        txtName = itemView.findViewById(R.id.txtName);
        imgRatting = itemView.findViewById(R.id.imgRatting);
        txtRating = itemView.findViewById(R.id.txtRating);
        context = itemView.getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupData(final CinemaModel dataModel, OnChangeSetItemClickLovedListener onChangeSetItemClickLovedListener) {
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + dataModel.image).into(imgMain);
        txtName.setText(dataModel.name);
        getRatCinema(dataModel.id);
        if (dataModel.loved == 1) {
            Glide.with(context).load(R.drawable.love).into(imgLoved);
            imgLoved.setOnClickListener(v -> onPushLoveCast(dataModel.id, Config.API_DELETE_LOVE_CINEMA, onChangeSetItemClickLovedListener));
        } else {
            Glide.with(context).load(R.drawable.unlove).into(imgLoved);
            imgLoved.setOnClickListener(v -> onPushLoveCast(dataModel.id, Config.API_INSERT_LOVE_CINEMA, onChangeSetItemClickLovedListener));
        }
        imgMain.setOnClickListener(v -> {
            Intent intent = new Intent(context, CinemaDetailActivity.class);
            intent.putExtra(Constant.ID, dataModel.id);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, imgMain, context.getResources().getString(R.string.shareName));
            context.startActivity(intent, options.toBundle());
        });
    }


    private void onPushLoveCast(final String id, String key, OnChangeSetItemClickLovedListener onChangeSetItemClickLovedListener) {
        LovedCinemaManager lovedCinemaManager = new LovedCinemaManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    onChangeSetItemClickLovedListener.changSetData();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        lovedCinemaManager.pushLovedCinema(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, key);
    }

    private void getRatCinema(String id) {
        GetDataRatingCinemaManager getDataRatingCinemaManager = new GetDataRatingCinemaManager(new ResponseCallbackListener<GetDataRatingCinemaResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataRatingCinemaResponse data) {
                if (data.status.equals("200")) {
                    double rat = 0.0;
                    for (int i = 0; i < data.result.size(); i++) {
                        rat += data.result.get(i).rat;
                    }
                    txtRating.setText(String.valueOf(rat / data.result.size()));
                    try {
                        checkRating(rat / data.result.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataRatingCinemaManager.startGetDataRating(id);
    }

    private void checkRating(double rat) {
        if (rat == 5.0) {
            Glide.with(context).load(R.drawable.fivestar).into(imgRatting);
        } else if (rat >= 4.0) {
            Glide.with(context).load(R.drawable.fourstar).into(imgRatting);
        } else if (rat >= 3.0) {
            Glide.with(context).load(R.drawable.threestar).into(imgRatting);
        } else if (rat >= 2.0) {
            Glide.with(context).load(R.drawable.twostar).into(imgRatting);
        } else if (rat >= 1.0) {
            Glide.with(context).load(R.drawable.onestar).into(imgRatting);
        }
    }

}

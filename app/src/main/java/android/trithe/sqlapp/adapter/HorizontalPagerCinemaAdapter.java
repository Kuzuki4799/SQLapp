package android.trithe.sqlapp.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.DetailFilmActivity;
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.model.Series;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataRatingFilmManager;
import android.trithe.sqlapp.rest.manager.SavedFilmManager;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.model.SeriesModel;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.rest.response.GetDataRatingFilmResponse;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HorizontalPagerCinemaAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<FilmModel> filmModelList;

    private ImageView thumbnail;
    private ImageView imgSaved;
    private TextView txtRating;
    private TextView txtName;
    private RecyclerView recyclerViewCalendar;
    private OnFilmItemClickListener onItemClickListener;

    public HorizontalPagerCinemaAdapter(List<FilmModel> list, final Context mContext, OnFilmItemClickListener mOnItemClickListener) {
        context = mContext;
        mLayoutInflater = LayoutInflater.from(context);
        filmModelList = list;
        this.onItemClickListener = mOnItemClickListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NotNull
    @Override
    public Object instantiateItem(@NotNull final ViewGroup container, final int position) {
        View view = mLayoutInflater.inflate(R.layout.item_cinema, container, false);
        setUpItem(view, filmModelList, position);
        container.addView(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpItem(View view, List<FilmModel> filmModelList, int position) {
        thumbnail = view.findViewById(R.id.thumbnail);
        imgSaved = view.findViewById(R.id.imgSaved);
        txtRating = view.findViewById(R.id.txtRating);
        txtName = view.findViewById(R.id.txtName);
        recyclerViewCalendar = view.findViewById(R.id.recycler_view_calendar);


        List<Series> stringArrayList = new ArrayList<>();
        stringArrayList.add(new Series(new SeriesModel("2", "1", "2", "adasd"), true));
        stringArrayList.add(new Series(new SeriesModel("2", "2", "2", "adasd"), true));
        stringArrayList.add(new Series(new SeriesModel("2", "3", "2", "adasd"), true));
        stringArrayList.add(new Series(new SeriesModel("2", "4", "2", "adasd"), true));
        stringArrayList.add(new Series(new SeriesModel("2", "5", "2", "adasd"), true));
        stringArrayList.add(new Series(new SeriesModel("2", "6", "2", "adasd"), true));
        stringArrayList.add(new Series(new SeriesModel("2", "7", "2", "adasd"), true));
        stringArrayList.add(new Series(new SeriesModel("2", "8", "2", "adasd"), true));
        stringArrayList.add(new Series(new SeriesModel("2", "9", "2", "adasd"), true));
        stringArrayList.add(new Series(new SeriesModel("2", "10", "2", "adasd"), true));
        stringArrayList.add(new Series(new SeriesModel("2", "11", "2", "adasd"), true));

        SeriesAdapter seriesAdapter = new SeriesAdapter(context, stringArrayList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 4);
        recyclerViewCalendar.setLayoutManager(mLayoutManager);
        recyclerViewCalendar.addItemDecoration(new GridSpacingItemDecorationUtils(4, dpToPx(), true));
        recyclerViewCalendar.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCalendar.setAdapter(seriesAdapter);


        FilmModel filmModel = filmModelList.get(position);
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + filmModel.image).into(thumbnail);
        txtName.setText(filmModel.name);
        getRatingFilm(filmModel.id);
        if (filmModel.saved == 1) {
            Glide.with(context).load(R.drawable.saved).into(imgSaved);
            imgSaved.setOnClickListener(v -> onClickPushSaved(filmModel.id, Config.API_DELETE_SAVED));
        } else {
            Glide.with(context).load(R.drawable.not_saved).into(imgSaved);
            imgSaved.setOnClickListener(v -> onClickPushSaved(filmModel.id, Config.API_INSERT_SAVED));
        }
        thumbnail.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailFilmActivity.class);
            intent.putExtra(Constant.ID, filmModel.id);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, thumbnail, context.getResources().getString(R.string.shareName));
            context.startActivity(intent, options.toBundle());
        });
    }


    private int dpToPx() {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
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

    public int getItemPosition(@NotNull Object object) {
        return POSITION_NONE;
    }

    private void onClickPushSaved(String id, String key) {
        SavedFilmManager savedFilmManager = new SavedFilmManager(new ResponseCallbackListener<BaseResponse>() {
            @Override
            public void onObjectComplete(String TAG, BaseResponse data) {
                if (data.status.equals("200")) {
                    onItemClickListener.changSetDataFilm();
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        savedFilmManager.startCheckSavedFilm(SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), id, key);
    }

    @Override
    public int getCount() {
        return filmModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

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
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.model.Series;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.trithe.sqlapp.rest.model.SeriesModel;
import android.trithe.sqlapp.utils.GridSpacingItemDecorationUtils;
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
    private TextView txtName;
    private RecyclerView recyclerViewCalendar;

    public HorizontalPagerCinemaAdapter(List<FilmModel> list, final Context mContext) {
        context = mContext;
        mLayoutInflater = LayoutInflater.from(context);
        filmModelList = list;
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
        txtName = view.findViewById(R.id.txtName);
        recyclerViewCalendar = view.findViewById(R.id.recycler_view_calendar);

        List<Series> stringArrayList = new ArrayList<>();
        if(position == 0){
            stringArrayList.add(new Series(new SeriesModel("2", "1", "2", "adasd"), true));
            stringArrayList.add(new Series(new SeriesModel("2", "2", "2", "adasd"), true));
            stringArrayList.add(new Series(new SeriesModel("2", "3", "2", "adasd"), true));

        }else if(position == 1){
            stringArrayList.add(new Series(new SeriesModel("2", "1", "2", "adasd"), true));
            stringArrayList.add(new Series(new SeriesModel("2", "2", "2", "adasd"), true));
            stringArrayList.add(new Series(new SeriesModel("2", "3", "2", "adasd"), true));
            stringArrayList.add(new Series(new SeriesModel("2", "4", "2", "adasd"), true));
        }else {
            stringArrayList.add(new Series(new SeriesModel("2", "1", "2", "adasd"), true));
            stringArrayList.add(new Series(new SeriesModel("2", "2", "2", "adasd"), true));
            stringArrayList.add(new Series(new SeriesModel("2", "3", "2", "adasd"), true));
            stringArrayList.add(new Series(new SeriesModel("2", "4", "2", "adasd"), true));
            stringArrayList.add(new Series(new SeriesModel("2", "5", "2", "adasd"), true));
        }

        SeriesAdapter seriesAdapter = new SeriesAdapter(context, stringArrayList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 4);
        recyclerViewCalendar.setLayoutManager(mLayoutManager);
        recyclerViewCalendar.addItemDecoration(new GridSpacingItemDecorationUtils(4, dpToPx(), true));
        recyclerViewCalendar.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCalendar.setAdapter(seriesAdapter);

        FilmModel filmModel = filmModelList.get(position);
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + filmModel.image).into(thumbnail);
        txtName.setText(filmModel.name);
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

    public int getItemPosition(@NotNull Object object) {
        return POSITION_NONE;
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

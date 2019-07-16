package android.trithe.sqlapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.DetailFilmActivity;
import android.trithe.sqlapp.callback.OnChangeSetItemClickLovedListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.SavedFilmManager;
import android.trithe.sqlapp.rest.model.ShowingFilmCinemaModel;
import android.trithe.sqlapp.rest.response.BaseResponse;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HorizontalPagerCinemaAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<ShowingFilmCinemaModel> filmModelList;
    private ImageView thumbnail;
    private TextView txtName;

    public HorizontalPagerCinemaAdapter(List<ShowingFilmCinemaModel> list, final Context mContext) {
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
    private void setUpItem(View view, List<ShowingFilmCinemaModel> filmModelList, int position) {
        thumbnail = view.findViewById(R.id.thumbnail);
        txtName = view.findViewById(R.id.txtName);
        ShowingFilmCinemaModel filmModel = filmModelList.get(position);
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + filmModel.image).into(thumbnail);
        txtName.setText(filmModel.name);
        thumbnail.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailFilmActivity.class);
            intent.putExtra(Constant.ID, filmModel.filmId);
            context.startActivity(intent);
        });

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

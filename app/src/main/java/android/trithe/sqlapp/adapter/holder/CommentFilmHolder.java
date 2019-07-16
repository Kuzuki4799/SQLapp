package android.trithe.sqlapp.adapter.holder;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataUserManager;
import android.trithe.sqlapp.rest.model.CommentFilmModel;
import android.trithe.sqlapp.rest.request.DataUserInfoRequest;
import android.trithe.sqlapp.rest.response.GetDataUserResponse;
import android.trithe.sqlapp.utils.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentFilmHolder extends RecyclerView.ViewHolder {
    private CircleImageView commentImage;
    private TextView commentUsername;
    private TextView commentMessage;
    private TextView textTime;

    private Context context;

    public static final int LAYOUT_ID = R.layout.item_comment;

    public CommentFilmHolder(@NonNull View itemView) {
        super(itemView);
        commentImage = itemView.findViewById(R.id.comment_image);
        commentUsername = itemView.findViewById(R.id.comment_username);
        commentMessage = itemView.findViewById(R.id.comment_message);
        textTime = itemView.findViewById(R.id.text_time);
        context = itemView.getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupData(final CommentFilmModel commentFilmModel) {
        DateUtils.parseDateFormat(textTime,commentFilmModel.time);
        commentMessage.setText(commentFilmModel.content);
        getDataUserById(commentFilmModel.userId, commentUsername, commentImage);
    }

    private void getDataUserById(String id, TextView name, CircleImageView imageView) {
        DataUserInfoRequest dataUserInfoRequest = new DataUserInfoRequest(id, null, null, null,null,null,0);
        GetDataUserManager getDataUserManager = new GetDataUserManager(new ResponseCallbackListener<GetDataUserResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataUserResponse data) {
                if (data.status.equals("200")) {
                    name.setText(data.result.name);
                    Glide.with(context).load(Config.LINK_LOAD_IMAGE + data.result.image).into(imageView);
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataUserManager.startGetDataInfo(dataUserInfoRequest, Config.API_GET_USER_BY_ID);
    }
}

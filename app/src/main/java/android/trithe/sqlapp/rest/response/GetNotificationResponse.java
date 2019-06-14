package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.NotificationModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetNotificationResponse extends BaseResponse {
    @SerializedName("result")
    public List<NotificationModel> result = new ArrayList<>();
}

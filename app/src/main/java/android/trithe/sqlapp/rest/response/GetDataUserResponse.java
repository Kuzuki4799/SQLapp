package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.UserModel;
import com.google.gson.annotations.SerializedName;

public class GetDataUserResponse extends BaseResponse  {
    @SerializedName("result")
    public UserModel result;
}

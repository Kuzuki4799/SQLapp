package android.trithe.sqlapp.rest.callback;

public interface ResponseCallbackListener<T> {

    void onObjectComplete(String TAG, T data);

    void onResponseFailed(String TAG, String message);
}
